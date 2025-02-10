<template>
  <div class="counter-container relative">
    <div class="absolute top-4 left-1/2 transform -translate-x-1/2 z-10 text-center">
      <div class="count z-10 text-black">스쿼트 횟수: {{ count }}</div>
      <!-- <div class="status">{{ feedback }}</div> -->
      <!-- <div v-if="formFeedback" class="form-feedback">{{ formFeedback }}</div> -->
      <!-- <div v-if="!isReady" class="form-feedback z-10">측면으로 서주세요!</div> -->
      <div v-if="showGreat" class="great-message text-red text-3xl">Great!</div>
    </div>
    <MediapipeComponent @pose-detected="processPose" class="z-0" />
  </div>
</template>

<script setup>
import { ref } from "vue"
import MediapipeComponent from "@/components/MediapipeComponent.vue"

// const emit = defineEmits(["pose-detected"])

// 상태 변수 정의
const count = ref(0)
const isDown = ref(false)
const feedback = ref("준비중...")
const formFeedback = ref("")
const showGreat = ref(false)

// 각도 계산 함수
const calculateAngle = (a, b, c) => {
  const radians = Math.atan2(c.y - b.y, c.x - b.x) - Math.atan2(a.y - b.y, a.x - b.x)
  let degrees = Math.abs((radians * 180.0) / Math.PI)
  if (degrees > 180.0) degrees = 360 - degrees
  return degrees
}

// 자세 체크 함수
const checkForm = (landmarks) => {
  console.log(landmarks)
  const leftHip = landmarks[23]
  const rightHip = landmarks[24]
  const leftKnee = landmarks[25]
  const rightKnee = landmarks[26]
  const leftAnkle = landmarks[27]
  const rightAnkle = landmarks[28]

  console.log(rightAnkle, leftAnkle, rightKnee, leftKnee)

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

  // isReady.value = true
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
  console.log(leftAnkle, rightAnkle, leftKnee, rightKnee)

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
</style>
