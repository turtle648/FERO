<template>
  <div class="container flex flex-col items-center justify-between p-4 h-fulll w-full">
    <div class="flex justify-between w-full">
      <div class="timer text-white-common z-20 absolute top-0 right-10">{{ formattedTime }}</div>
    </div>

    <!-- 중앙 영역 -->
    <div v-if="countdown > 0" class="countdown text-4xl text-white z-10">{{ countdown }}</div>
    <div v-else-if="showStartText" class="start-text text-4xl text-white z-10">START</div>

    <!-- 본인 화면 -->
    <!-- 기존 코드 -->
    <!-- <div class="video-container relative h-full">
      <canvas ref="canvasElement" class="h-screen">
        <video ref="videoElement" autoplay playsinline muted class="h-full object-cover overflow-hidden aspect-[9/16]"></video>
      </canvas>
    </div> -->

    <!-- 수정 코드 -->
    <!-- <div class="video-container relative h-full max-w-[800px] max-h-full">
      <canvas ref="canvasElement" class="h-screen">
        <video ref="videoElement" autoplay playsinline muted class="h-full w-full overflow-hidden"></video>
      </canvas>
    </div> -->

    <!-- 수정 코드2 -->
    <div class="video-container relative h-full overflow-hidden mx-auto">
      <canvas ref="canvasElement" class="top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
        <video ref="videoElement" autoplay playsinline muted class="h-full w-full overflow-hidden"></video>
      </canvas>
    </div>

    <!-- 하단 버튼 -->
    <div class="absolute bottom-[2vh] button-container z-10">
      <div class="flex justify-between items-center w-full mt-4 z-10">
        <ExitButton class="px-4 py-2 rounded mx-auto" @click="stopCameraAndNavigate" />
        <ReportIssueButton />
      </div>
    </div>
    <!-- <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex justify-between items-center w-[90%] max-w-md z-10">
      <ExitButton class="rounded bg-red-500 text-white" @click="stopCameraAndNavigate" />
      <ReportIssueButton class="rounded bg-blue-500 text-white" />
    </div> -->
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import { defineEmits } from "vue"

const emit = defineEmits(["pose-detected", "open-modal"])
const route = useRoute()
const router = useRouter()

// const isLoading = ref(true)

// 버튼
import ExitButton from "@/components/button/ExitButton.vue"
import ReportIssueButton from "@/components/button/ReportIssueButton.vue"

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
  // if (isLoading.value) return

  clearInterval(intervalId) // 기존 타이머 초기화
  timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
  formattedTime.value = formatTime(timeLeft.value)

  intervalId = setInterval(() => {
    timeLeft.value -= 1000 // 매초마다 시간 감소
    formattedTime.value = formatTime(timeLeft.value)

    if (timeLeft.value <= 0) {
      clearInterval(intervalId) // 타이머 종료
      formattedTime.value = "00:00"
      camera.stop()
      emit("open-modal")
    }
  }, 1000)
}

// 타이머 리셋 함수 (시간 변경 시 호출)
// function resetTimer() {
//   clearInterval(intervalId) // 기존 타이머 초기화
//   timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
//   formattedTime.value = formatTime(timeLeft.value)
// }

// 카운트다운
const countdown = ref(4)
const showStartText = ref(false)

// 카운트다운 시작 함수
function startCountdown() {
  // if (isLoading.value) return

  countdown.value = 4 // 카운트다운 초기화
  showStartText.value = false // 'START' 숨김

  const countdownInterval = setInterval(() => {
    if (countdown.value > 1) {
      countdown.value-- // 카운트다운 감소
    } else {
      clearInterval(countdownInterval) // 카운트다운 종료
      countdown.value = null // 숫자 숨김
      showStartText.value = true // 'START' 표시

      setTimeout(() => {
        showStartText.value = false // 'START' 숨김 (2초 후)
        startTimer() // 타이머 자동 시작
      }, 2000)
    }
  }, 1000)
}

// 미디어파이프 관련
import { Camera } from "@mediapipe/camera_utils"
import { Pose } from "@mediapipe/pose"
// import { Pose, POSE_CONNECTIONS } from "@mediapipe/pose"
// import { drawConnectors, drawLandmarks } from "@mediapipe/drawing_utils"

const videoElement = ref(null)
const canvasElement = ref(null)
let camera = null
let pose = null

// 얼굴 블러처리 기본 코드 가이드라인만 추가함함
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
    emit("pose-detected", results.poseLandmarks)

    const landmarks = results.poseLandmarks
    const nose = landmarks[0] // 코
    const leftEye = landmarks[2] // 왼쪽 눈
    const rightEye = landmarks[5] // 오른쪽 눈
    // drawConnectors(canvasCtx, results.poseLandmarks, POSE_CONNECTIONS, {
    //   color: "#00FF00",
    //   lineWidth: 4,
    // })
    // drawLandmarks(canvasCtx, results.poseLandmarks, { color: "#FF0000", lineWidth: 2 })
    if (nose && leftEye && rightEye) {
      const faceX = ((nose.x + leftEye.x + rightEye.x) / 3) * canvasElement.value.width
      const faceY = ((nose.y + leftEye.y + rightEye.y) / 3) * canvasElement.value.height
      const faceWidth = Math.abs(leftEye.x - rightEye.x) * 2 * canvasElement.value.width
      const faceHeight = faceWidth * 1.2 // 비율상 얼굴 높이 설정

      // 얼굴 영역을 따로 복사
      const faceImage = canvasCtx.getImageData(faceX - faceWidth / 2, faceY - faceHeight / 2, faceWidth, faceHeight)
      const offscreenCanvas = document.createElement("canvas")
      offscreenCanvas.width = faceWidth
      offscreenCanvas.height = faceHeight
      const offscreenCtx = offscreenCanvas.getContext("2d")
      offscreenCtx.putImageData(faceImage, 0, 0)

      // 블러 필터 적용
      offscreenCtx.filter = "blur(10px)"
      offscreenCtx.drawImage(offscreenCanvas, 0, 0)

      // 다시 원래 위치에 그리기
      canvasCtx.drawImage(offscreenCanvas, faceX - faceWidth / 2, faceY - faceHeight / 2, faceWidth, faceHeight)
    }
  }

  canvasCtx.restore()
}

onMounted(async () => {
  // setTimeout(() => {
  //   isLoading.value = false
  // }, 2000)

  // Single Mode의 경우 시간을 URL BASE로 설정
  if (window.location.href.includes("single-mode")) {
    // 시작 시간 설정 by url prams
    const pathSegments = route.path.split("/").filter(Boolean) // URL을 '/' 기준으로 분할하고, 빈 요소(마지막 `/`) 제거
    const timeFromUrl = parseInt(pathSegments[pathSegments.length - 1]) // 마지막 값 가져오기
    console.log(timeFromUrl, "인지된 시간")
    if (!isNaN(timeFromUrl)) {
      selectedTime.value = timeFromUrl * 60 * 1000 // 초에서 밀리초 변환
    }
  } else if (window.location.href.includes("tutorial")) {
    // 튜토리얼 모드 기본값: 999분
    selectedTime.value = 999 * 60 * 1000
  }

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
    startCountdown() // 카운트다운 시작
  } catch (error) {
    console.error("카메라 시작 오류:", error)
  }
})

// 로딩 상태가 변경되었을 때 동작 추가
// watch(isLoading, async (newValue) => {
//   if (!newValue) {
//     try {
//       await camera.start()
//       startCountdown()
//     } catch (error) {
//       console.error("카메라 시작 오류:", error)
//     }
//   }
// })

// 종료 버튼 클릭 시
function stopCameraAndNavigate() {
  if (camera) {
    camera.stop()
  }
  router.push({ name: "Main" }) // /main으로 이동
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
