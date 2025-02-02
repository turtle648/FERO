<template>
  <div class="counter-container">
    <div class="count">런지 횟수: {{ count }}</div>
    <div class="status">{{ feedback }}</div>
    <WebCamera @pose-detected="processPose" />
  </div>
</template>

<script>
import { ref } from "vue";
import WebCamera from "./WebCamera.vue";

export default {
  name: "LungeCounter",
  components: {
    WebCamera,
  },
  setup() {
    const count = ref(0);
    const isDown = ref(false);
    const feedback = ref("준비중...");

    // 각도 계산 함수
    const calculateAngle = (p1, p2, p3) => {
      const radians = Math.atan2(p3.y - p2.y, p3.x - p2.x) - Math.atan2(p1.y - p2.y, p1.x - p2.x);
      let angle = Math.abs((radians * 180.0) / Math.PI);
      if (angle > 180.0) angle = 360 - angle;
      return angle;
    };

    const processPose = (landmarks) => {
      // 왼쪽 다리 각도 계산에 필요한 점들
      const leftHip = landmarks[23];
      const leftKnee = landmarks[25];
      const leftAnkle = landmarks[27];

      // 오른쪽 다리 각도 계산에 필요한 점들
      const rightHip = landmarks[24];
      const rightKnee = landmarks[26];
      const rightAnkle = landmarks[28];

      // 양쪽 무릎 각도 계산
      const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
      const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);

      // 둘 중 더 굽혀진(각도가 작은) 무릎을 기준으로 판단
      const minKneeAngle = Math.min(leftKneeAngle, rightKneeAngle);

      // 런지 동작 감지 (90도 이하로 내려갔다가 160도 이상으로 올라올 때)
      if (!isDown.value && minKneeAngle < 90) {
        isDown.value = true;
        feedback.value = "Down";
      } else if (isDown.value && minKneeAngle > 160) {
        isDown.value = false;
        count.value++;
        feedback.value = `Up! Count: ${count.value}`;
      }
    };

    return {
      count,
      processPose,
      feedback,
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
</style>
