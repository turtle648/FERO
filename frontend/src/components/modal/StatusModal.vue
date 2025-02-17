<template>
<<<<<<< HEAD
    <div class="modal" @click="closeStatusModal">
      <div class="modal-content" @click.stop>
        <!-- 모달 종료 버튼 -->
        <button id="close-btn" @click="closeStatusModal">X</button>
        <!-- 요소 1: 닉네임, 레벨, 랭크-->
        <div class="status-element status-1">
          <!-- 차후, png로 대체할 것 -->
          <h2>Status</h2>
          <div style="margin-top: 2%;">Nick: {{ userInfo.userNickname }}</div>
          <div style="margin-top: 2%;">
            Lv. {{ userInfo.level }} | Rank: !!api수정을 통해 받기!!
          </div>
        </div>
        <!-- 요소 2 -->
        <div class="status-element status-2">
            <div>
                <!-- 전체 승: !!api수정을 통해 받기!! | 총 플레이 수: !!api수정을 통해 받기!! | myBest: !!api수정을 통해 받기!! -->
            </div>
            <div>
                <!-- 월간 승: !!전척api완성후추가!! | 월간 게임 수: !!전척api완성후추가!! | myBest: !!전척api완성후추가!! -->
            </div>
        </div>
  
        <!-- 요소 3: 퀘스트 달성률, 게이지바 -->
        <div class="status-element status-3">
        <div style="margin-bottom: 2%;">
            <!-- <span style="margin-right: 10%;">미션 클리어 수: !!퀘스트api완성후추가!!</span> -->
            <!-- <span>월간 클리어 수: !!퀘스트api완성후추가!!</span> -->
=======
  <div class="modal" @click="closeStatusModal">
    <div class="modal-content" @click.stop>
      <!-- 모달 종료 버튼 -->
      <button id="close-btn" @click="closeStatusModal">X</button>
      <!-- 요소 1: 닉네임, 레벨, 랭크-->
      <div class="status-element status-1">
        <!-- 차후, png로 대체할 것 -->
        <h2>Status</h2>
        <div style="margin-top: 2%">닉네임: {{ userInfo.userNickname }}</div>
        <div style="margin-top: 2%">
          Lv. {{ userInfo.level }} | Rank: !!api수정을 통해 받기!!
        </div>
      </div>
      <!-- 요소 2 -->
      <div class="status-element status-2">
        <div>
          전체 승: !!api수정을 통해 받기!! | 총 플레이 수: !!api수정을 통해
          받기!! | myBest: !!api수정을 통해 받기!!
        </div>
        <div>
          월간 승: !!전척api완성후추가!! | 월간 게임 수: !!전척api완성후추가!! |
          myBest: !!전척api완성후추가!!
        </div>
      </div>

      <!-- 요소 3: 퀘스트 달성률, 게이지바 -->
      <div class="status-element status-3">
        <div style="margin-bottom: 2%">
          <span style="margin-right: 10%"
            >미션 클리어 수: !!퀘스트api완성후추가!!</span
          >
          <span>월간 클리어 수: !!퀘스트api완성후추가!!</span>
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
        </div>
        <div class="progress-box">
          일일 달성률:
          <!-- 여기서 박스를 채우기 위해 v-for로 박스를 순회하며 스타일을 적용 -->
          <span
            v-for="n in 5"
            :key="n"
            :class="{
              filled: (85 % 100) / 20 >= n,
              empty: (85 % 100) / 20 < n,
            }"
            class="progress-box-cell"
          >
          </span>
          <!-- (!!퀘스트api완성후추가!!%) -->
          85%
          <!-- ({{ userStatus.todayQuestProgress }}%) -->
        </div>
      </div>

      <!-- 요소 4: 그래프 이미지 박스 -->
      <div class="status-element status-4">
        <div class="status-graph">
          <PolarArea :data="chartData" :options="options" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits, ref, computed } from "vue";
import { useUserDataStore } from "@/stores/userDataStore";
import {
  Chart as ChartJS,
  RadialLinearScale,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";
import { PolarArea } from "vue-chartjs";
import * as chartConfig from "../config/chartConfig.js";

ChartJS.register(RadialLinearScale, ArcElement, Tooltip, Legend);

const options = ref(chartConfig.options);
const userDataStore = useUserDataStore();
const userInfo = ref(userDataStore.userInfo);

const chartData = computed(() => ({
  labels: ["팔", "다리", "가슴", "복근", "등", "체력"],
  datasets: [
    {
      label: "운동 데이터",
      backgroundColor: "rgba(236, 72, 153, 1)",
      pointBackgroundColor: "rgba(236, 72, 153, 1)",
      pointBorderColor: "#fff",
      pointHoverBackgroundColor: "#fff",
      pointHoverBorderColor: "rgba(236, 72, 153, 1)",
      data: getSelectedDateData(),
    },
  ],
}));

const getSelectedDateData = () => {
  const data = new Array(
    userInfo.value.userStats.armsStats,
    userInfo.value.userStats.legsStats,
    userInfo.value.userStats.chestStats,
    userInfo.value.userStats.absStats,
    userInfo.value.userStats.backStats,
    userInfo.value.userStats.staminaStats
  );

  console.log(data);

  return data ? data : [0, 0, 0, 0, 0, 0];
};

const emit = defineEmits(["close-modal"]);

// 모달 외부 클릭 시 상태창 모달 종료
const closeStatusModal = () => {
  emit("close-modal");
};
</script>

<style scoped>
/* 임시 */
h2 {
  margin: 0px;
}

/* 모달 관련 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5); /* 배경에 반투명 검정색 추가 */
}

.modal-content {
  position: absolute;
  background: rgba(
    255,
    255,
    255,
    0.8
  ); /* 콘텐츠 영역 배경을 반투명 흰색으로 설정 */
  padding: 0;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  width: 90%;
  height: 70%;
  text-align: center;
  display: flex;
  flex-direction: column; /* 요소들을 세로로 나열 */
}

/* 모달 내부요소 사이즈 지정 */
.status-1 {
  width: 100%; /* 너비는 100% */
  height: 20%; /* 높이를 20%로 설정 */
  background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
  border: solid;
}

.status-2 {
  width: 100%; /* 너비는 100% */
  height: 10%; /* 높이를 10%로 설정 */
  background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
  border: solid;
}

.status-3 {
  width: 100%; /* 너비는 100% */
  height: 10%; /* 높이를 10%로 설정 */
  background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
  border: solid;
}

.status-4 {
  width: 100%; /* 너비는 100% */
  height: 60%; /* 높이를 10%로 설정 */
  background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
  border: solid;
}

.status-graph {
  position: relative;
  width: 100%; /* 부모 요소의 너비에 맞추기 */
  height: 100%; /* 부모 요소의 높이에 맞추기 */
}

.status-graph::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0; /* 요소 안에서 이미지가 꽉 차도록 설정 */
  background-image: url("@/assets/images/status/statusBackgroundImage.png");
  background-size: contain; /* 이미지를 요소 크기에 맞게 꽉 차게 설정 */
  background-repeat: no-repeat; /* 이미지 반복을 막기 */
  background-position: center; /* 이미지의 중심을 기준으로 배치 */
  opacity: 1; /* 투명도 설정 */
  z-index: -1; /* 이미지가 요소 뒤에 위치하도록 설정 */
}

/* progress-box는 박스를 감싸는 div */
.progress-box {
  display: flex;
  width: 100%; /* 5개의 박스 크기를 맞추기 위한 설정 */
  justify-content: space-between;
}

/* progress-box-cell은 각 박스의 스타일 */
.progress-box-cell {
  width: 10%;
  border: 2px solid #ccc;
  background-color: #fff;
  border-radius: 2px;
}

.filled {
  background-color: #4caf50; /* 채워진 박스 색상 */
}

.empty {
  background-color: #ddd; /* 비어있는 박스 색상 */
}

#close-btn {
  position: absolute;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: xx-small;
}

#close-btn {
  top: 2%;
  right: 2%;
}
</style>
