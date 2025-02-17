// stores/mainStore.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

// 튜토리얼 타입 상수화 (확장성 고려)
export const TUTORIAL_IDS = {
  UI: 99,
  PUSHUP: 1,
  SQUAT: 2,
  LUNGE: 3,
  PLANK: 4,
};

export const useMainStore = defineStore("main", () => {
  const tutorial = ref([]);
  const authToken = ref(localStorage.getItem("authToken"));
  const uiTutorialCompleted = ref(false);

  const api = axios.create({
    baseURL: "https://i12e103.p.ssafy.io:8076/api/v1",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${authToken.value}`,
    },
  });

  // 인터셉터 설정 (디버깅 강화)
  api.interceptors.request.use((config) => {
    console.groupCollapsed(
      `[🌐 REQ] ${config.method?.toUpperCase()} ${config.url}`
    );
    console.log("Headers:", config.headers);
    return config;
  });

  api.interceptors.response.use(
    (response) => {
      console.log("✅ RES:", response.status);
      console.table(response.data);
      console.groupEnd();
      return response;
    },
    (error) => {
      console.error("🚨 ERR:", error.response?.status || "NO_STATUS");
      console.error("Data:", error.response?.data || "NO_DATA");
      console.groupEnd();
      return Promise.reject(error);
    }
  );

  // 통합 튜토리얼 완료 함수
  async function completeTutorial(tutorialId) {
    try {
      console.time(`[⏱️] Tutorial #${tutorialId}`);
      const response = await api.post(`/Tutorial/complete/${tutorialId}`);

      // 상태 업데이트
      if (tutorialId === TUTORIAL_IDS.UI) {
        uiTutorialCompleted.value = true;
      } else {
        const target = tutorial.value.find((t) => t.tutorialId === tutorialId);
        if (target) target.completed = true;
      }

      // 응답의 상태 코드로 성공 여부 판단
      return response.status === 200;
    } catch (error) {
      console.error(`[🔥] Tutorial #${tutorialId} Error:`, {
        message: error.message,
        config: error.config,
      });
      return false;
    } finally {
      console.timeEnd(`[⏱️] Tutorial #${tutorialId}`);
    }
  }

  // 튜토리얼 데이터 로드
  async function loadTutorial() {
    try {
      const response = await api.get("/Tutorial/simple");
      tutorial.value = response.data;
      console.log(
        "[📊] Tutorial Data:",
        JSON.parse(JSON.stringify(tutorial.value))
      );
    } catch (error) {
      console.error("[❗] Load Error:", error);
      throw error;
    }
  }

  // 퀘스트 한달치
  async function isQuestCompleted(year, month) {
    console.log(month, year)
    try {
      console.time(`[⏱️] Sending Date Year: ${year}, Month: ${month}`);

      // API 호출
      const data = { year, month };
      const response = await api.get("/exercise/monthly", data);

      console.log("✅ Date Sent Successfully:", response.data);
      return response.data; // 성공 시 응답 데이터 반환
    } catch (error) {
      console.error(`[🔥] Error Sending Date Year: ${year}, Month: ${month}`, {
        message: error.message,
        config: error.config,
      });
      throw error; // 호출한 컴포넌트에서 에러를 처리할 수 있도록 던짐
    } finally {
      console.timeEnd(`[⏱️] Sending Date Year: ${year}, Month: ${month}`);
    }
  }

  async function getMonthStatus(year, month) {
    const params = { month, year };
    const response = await api.get("/userStats/history", { params });
    return response.data;
  }

  return {
    tutorial,
    authToken,
    uiTutorialCompleted,
    TUTORIAL_IDS, // 상수 노출
    loadTutorial,
    completeTutorial,
    isQuestCompleted,
    getMonthStatus,
  };
});
