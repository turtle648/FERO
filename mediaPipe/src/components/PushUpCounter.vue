<template>
  <div class="counter-container">
    <div class="count">푸시업 횟수: {{ count }}</div>
    <div class="status">{{ feedback }}</div>
    <div v-if="formFeedback" class="form-feedback">{{ formFeedback }}</div>
    <WebCamera @pose-detected="processPose" />
  </div>
</template>

<script>
import { ref } from "vue";
import WebCamera from "./WebCamera.vue";

export default {
  name: "PushUpCounter",
  components: {
    WebCamera,
  },
  setup() {
    const count = ref(0);
    const isDown = ref(false);
    const feedback = ref("준비중...");
    const formFeedback = ref(""); // 자세 피드백용

    // 두 점 사이의 거리 계산
    const calculateDistance = (point1, point2) => {
      return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    };

    // 팔꿈치-어깨-몸통 각도 계산
    const calculateBodyAngle = (elbow, shoulder, hip) => {
      const angle =
        Math.atan2(hip.y - shoulder.y, hip.x - shoulder.x) -
        Math.atan2(elbow.y - shoulder.y, elbow.x - shoulder.x);
      let degrees = Math.abs((angle * 180.0) / Math.PI);
      if (degrees > 180.0) degrees = 360 - degrees;
      return degrees;
    };

    // 손목-팔꿈치-어깨 각도 계산
    const calculateArmAngle = (wrist, elbow, shoulder) => {
      const angle =
        Math.atan2(shoulder.y - elbow.y, shoulder.x - elbow.x) -
        Math.atan2(wrist.y - elbow.y, wrist.x - elbow.x);
      let degrees = Math.abs((angle * 180.0) / Math.PI);
      if (degrees > 180.0) degrees = 360 - degrees;
      return degrees;
    };

    // 세 점이 일직선인지 확인하는 함수 추가
    const isAligned = (point1, point2, point3, tolerance = 0.1) => {
      // 두 선분의 각도 계산
      const angle =
        Math.atan2(point3.y - point2.y, point3.x - point2.x) -
        Math.atan2(point1.y - point2.y, point1.x - point2.x);
      let degrees = Math.abs((angle * 180.0) / Math.PI);
      if (degrees > 180.0) degrees = 360 - degrees;

      // 180도에 가까운지 확인 (tolerance 값으로 오차 허용)
      return Math.abs(180 - degrees) <= tolerance * 180;
    };

    const checkForm = (landmarks) => {
      const leftWrist = landmarks[15];
      const rightWrist = landmarks[16];
      const leftShoulder = landmarks[11];
      const rightShoulder = landmarks[12];
      const leftElbow = landmarks[13];
      const rightElbow = landmarks[14];
      const leftHip = landmarks[23];
      const rightHip = landmarks[24];

      let formMessages = [];

      // 1. 손 간격 체크 (권장사항)
      const shoulderWidth = calculateDistance(leftShoulder, rightShoulder);
      const handWidth = calculateDistance(leftWrist, rightWrist);
      if (handWidth < shoulderWidth * 1.1) {
        formMessages.push("팁: 손 간격을 조금 더 넓히면 좋습니다");
      }

      // 2. 팔꿈치-어깨-몸통 각도 체크 (권장사항)
      const leftBodyAngle = calculateBodyAngle(leftElbow, leftShoulder, leftHip);
      const rightBodyAngle = calculateBodyAngle(rightElbow, rightShoulder, rightHip);

      if (leftBodyAngle < 40 || leftBodyAngle > 50 || rightBodyAngle < 40 || rightBodyAngle > 50) {
        formMessages.push("팁: 팔꿈치-어깨-몸통 각도를 45도로 맞추면 더 좋습니다");
      }

      return formMessages.join(", ");
    };

    const processPose = (landmarks) => {
      // 자세 피드백 업데이트 (카운팅과 무관)
      formFeedback.value = checkForm(landmarks);

      // 몸이 일자인지 확인
      const leftShoulder = landmarks[11];
      const rightShoulder = landmarks[12];
      const leftHip = landmarks[23];
      const rightHip = landmarks[24];
      const leftKnee = landmarks[25];
      const rightKnee = landmarks[26];

      // 양쪽 어깨, 엉덩이, 무릎의 중간점 계산
      const shoulderMid = {
        x: (leftShoulder.x + rightShoulder.x) / 2,
        y: (leftShoulder.y + rightShoulder.y) / 2,
      };
      const hipMid = {
        x: (leftHip.x + rightHip.x) / 2,
        y: (leftHip.y + rightHip.y) / 2,
      };
      const kneeMid = {
        x: (leftKnee.x + rightKnee.x) / 2,
        y: (leftKnee.y + rightKnee.y) / 2,
      };

      // 몸이 일자인지 확인
      const isBodyStraight = isAligned(shoulderMid, hipMid, kneeMid);

      // 팔 각도 계산 (카운팅 기준)
      const leftArmAngle = calculateArmAngle(landmarks[15], landmarks[13], landmarks[11]);
      const rightArmAngle = calculateArmAngle(landmarks[16], landmarks[14], landmarks[12]);
      const avgArmAngle = (leftArmAngle + rightArmAngle) / 2;

      // 몸이 일자가 아니면 피드백 제공
      if (!isBodyStraight) {
        feedback.value = "몸을 일자로 유지해주세요";
        return;
      }

      // 팔 굽힘/폄 상태 감지 (몸이 일자일 때만 카운팅)
      if (!isDown.value && avgArmAngle >= 85 && avgArmAngle <= 95) {
        isDown.value = true;
        feedback.value = "Down";
      } else if (isDown.value && avgArmAngle > 160) {
        isDown.value = false;
        count.value++;
        feedback.value = `Up! Count: ${count.value}`;
      }
    };

    return {
      count,
      processPose,
      feedback,
      formFeedback,
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
</style>
