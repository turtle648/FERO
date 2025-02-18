<template>
  <div class="video-container">
    <canvas ref="canvasElement">
      <video ref="videoElement" autoplay playsinline muted />
    </canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from "vue";
import { defineEmits, defineProps } from "vue";
import { Pose } from "@mediapipe/pose";

const props = defineProps(["peerStream"]);

const emit = defineEmits(["pose-detected", "open-modal", "getTime"]);

const videoElement = ref(null);
const canvasElement = ref(null);
let pose = null;
let animationFrameId = null;

// 비디오 스트림 설정 및 처리 시작
const setupVideo = async (stream) => {
  if (!videoElement.value) {
    console.error("videoElement가 초기화되지 않음");
    return;
  }

  console.log("비디오 스트림 설정 시작", stream);
  videoElement.value.srcObject = stream;

  try {
    await videoElement.value.play();
    console.log("비디오 재생 성공");
  } catch (error) {
    console.error("비디오 재생 오류:", error);
  }
};

// 스트림 변경 감지
watch(
  () => props.peerStream,
  async (newStream) => {
    console.log(newStream);

    if (newStream) {
      await setupVideo(newStream);
    }
  },
  { immediate: true }
);

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

    const landmarks = results.poseLandmarks;
    const nose = landmarks[0];
    const leftEar = landmarks[7];
    const rightEar = landmarks[8];
    const leftShoulder = landmarks[11];
    const rightShoulder = landmarks[12];
    const emoji = "🤓";

    if (nose && leftEar && rightEar && leftShoulder && rightShoulder) {
      const faceX =
        ((nose.x + leftEar.x + rightEar.x) / 3) * canvasElement.value.width;
      const faceY =
        ((nose.y + leftEar.y + rightEar.y) / 3) * canvasElement.value.height;
      const faceWidth =
        Math.abs(leftEar.x - rightEar.x) * 2.5 * canvasElement.value.width;
      const faceHeight =
        Math.abs(nose.y - (leftShoulder.y + rightShoulder.y) / 2) *
        2.5 *
        canvasElement.value.height;

      // 블러 처리
      const offscreenCanvas = document.createElement("canvas");
      offscreenCanvas.width = faceWidth;
      offscreenCanvas.height = faceHeight;
      const offscreenCtx = offscreenCanvas.getContext("2d");

      offscreenCtx.drawImage(
        results.image,
        faceX - faceWidth / 2,
        faceY - faceHeight / 2,
        faceWidth,
        faceHeight,
        0,
        0,
        faceWidth,
        faceHeight
      );

      offscreenCtx.filter = "blur(40px)";
      offscreenCtx.drawImage(offscreenCanvas, 0, 0);

      canvasCtx.drawImage(
        offscreenCanvas,
        faceX - faceWidth / 2,
        faceY - faceHeight / 2,
        faceWidth,
        faceHeight
      );

      // 이모지 그리기
      const earDistance =
        Math.abs(leftEar.x - rightEar.x) * canvasElement.value.width;
      const emojiSize = earDistance * 2;

      canvasCtx.font = `${emojiSize}px sans-serif`;
      canvasCtx.textAlign = "center";
      canvasCtx.textBaseline = "middle";
      canvasCtx.fillText(emoji, faceX, faceY);
    }
  }

  canvasCtx.restore();
};

const processFrame = async () => {
  if (pose && videoElement.value && videoElement.value.readyState === 4) {
    await pose.send({ image: videoElement.value });
  }
  animationFrameId = requestAnimationFrame(processFrame);
};

onMounted(async () => {
  console.log("컴포넌트가 마운트되었습니다");

  if (!videoElement.value || !canvasElement.value) {
    console.error("Video or Canvas element is not initialized.");
    return;
  }

  canvasElement.value.width = window.innerWidth;
  canvasElement.value.height = window.innerHeight;

  // Pose 초기화
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

  // 비디오 메타데이터 로드 완료 시 프레임 처리 시작
  videoElement.value.addEventListener("loadedmetadata", () => {
    processFrame();
  });

  // 초기 스트림 설정
  if (props.peerStream) {
    await setupVideo(props.peerStream);
  }
});

onUnmounted(() => {
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId);
  }
  if (pose) {
    pose.close();
  }
  if (videoElement.value) {
    videoElement.value.srcObject = null;
  }
});
</script>

<style scoped>
.video-container {
  width: 100%;
  height: 100%;
  position: relative;
}

canvas {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

video {
  display: none;
}

.hidden {
  z-index: -1;
}
</style>
