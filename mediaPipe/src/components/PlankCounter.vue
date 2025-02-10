<template>
  <div class="counter-container">
    <div class="count">플랭크 시간: {{ formatTime(time) }}</div>
    <div class="status" :class="{ 'status-warning': !isGoodPose }">{{ feedback }}</div>
    <WebCamera @pose-detected="processPose" />
  </div>
</template>

<script>
import { ref, onUnmounted } from "vue";
import WebCamera from "./WebCamera.vue";

export default {
  name: "PlankCounter",
  components: {
    WebCamera,
  },
  setup() {
    const time = ref(0);
    const feedback = ref("준비중...");
    const isGoodPose = ref(true);
    let timer = null;

    const formatTime = (seconds) => {
      const mins = Math.floor(seconds / 60);
      const secs = seconds % 60;
      return `${mins}:${secs.toString().padStart(2, "0")}`;
    };

    // 자세 체크 함수
    const checkPlankPose = (landmarks) => {
      if (!landmarks) return false;

      // 어깨, 엉덩이, 발목의 위치를 확인
      const leftShoulder = landmarks[11];
      const rightShoulder = landmarks[12];
      const leftHip = landmarks[23];
      const rightHip = landmarks[24];
      const leftAnkle = landmarks[27];
      const rightAnkle = landmarks[28];

      // 어깨와 엉덩이의 y좌표 차이 확인 (수평 유지)
      const shoulderY = (leftShoulder.y + rightShoulder.y) / 2;
      const hipY = (leftHip.y + rightHip.y) / 2;
      const ankleY = (leftAnkle.y + rightAnkle.y) / 2;

      // 허용 오차 범위 (조정 가능)
      const tolerance = 0.1;

      // 어깨-엉덩이-발목이 일직선 상에 있는지 확인
      const isAligned =
        Math.abs(shoulderY - hipY) < tolerance && Math.abs(hipY - ankleY) < tolerance;

      return isAligned;
    };

    // 타이머 시작
    const startTimer = () => {
      if (timer) return;

      timer = setInterval(() => {
        if (isGoodPose.value) {
          time.value++;
          feedback.value = "자세가 좋습니다!";
        } else {
          feedback.value = "허리를 수평으로 유지해주세요!";
        }
      }, 1000);
    };

    // 타이머 정지
    const stopTimer = () => {
      if (timer) {
        clearInterval(timer);
        timer = null;
      }
    };

    const processPose = (landmarks) => {
      isGoodPose.value = checkPlankPose(landmarks);

      // 처음 올바른 자세가 감지되면 타이머 시작
      if (isGoodPose.value && !timer) {
        startTimer();
        feedback.value = "자세가 좋습니다!";
      }
    };

    // 컴포넌트가 제거될 때 타이머 정리
    onUnmounted(() => {
      stopTimer();
    });

    return {
      time,
      processPose,
      feedback,
      formatTime,
      isGoodPose,
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
  color: #4caf50;
  margin-bottom: 10px;
  transition: color 0.3s;
}

.status-warning {
  color: #f44336;
}
</style>
