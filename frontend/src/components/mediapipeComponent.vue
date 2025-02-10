<template>
  <div class="container flex flex-col items-center justify-between p-4 h-screen w-screen">
    <div class="flex justify-between w-full">
      <div class="counter text-white-common z-10">(갯수)</div>
      <div class="timer text-white-common z-10">{{ formattedTime }}</div>

      <!-- fix: 시간 선택은 앞에서 넘어와야함 -->
      <select class="z-10" v-model="selectedTime" @change="resetTimer">
        <option :value="1 * 60 * 1000">1분</option>
        <option :value="2 * 60 * 1000">2분</option>
        <option :value="5 * 60 * 1000">5분</option>
      </select>
      <button class="text-white-common z-10" @click="startTimer">시작</button>
    </div>

    <!-- 본인 화면 -->
    <video ref="videoElement" class="absolute inset-0 w-full h-full object-cover z-0"></video>
    <canvas ref="canvasElement" class="absolute inset-0 w-full h-full z-0"></canvas>

    <!-- 하단 버튼 -->
    <div class="flex justify-between items-center w-full mt-4 z-10">
      <ExitButton class="px-4 py-2 rounded mx-auto" @click="stopCameraAndNavigate" />
      <ReportIssueButton />
    </div>
  </div>
  <!-- <ExitConfirmationModal v-if="showExitModal" @closeExit="closeExitModal" @openExit="openExitModal" />
  <ReportModal v-if="showReportModal" /> -->
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

// 버튼
import ExitButton from "@/assets/button/ExitButton.vue"
import ReportIssueButton from "@/assets/button/ReportIssueButton.vue"

let intervalId = null // setInterval ID 저장 (타이머 초기화용)

const selectedTime = ref(1 * 60 * 1000) // 기본값: 1분
const timeLeft = ref(selectedTime.value) // 남은 시간 (ms)
const formattedTime = ref(formatTime(timeLeft.value)) // 표시할 시간

// 시간을 포맷팅하는 함수 (분:초 형식)
function formatTime(time) {
  const minutes = Math.floor(time / 60000)
  const seconds = Math.floor((time % 60000) / 1000)
  return `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`
}

// 타이머 시작 함수
function startTimer() {
  clearInterval(intervalId) // 기존 타이머 초기화
  timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
  formattedTime.value = formatTime(timeLeft.value)

  intervalId = setInterval(() => {
    timeLeft.value -= 1000 // 매초마다 시간 감소
    formattedTime.value = formatTime(timeLeft.value)

    if (timeLeft.value <= 0) {
      clearInterval(intervalId) // 타이머 종료
      formattedTime.value = "00:00"
    }
  }, 1000)
}

// 타이머 리셋 함수 (시간 변경 시 호출)
function resetTimer() {
  clearInterval(intervalId) // 기존 타이머 초기화
  timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
  formattedTime.value = formatTime(timeLeft.value)
}

// 미디어파이프 관련
import { Camera } from "@mediapipe/camera_utils"
import { Pose, POSE_CONNECTIONS } from "@mediapipe/pose"
import { drawConnectors, drawLandmarks } from "@mediapipe/drawing_utils"

const videoElement = ref(null)
const canvasElement = ref(null)
let camera = null
let pose = null

// const router = useRouter()

const onResults = (results) => {
  if (!canvasElement.value) return

  const canvasCtx = canvasElement.value.getContext("2d")
  canvasCtx.save()
  canvasCtx.clearRect(0, 0, canvasElement.value.width, canvasElement.value.height)

  // 좌우 반전 적용
  canvasCtx.translate(canvasElement.value.width, 0)
  canvasCtx.scale(-1, 1)

  canvasCtx.drawImage(results.image, 0, 0, canvasElement.value.width, canvasElement.value.height)

  if (results.poseLandmarks) {
    drawConnectors(canvasCtx, results.poseLandmarks, POSE_CONNECTIONS, {
      color: "#00FF00",
      lineWidth: 4,
    })
    drawLandmarks(canvasCtx, results.poseLandmarks, { color: "#FF0000", lineWidth: 2 })
  }

  canvasCtx.restore()
}

onMounted(async () => {
  if (!videoElement.value || !canvasElement.value) {
    console.error("Video or Canvas element is not initialized.")
    return
  }

  // Canvas 크기 설정
  canvasElement.value.width = window.innerWidth
  canvasElement.value.height = window.innerHeight

  pose = new Pose({
    locateFile: (file) => `https://cdn.jsdelivr.net/npm/@mediapipe/pose/${file}`,
  })

  pose.setOptions({
    modelComplexity: 1,
    smoothLandmarks: true,
    minDetectionConfidence: 0.5,
    minTrackingConfidence: 0.5,
  })

  pose.onResults(onResults)

  camera = new Camera(videoElement.value, {
    onFrame: async () => {
      if (pose && videoElement.value) {
        await pose.send({ image: videoElement.value })
      }
    },
    width: window.innerWidth,
    height: window.innerHeight,
  })

  try {
    await camera.start()
  } catch (error) {
    console.error("카메라 시작 오류:", error)
  }
})

// 종료 버튼 클릭 시
function stopCameraAndNavigate() {
  if (camera) {
    camera.stop() // 카메라 스트림 중지
  }

  router.push("/main") // /main으로 이동
}

onUnmounted(() => {
  if (camera) camera.stop()
})
</script>

<style scoped>
.text-white-common {
  @apply text-white;
}

/* .container {
  height: 100%;
  width: 100%;
} */

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
