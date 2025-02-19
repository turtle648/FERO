<template>
  <div class="squat-container w-screen h-screen">
    <div
      v-if="showSpinner"
      class="loading-spinner flex items-center justify-center h-screen"
    >
      <div
        class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500 z-50"
      ></div>
    </div>

    <div class="media-container">
      <MediapipeComponent @pose-detected="processPose" class="z-0" />
    </div>

    <div class="text-container absolute top-4 right-4">
      <button type="button" class="nes-btn is-primary text-1xl font-dgm">
        스쿼트 횟수: {{ count }}
      </button>
      <div v-if="showGreat" class="great-message text-red text-3xl">Great!</div>
    </div>

    <div
      v-if="showErrorModal"
      class="landmark-error-modal absolute inset-x-0 top-[30%] transform flex items-center justify-center text-yellow-500 text-xl z-20 font-dgm"
    >
      <button type="button" class="nes-btn is-warning">
        몸 전체가 화면에 나오도록 위치를 수정해주세요
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import MediapipeComponent from "@/components/MediapipeComponent.vue";

// ✅ 상태 변수
const count = ref(0);
const state = ref("up"); // "up", "down", "middle"
const feedback = ref("준비중...");
const showGreat = ref(false);
const showErrorModal = ref(false);
const showSpinner = ref(true);
const handTouchedKnee = ref(false); // 손을 무릎에 댔는지 확인

// ✅ 유클리드 거리 계산 함수
const calculateDistance = (p1, p2) => {
  return Math.sqrt((p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2);
};

// ✅ 각도 계산 함수
const calculateAngle = (a, b, c) => {
  const radians =
    Math.atan2(c.y - b.y, c.x - b.x) - Math.atan2(a.y - b.y, a.x - b.x);
  let degrees = Math.abs((radians * 180.0) / Math.PI);
  if (degrees > 180.0) degrees = 360 - degrees;
  return degrees;
};

// ✅ 필수 랜드마크 확인 함수
const requiredLandmarks = [11, 12, 23, 24, 25, 26, 27, 28, 15, 16];

const checkRequiredLandmarks = (landmarks) => {
  return requiredLandmarks.every((index) => {
    const landmark = landmarks[index];
    return landmark && landmark.visibility > 0.5;
  });
};

// ✅ 포즈 감지 및 스쿼트 카운팅 로직
const processPose = (landmarks) => {
  if (!landmarks || landmarks.length === 0) {
    feedback.value = "준비중...";
    return;
  }

  if (!checkRequiredLandmarks(landmarks)) {
    showErrorModal.value = true;
    return;
  } else {
    showErrorModal.value = false;
  }

  const leftHip = landmarks[23];
  const rightHip = landmarks[24];
  const leftKnee = landmarks[25];
  const rightKnee = landmarks[26];
  const leftAnkle = landmarks[27];
  const rightAnkle = landmarks[28];
  const leftHand = landmarks[15];
  const rightHand = landmarks[16];

  // ✅ 무릎 각도 계산
  const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
  const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);
  const avgKneeAngle = (leftKneeAngle + rightKneeAngle) / 2;

  // ✅ 손과 무릎 거리 계산
  const leftHandKneeDist = calculateDistance(leftHand, leftKnee);
  const rightHandKneeDist = calculateDistance(rightHand, rightKnee);

  // ✅ down 또는 middle 상태에서 손이 무릎에 가까우면 카운트 방지
  if (
    state.value !== "up" &&
    (leftHandKneeDist <= 0.1 || rightHandKneeDist <= 0.1)
  ) {
    handTouchedKnee.value = true;
  }

  // ✅ 스쿼트 상태 변경 (down 판정 완화)
  if (state.value === "up" && avgKneeAngle < 115) {
    // 기존 100 → 115로 완화
    state.value = "down";
    feedback.value = "Down";
  } else if (
    state.value === "down" &&
    avgKneeAngle >= 125 && // 기존 120 → 125로 완화
    avgKneeAngle < 165
  ) {
    state.value = "middle";
    feedback.value = "Middle";
  } else if (state.value === "middle" && avgKneeAngle >= 165) {
    state.value = "up";

    if (!handTouchedKnee.value) {
      count.value++;
      feedback.value = `Up! Count: ${count.value}`;
      showGreat.value = true;
      setTimeout(() => {
        showGreat.value = false;
      }, 1000);
    } else {
      feedback.value = "손을 무릎에 대고 있어서 카운팅되지 않음!";
    }

    // ✅ up 상태로 복귀하면 손 닿음 상태 초기화
    handTouchedKnee.value = false;
  }
};

// ✅ 로딩 스피너 설정
onMounted(() => {
  setTimeout(() => {
    showSpinner.value = false;
  }, 2000);
});
</script>

<style scoped></style>
