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
      <MediapipeComponent
        @get-Time="getTime"
        @pose-detected="processPose"
        @open-modal="openModal"
        class="z-0"
      />
      <CompleteModal
        v-if="showModal"
        :result="result"
        :count="count"
        class="z-20"
      />
    </div>

    <div class="text-container absolute top-4 right-4">
      <!-- <div class="count text-yellow-500 text-4xl font-dgm bg-white">스쿼트 횟수: {{ count }}</div> -->
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
        몸 전체가 화면에 나오도록
        <br />
        위치를 수정해주세요
      </button>
    </div>
    <button
      v-if="isTutorialMode"
      @click="setCountToThree"
      class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 absolute bottom-4 left-4 z-50"
    >
      3
    </button>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, defineEmits, defineProps } from "vue";
import MediapipeComponent from "@/components/MediapipeComponent.vue";
import CompleteModal from "@/components/modal/CompleteModal.vue";

// 상태 변수 e
const count = ref(0);
const isDown = ref(false);
const feedback = ref("준비중...");
const formFeedback = ref("");
const showGreat = ref(false);
const showModal = ref(false);
const showErrorModal = ref(false); // 에러 모달 상태 변수
const showSpinner = ref(true); // 로딩 스피너 상태 변수
const isTutorialMode = window.location.href.includes("tutorial");
// const isSingleMode = window.location.href.includes("single-mode")

const emit = defineEmits(["setCount", "getTimeLeft"]);
// 모드 리턴
// const getMode = () => {
//   if (isTutorialMode) return "Tutorial Mode"
//   if (isSingleMode) return "Single Mode"
//   return "Unknown Mode"
// }

const props = defineProps(["command"]);
const result = ref("");
watch(
  () => props.command,
  (newCommand) => {
    console.log(newCommand, "명령받음");
    if (newCommand) {
      showModal.value = true;
      result.value = newCommand;
    }
  }
);

const getTime = (value) => {
  console.log();
  emit("getTimeLeft", value);
};

// 필수 랜드마크 정의
const requiredLandmarks = [0, 1, 2, 3, 4, 5, 6, 27, 28, 29, 30, 31, 32];

// 필수 랜드마크 확인 함수
const checkRequiredLandmarks = (landmarks) => {
  return requiredLandmarks.every((index) => {
    const landmark = landmarks[index];
    return (
      landmark &&
      landmark.x >= 0 &&
      landmark.x <= 1 &&
      landmark.y >= 0 &&
      landmark.y <= 1 &&
      landmark.visibility !== undefined &&
      landmark.visibility > 0.5 // 가시성 기준 추가
    );
  });
};

// Complete Modal 열기
const openModal = () => {
  showErrorModal.value = false;
  showModal.value = true;
};

// 각도 계산 함수
const calculateAngle = (a, b, c) => {
  const radians =
    Math.atan2(c.y - b.y, c.x - b.x) - Math.atan2(a.y - b.y, a.x - b.x);
  let degrees = Math.abs((radians * 180.0) / Math.PI);
  if (degrees > 180.0) degrees = 360 - degrees;
  return degrees;
};

// 자세 체크 함수
const checkForm = (landmarks) => {
  const leftHip = landmarks[23];
  const rightHip = landmarks[24];
  const leftKnee = landmarks[25];
  const rightKnee = landmarks[26];
  const leftAnkle = landmarks[27];
  const rightAnkle = landmarks[28];

  const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
  const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);
  let formMessages = [];

  if (leftKneeAngle < 80 || rightKneeAngle < 80) {
    formMessages.push("너무 깊이 내려가지 않도록 조절하세요!");
  } else if (leftKneeAngle > 120 || rightKneeAngle > 120) {
    formMessages.push("스쿼트 자세를 조금 더 깊게 내려가 보세요!");
  }

  if (leftKnee.x > leftAnkle.x || rightKnee.x > rightAnkle.x) {
    formMessages.push("무릎이 발보다 앞에 나가면 안 돼요!");
  }

  return formMessages.join(", ");
};

// 포즈 처리 함수
const processPose = (landmarks) => {
  if (!landmarks || landmarks.length === 0) {
    // isReady.value = false
    feedback.value = "준비중...";
    return;
  }

  // 필수 랜드마크 확인
  if (!checkRequiredLandmarks(landmarks)) {
    showErrorModal.value = true; // 필수 랜드마크가 없으면 모달 표시
  } else {
    showErrorModal.value = false; // 필수 랜드마크가 모두 있으면 모달 숨김
  }

  // isReady.value = true
  if (!showErrorModal.value) {
    formFeedback.value = checkForm(landmarks);

    const leftHip = landmarks[23];
    const rightHip = landmarks[24];
    const leftKnee = landmarks[25];
    const rightKnee = landmarks[26];
    const leftAnkle = landmarks[27];
    const rightAnkle = landmarks[28];

    const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
    const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);
    const avgKneeAngle = (leftKneeAngle + rightKneeAngle) / 2;

    if (isTutorialMode && count.value >= 3) {
      return;
    }

    if (!isDown.value && avgKneeAngle < 100) {
      isDown.value = true;
      feedback.value = "Down";
    } else if (isDown.value && avgKneeAngle > 160) {
      isDown.value = false;
      count.value++;
      emit("setCount", count.value);
      feedback.value = `Up! Count: ${count.value}`;

      showGreat.value = true;
      setTimeout(() => {
        showGreat.value = false;
      }, 1000);

      if (isTutorialMode && count.value === 3) {
        showModal.value = true;
      }
    }
  }
};

// 임시 버튼 클릭 핸들러 함수
const setCountToThree = () => {
  count.value = 3;
  if (isTutorialMode) {
    showModal.value = true; // 튜토리얼 모드일 경우 완료 상태로 전환
  }
};

// 로딩 스피너 타이머 설정
onMounted(() => {
  setTimeout(() => {
    showSpinner.value = false; // 스피너 숨기기
  }, 2000);
});
</script>

<style scoped></style>
