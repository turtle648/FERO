<template>
  <div class="fixed inset-0 overflow-hidden">
    <!-- 중앙 영역 (카운트다운) -->
    <div class="absolute inset-0 flex items-center justify-center z-20">
      <div v-if="countdown > 0" class="text-5xl">{{ countdown }}</div>
      <div v-else-if="showStartText" class="text-5xl">START!</div>
    </div>

    <!-- 상단 영역 (타이머) -->
    <div class="timer-container absolute top-4 left-4 z-20 text-2xl nes-btn">
      {{ formattedTime }}
    </div>

    <!-- 비디오 영역 -->
    <div class="video-container fixed inset-0 overflow-hidden">
      <canvas ref="canvasElement" class="absolute left-1/2 -translate-x-1/2">
        <video ref="videoElement" muted playsinline class="object-cover" />
      </canvas>
    </div>

    <!-- ver.1 -->
    <!-- <div class="video-container fixed inset-0 overflow-hidden">
      <canvas ref="canvasElement" width="300" height="150" class="h-screen w-auto object-contain absolute left-1/2 -translate-x-1/2">
        <video ref="videoElement" class="object-cover" muted />
      </canvas>
    </div> -->

    <!-- 옛날꺼 -->
    <!-- <div class="absolute inset-0 z-10">
      <canvas ref="canvasElement" class="h-full w-full object-cover">
        <video ref="videoElement" muted class="object-cover" />
      </canvas>
    </div> -->

    <!-- 버튼 영역 -->
    <div
      class="absolute bottom-0 inset-x-0 p-4 flex justify-between items-center z-20"
    >
      <div class="flex-1">
        <!-- 왼쪽 여백 -->
      </div>
      <div class="flex-1 flex justify-center">
        <ExitButton @click="stopCameraAndNavigate" />
      </div>
      <div class="flex-1 flex justify-end">
        <ReportIssueButton />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Camera } from "@mediapipe/camera_utils";
import { Pose } from "@mediapipe/pose";
import ExitButton from "@/components/button/ExitButton.vue";
import ReportIssueButton from "@/components/button/ReportButton.vue";

const props = defineProps(["isTimerStart"]);
const emit = defineEmits(["pose-detected", "open-modal", "getTime"]);
const route = useRoute();
const router = useRouter();

// 타이머 관련
let intervalId = null;
const selectedTime = ref(1 * 60 * 1000);
const timeLeft = ref(selectedTime.value);
const formattedTime = ref(formatTime(timeLeft.value));

watch(
  () => props.isTimerStart,
  (newTimerStart) => {
    console.log("소켓 연결 후 타이머 시작" + newTimerStart);

    if (newTimerStart) {
      startCountdown();
    }
  },
  { deep: true, immediate: false }
);

function formatTime(time) {
  const minutes = Math.floor(time / 60000);
  const seconds = Math.floor((time % 60000) / 1000);
  return `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(
    2,
    "0"
  )}`;
}

function startTimer() {
  clearInterval(intervalId);
  timeLeft.value = selectedTime.value;
  formattedTime.value = formatTime(timeLeft.value);

  intervalId = setInterval(() => {
    timeLeft.value -= 1000;
    formattedTime.value = formatTime(timeLeft.value);
    emit("getTime", timeLeft.value);

    if (timeLeft.value <= 0) {
      clearInterval(intervalId);
      formattedTime.value = "00:00";
      camera.stop();
      emit("open-modal");
    }
  }, 1000);
}

// 카운트다운 관련
const countdown = ref(4);
const showStartText = ref(false);

function startCountdown() {
  countdown.value = 4;
  showStartText.value = false;

  const countdownInterval = setInterval(() => {
    if (countdown.value > 1) {
      countdown.value--;
    } else {
      clearInterval(countdownInterval);
      countdown.value = null;
      showStartText.value = true;

      setTimeout(() => {
        showStartText.value = false;
        startTimer();
      }, 2000);
    }
  }, 1000);
}

// 미디어파이프 관련
const videoElement = ref(null);
const canvasElement = ref(null);
let camera = null;
let pose = null;

const applyFullscreenBlur = (canvasCtx, results) => {
  // 오프스크린 캔버스 생성
  const offscreenCanvas = document.createElement("canvas");
  offscreenCanvas.width = canvasElement.value.width;
  offscreenCanvas.height = canvasElement.value.height;
  const offscreenCtx = offscreenCanvas.getContext("2d");

  // 전체 화면 블러 처리
  offscreenCtx.drawImage(
    results.image,
    0,
    0,
    offscreenCanvas.width,
    offscreenCanvas.height
  );
  offscreenCtx.filter = "blur(20px)";
  offscreenCtx.drawImage(offscreenCanvas, 0, 0);

  // 메인 캔버스에 합성
  canvasCtx.drawImage(offscreenCanvas, 0, 0);
};

const drawEmoji = (canvasCtx, landmarks) => {
  const emoji = "😎";
  const nose = landmarks[0];
  const leftEar = landmarks[7];
  const rightEar = landmarks[8];

  if (nose && leftEar && rightEar) {
    const faceX =
      ((nose.x + leftEar.x + rightEar.x) / 3) * canvasElement.value.width;
    const faceY =
      ((nose.y + leftEar.y + rightEar.y) / 3) * canvasElement.value.height;

    // 1. 귀 간 거리 기반 기본 크기 계산
    const earDistance =
      Math.abs(leftEar.x - rightEar.x) * canvasElement.value.width;
    let emojiSize = earDistance * 2;

    // 2. 최소 크기 설정 (화면 세로의 1/5)
    const minSize = window.innerHeight / 5;
    emojiSize = Math.max(emojiSize, minSize); // [1]

    canvasCtx.font = `${emojiSize}px sans-serif`;
    canvasCtx.textAlign = "center";
    canvasCtx.textBaseline = "middle";
    canvasCtx.fillText(emoji, faceX, faceY);
  }
};

const onResults = (results) => {
  if (!canvasElement.value) return;

  const canvasCtx = canvasElement.value.getContext("2d");
  canvasCtx.save();
  canvasCtx.clearRect(
    0,
    0,
    canvasElement.value.width,
    canvasElement.value.height
  );

  // 좌우 반전 적용
  canvasCtx.translate(canvasElement.value.width, 0);
  canvasCtx.scale(-1, 1);

  // 원본 이미지 그리기
  canvasCtx.drawImage(
    results.image,
    0,
    0,
    canvasElement.value.width,
    canvasElement.value.height
  );

  if (results.poseLandmarks) {
    emit("pose-detected", results.poseLandmarks);
    drawEmoji(canvasCtx, results.poseLandmarks);
  }

  if (results.poseLandmarks) {
    emit("pose-detected", results.poseLandmarks);
    const landmarks = results.poseLandmarks;

    // 얼굴 인식 여부 확인
    const nose = landmarks[0];
    const leftEar = landmarks[7];
    const rightEar = landmarks[8];

    if (nose && leftEar && rightEar) {
      drawEmoji(canvasCtx, landmarks);
    } else {
      applyFullscreenBlur(canvasCtx, results);
    }
  } else {
    applyFullscreenBlur(canvasCtx, results);
  }
  canvasCtx.restore();
};

onMounted(async () => {
  if (window.location.href.includes("single-mode")) {
    const pathSegments = route.path.split("/").filter(Boolean);
    const timeFromUrl = parseInt(pathSegments[pathSegments.length - 1]);
    if (!isNaN(timeFromUrl)) {
      selectedTime.value = timeFromUrl * 60 * 1000;
    }
  } else if (window.location.href.includes("tutorial")) {
    selectedTime.value = 999 * 60 * 1000;
  }

  if (!videoElement.value || !canvasElement.value) {
    console.error("Video or Canvas element is not initialized.");
    return;
  }

  const canvas = canvasElement.value;
  const video = videoElement.value;

  pose = new Pose({
    locateFile: (file) =>
      `https://cdn.jsdelivr.net/npm/@mediapipe/pose/${file}`,
  });

  pose.setOptions({
    modelComplexity: 1,
    smoothLandmarks: true,
    minDetectionConfidence: 0.5,
    minTrackingConfidence: 0.5,
  });

  pose.onResults(onResults);

  video.addEventListener(
    "canplay",
    () => {
      canvas.height = window.innerHeight;
      const aspectRatio = video.videoWidth / video.videoHeight;
      canvas.width = canvas.height * aspectRatio;
    },
    { once: true }
  );

  camera = new Camera(videoElement.value, {
    onFrame: async () => {
      if (pose && videoElement.value) {
        await pose.send({ image: videoElement.value });
      }
    },
    width: window.innerWidth,
    height: window.innerHeight,
  });

  try {
    await camera.start();
    // if (props.isTimerStart) {
    //   startCountdown();
    // }
  } catch (error) {
    console.error("카메라 시작 오류:", error);
  }
});

function stopCameraAndNavigate() {
  if (camera) camera.stop();
  router.push({ name: "Main" });
}

onUnmounted(() => {
  if (camera) camera.stop();
});
</script>

<style scoped></style>
