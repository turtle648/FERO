<template>
  <div class="counter-container">
    <div class="count">스쿼트 횟수: {{ count }}</div>
    <div class="status">{{ feedback }}</div>
    <div v-if="formFeedback" class="form-feedback">{{ formFeedback }}</div>
    <div v-if="!isReady" class="form-feedback">측면으로 서주세요!</div>

    <!-- 손과 무릎 거리 정보 -->
    <div class="coords">
      <p>📏 손-무릎 거리:</p>
      <p>
        👈 왼쪽: {{ leftHandKneeDist.toFixed(3) }} / 👉 오른쪽: {{ rightHandKneeDist.toFixed(3) }}
      </p>
    </div>

    <WebCamera @pose-detected="processPose" />
  </div>
</template>

<script>
import { ref } from "vue";
import WebCamera from "./WebCamera.vue";

export default {
  name: "SquatCounter",
  components: {
    WebCamera,
  },
  setup() {
    const count = ref(0);
    const state = ref("up"); // "up" (완전히 선 상태), "down" (완전히 앉은 상태), "middle" (중간 단계)
    const feedback = ref("준비중...");
    const isReady = ref(false);
    const handTouchedKnee = ref(false); // 📌 손이 무릎에 닿았는지 확인하는 변수

    // 손과 무릎 사이 거리 저장
    const leftHandKneeDist = ref(0);
    const rightHandKneeDist = ref(0);

    // 📌 유클리드 거리 계산 함수 (손과 무릎 사이 거리 측정)
    const calculateDistance = (p1, p2) => {
      return Math.sqrt((p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2);
    };

    // 📌 각도 계산 함수
    const calculateAngle = (a, b, c) => {
      const radians = Math.atan2(c.y - b.y, c.x - b.x) - Math.atan2(a.y - b.y, a.x - b.x);
      let degrees = Math.abs((radians * 180.0) / Math.PI);
      if (degrees > 180.0) degrees = 360 - degrees;
      return degrees;
    };

    // 📌 스쿼트 동작 처리 함수
    const processPose = (landmarks) => {
      if (!landmarks || landmarks.length === 0) {
        isReady.value = false;
        feedback.value = "준비중...";
        return;
      }

      isReady.value = true;

      const leftHip = landmarks[23];
      const rightHip = landmarks[24];
      const leftKnee = landmarks[25];
      const rightKnee = landmarks[26];
      const leftHand = landmarks[15];
      const rightHand = landmarks[16];

      const leftKneeAngle = calculateAngle(leftHip, leftKnee, landmarks[27]);
      const rightKneeAngle = calculateAngle(rightHip, rightKnee, landmarks[28]);
      const avgKneeAngle = (leftKneeAngle + rightKneeAngle) / 2;

      // 손과 무릎 사이 거리 측정
      leftHandKneeDist.value = calculateDistance(leftHand, leftKnee);
      rightHandKneeDist.value = calculateDistance(rightHand, rightKnee);

      // 📌 손과 무릎 사이 거리가 0.1 이하로 떨어진 적이 한 번이라도 있으면 기록
      // 하지만 "up" 상태에서는 무시함
      if (
        state.value !== "up" &&
        (leftHandKneeDist.value <= 0.1 || rightHandKneeDist.value <= 0.1)
      ) {
        handTouchedKnee.value = true;
      }

      // 📌 스쿼트 상태 변환
      if (state.value === "up" && avgKneeAngle < 100) {
        state.value = "down";
        feedback.value = "Down";
      } else if (state.value === "down" && avgKneeAngle >= 120 && avgKneeAngle < 165) {
        state.value = "middle";
        feedback.value = "Middle";
      } else if (state.value === "middle" && avgKneeAngle >= 165) {
        state.value = "up";

        if (!handTouchedKnee.value) {
          count.value++;
          feedback.value = `Up! Count: ${count.value}`;
        } else {
          feedback.value = "손을 무릎에 대고 있어서 카운팅되지 않음!";
        }

        // up 상태로 복귀하면 손 닿음 상태 초기화
        handTouchedKnee.value = false;
      }
    };

    return {
      count,
      processPose,
      feedback,
      isReady,
      leftHandKneeDist,
      rightHandKneeDist,
    };
  },
};
</script>

<style scoped>
.counter-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.count {
  font-size: 24px;
  font-weight: bold;
}

.status {
  font-size: 18px;
  color: #666;
  margin-bottom: 10px;
}

.form-feedback {
  font-size: 14px;
  color: #888;
  font-style: italic;
}

.coords {
  font-size: 14px;
  color: #333;
  margin-top: 10px;
}
</style>
