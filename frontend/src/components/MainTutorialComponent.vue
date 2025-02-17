<template>
  <div class="img-container z-50 fixed inset-0 flex items-center justify-center" @click="nextStep">
    <!-- 설명 단계별로 표시 -->
    <section class="message-list flex items-center absolute top-3/4 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
      <!-- 이미지 -->
      <img src="@/assets/images/pesocom.png" class="w-[15vh] object-contain mt-40" alt="Tutorial Image" />

      <!-- Balloon -->
      <div class="desc-container nes-balloon from-left ml-4 w-[37vh] h-[12vh] -translate-y-4">
        <span v-if="currentStep === 1" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">ㅎㅇㅎㅇ 저는 파소콩</span>
        <span v-if="currentStep === 2" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">먼저 왼쪽 상단에는 타이머가 있어</span>
        <span v-if="currentStep === 3" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">운동 시간을 체크할 수 있음</span>
        <span v-if="currentStep === 4" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">그 옆 오른쪽에는 카운트!</span>
        <span v-if="currentStep === 5" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">바른 자세로 운동을 하면 카운트가 +1</span>
        <span v-if="currentStep === 6" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">스쿼트를 할 때 자세에 대해 알려드림</span>
        <span v-if="currentStep === 7" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">카메라를 약간 측면으로 보고 서서</span>
        <span v-if="currentStep === 8" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">무릎이 90도가 될 때까지</span>
        <span v-if="currentStep === 9" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">내려갔다가 올라오면 카운트가 + 1</span>
        <span v-if="currentStep === 10" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">아래의 가운데에는 종료 버튼이 있음</span>
        <span v-if="currentStep === 11" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">운동을 하다가 종료하고 싶을 때 클릭!</span>
        <span v-if="currentStep === 12" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">종료 버튼 옆에는 신고 버튼이 있음</span>
        <span v-if="currentStep === 13" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">운동을 하다가 버그가 발생했거나</span>
        <span v-if="currentStep === 14" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">불쾌한 옷차림의 유저를 만났을 때 신고하면 됨</span>
        <span v-if="currentStep === 15" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">자 그럼 바로 운동을 하러 가볼까?</span>
      </div>
    </section>

    <!-- 튜토리얼 완료 버튼 -->
    <div v-if="currentStep === 15" class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center space-y-4 z-50">
      <button class="px-4 py-2 nes-btn is-primary font-dgm z-50" @click="completeUiTutorial">튜토리얼 완료!</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"

const router = useRouter()
const mainStore = useMainStore()

// 현재 단계 상태 관리
const currentStep = ref(1)

// 다음 단계로 이동하는 함수
const nextStep = () => {
  if (currentStep.value < 15) {
    currentStep.value += 1 // 다음 단계로 이동
  }
}

const completeUiTutorial = async () => {
  try {
    const success = await mainStore.completeTutorial(TUTORIAL_IDS.MAIN_PAGE)

    if (success) {
      //   const nextExercise = route.params.exercise || TUTORIAL_IDS.SQUAT // 기본값 설정
      //   if (!nextExercise) {
      //     console.error("운동 정보가 누락되었습니다.")
      //     return
      //   }
      router.push({
        name: "UiTutorial",
      })
    } else {
      console.error("튜토리얼 완료 처리 실패")
    }
  } catch (error) {
    console.error("튜토리얼 완료 중 오류 발생:", error)
  }
}
</script>

<style scoped></style>
