<template>
  <div class="camera-container">
    <video ref="videoElement" class="input-video"></video>
    <canvas ref="canvasElement" class="output-canvas"></canvas>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";
import { Camera } from "@mediapipe/camera_utils";
import { Pose, POSE_CONNECTIONS } from "@mediapipe/pose"; // POSE_CONNECTIONS 추가
import { drawConnectors, drawLandmarks } from "@mediapipe/drawing_utils";

export default {
  name: "WebCamera",
  setup(props, { emit }) {
    const videoElement = ref(null);
    const canvasElement = ref(null);
    let camera = null;
    let pose = null;

    const onResults = (results) => {
      if (!canvasElement.value) return;

      const canvasCtx = canvasElement.value.getContext("2d");
      canvasCtx.save();
      canvasCtx.clearRect(0, 0, canvasElement.value.width, canvasElement.value.height);

      // 좌우 반전 적용
      canvasCtx.translate(canvasElement.value.width, 0);
      canvasCtx.scale(-1, 1);

      canvasCtx.drawImage(
        results.image,
        0,
        0,
        canvasElement.value.width,
        canvasElement.value.height
      );

      if (results.poseLandmarks) {
        drawConnectors(canvasCtx, results.poseLandmarks, POSE_CONNECTIONS, {
          color: "#00FF00",
          lineWidth: 4,
        });
        drawLandmarks(canvasCtx, results.poseLandmarks, { color: "#FF0000", lineWidth: 2 });

        emit("pose-detected", results.poseLandmarks);
      }

      canvasCtx.restore();
    };

    onMounted(async () => {
      if (!videoElement.value || !canvasElement.value) return;

      // Canvas 크기 설정
      canvasElement.value.width = 640;
      canvasElement.value.height = 480;

      pose = new Pose({
        locateFile: (file) => {
          return `https://cdn.jsdelivr.net/npm/@mediapipe/pose/${file}`;
        },
      });

      pose.setOptions({
        modelComplexity: 1,
        smoothLandmarks: true,
        minDetectionConfidence: 0.5,
        minTrackingConfidence: 0.5,
      });

      pose.onResults(onResults);

      camera = new Camera(videoElement.value, {
        onFrame: async () => {
          if (pose) {
            await pose.send({ image: videoElement.value });
          }
        },
        width: 640,
        height: 480,
      });

      try {
        await camera.start();
      } catch (error) {
        console.error("카메라 시작 오류:", error);
      }
    });

    onUnmounted(() => {
      if (camera) {
        camera.stop();
      }
      if (pose) {
        pose.close();
      }
    });

    return {
      videoElement,
      canvasElement,
    };
  },
};
</script>

<style scoped>
.camera-container {
  position: relative;
  width: 640px;
  height: 480px;
}

.input-video {
  position: absolute;
  visibility: hidden;
}

.output-canvas {
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>
