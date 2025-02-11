<template>
  <div class="counter-container relative">
    <div class="absolute top-4 left-1/2 transform -translate-x-1/2 z-10 text-center">
      <div class="count z-10 text-black">스쿼트 횟수: {{ count }}</div>
      <div v-if="showGreat" class="great-message text-red text-3xl">Great!</div>
    </div>
    <MediapipeComponent @pose-detected="processPose" class="z-0" />
    <CompleteModal v-if="showModal" @openModal="openModal"/>

    <div v-if="showErrorModal" class="landmark-error-modal">전신이 나오도록 카메라 위치를 수정해주세요</div>
    <button v-if="isTutorialMode" @click="setCountToThree" class="fixed top-4 right-4 bg-blue-500 text-white px-4 py-2 rounded shadow-lg hover:bg-blue-600 z-50">Set Count to 3</button>
  </div>
</template>

<script setup>
import { ref } from "vue"
import MediapipeComponent from "@/components/MediapipeComponent.vue"
import CompleteModal from "@/components/modal/CompleteModal.vue"

// 상태 변수 정의
const count = ref(0)
const isDown = ref(false)
const feedback = ref("준비중...")
const formFeedback = ref("")
const showGreat = ref(false)
const showModal = ref(false)
const showErrorModal = ref(false) // 에러 모달 상태 변수

const isTutorialMode = window.location.href.includes("tutorial")


// 필수 랜드마크 정의
const requiredLandmarks = [0, 1, 2, 3, 4, 5, 6, 27, 28, 29, 30, 31, 32]

// 필수 랜드마크 확인 함수
const checkRequiredLandmarks = (landmarks) => {
  return requiredLandmarks.every((index) => {
    const landmark = landmarks[index]
    return (
      landmark && landmark.x >= 0 && landmark.x <= 1 && landmark.y >= 0 && landmark.y <= 1 && landmark.visibility !== undefined && landmark.visibility > 0.5 // 가시성 기준 추가
    )
  })
}

const openModal = () => { showModal.value = true }
// 각도 계산 함수
const calculateAngle = (a, b, c) => {
  const radians = Math.atan2(c.y - b.y, c.x - b.x) - Math.atan2(a.y - b.y, a.x - b.x)
  let degrees = Math.abs((radians * 180.0) / Math.PI)
  if (degrees > 180.0) degrees = 360 - degrees
  return degrees
}

// 자세 체크 함수
const checkForm = (landmarks) => {
  const leftHip = landmarks[23]
  const rightHip = landmarks[24]
  const leftKnee = landmarks[25]
  const rightKnee = landmarks[26]
  const leftAnkle = landmarks[27]
  const rightAnkle = landmarks[28]

  const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle)
  const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle)
  let formMessages = []

  if (leftKneeAngle < 80 || rightKneeAngle < 80) {
    formMessages.push("너무 깊이 내려가지 않도록 조절하세요!")
  } else if (leftKneeAngle > 120 || rightKneeAngle > 120) {
    formMessages.push("스쿼트 자세를 조금 더 깊게 내려가 보세요!")
  }

  if (leftKnee.x > leftAnkle.x || rightKnee.x > rightAnkle.x) {
    formMessages.push("무릎이 발보다 앞에 나가면 안 돼요!")
  }

  return formMessages.join(", ")
}

// 포즈 처리 함수
const processPose = (landmarks) => {
  if (!landmarks || landmarks.length === 0) {
    // isReady.value = false
    feedback.value = "준비중..."
    return
  }

  // 필수 랜드마크 확인
  if (!checkRequiredLandmarks(landmarks)) {
    showErrorModal.value = true // 필수 랜드마크가 없으면 모달 표시
  } else {
    showErrorModal.value = false // 필수 랜드마크가 모두 있으면 모달 숨김
  }

  // isReady.value = true
  if (!showErrorModal.value) {
    formFeedback.value = checkForm(landmarks)

    const leftHip = landmarks[23]
    const rightHip = landmarks[24]
    const leftKnee = landmarks[25]
    const rightKnee = landmarks[26]
    const leftAnkle = landmarks[27]
    const rightAnkle = landmarks[28]

    const leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle)
    const rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle)
    const avgKneeAngle = (leftKneeAngle + rightKneeAngle) / 2

    if (isTutorialMode && count.value >= 3) {
      return
    }

    if (!isDown.value && avgKneeAngle < 100) {
      isDown.value = true
      feedback.value = "Down"
    } else if (isDown.value && avgKneeAngle > 160) {
      isDown.value = false
      count.value++
      feedback.value = `Up! Count: ${count.value}`

      showGreat.value = true
      setTimeout(() => {
        showGreat.value = false
      }, 1000)

      if (isTutorialMode && count.value === 3) {
        showModal.value = true
      }
    }
  }
}

// 임시 버튼 클릭 핸들러 함수
const setCountToThree = () => {
  count.value = 3
  if (isTutorialMode) {
    showModal.value = true // 튜토리얼 모드일 경우 완료 상태로 전환
  }
}
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

.landmark-error-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  z-index: 100;
}
.set-count-button {
  margin-top: 20px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  z-index: 100;
}
</style>
