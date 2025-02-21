<template>
  <div class="ui-tutorial-page w-full h-screen object-fill bg-black flex" @click="nextStep">
    <!-- 설명 단계별로 표시 -->
    <section v-if="currentStep <= 14" 
             class="message-list flex items-center absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-black">
      <!-- 이미지 -->
      <img src="@/assets/images/pesocom.png" 
           class="w-[15vh] object-contain mt-40" 
           alt="Tutorial Image" />

      <!-- Balloon -->
      <div class="desc-container nes-balloon from-left ml-4 w-[40vh] h-[15vh] -translate-y-8 flex justify-center items-center">
      <span v-if="currentStep === 1" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">운동 페이지의 기능에 대해 설명을 해드리겠습니다</span>
      <span v-if="currentStep === 2" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">먼저 왼쪽 상단에는 남은 시간 체크를 위한</span>
      <span v-if="currentStep === 3" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">
      <span class="text-red-500">타이머</span>
      가 있습니다
      </span>
      <span v-if="currentStep === 4" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">그리고 타이머의 오른쪽에는</span>
      <span v-if="currentStep === 5" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">
      운동 횟수를 볼 수 있는
      <span class="text-red-500">카운트</span>
      가 있습니다
      </span>
      <span v-if="currentStep === 6" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">
      하단에는 운동을 종료할 수 있는
      <span class="text-red-500">종료버튼</span>
      ,
      </span>
      <span v-if="currentStep === 7" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">운동을 하다가 버그가 발생했거나</span>
      <span v-if="currentStep === 8" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">불쾌한 옷차림의 유저를 만났을 때</span>
      <span v-if="currentStep === 9" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">
      사용할 수 있는
      <span class="text-red-500">신고버튼</span>
      이 있습니다
      </span>
      <span v-if="currentStep === 10" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">
      이제 운동을 할 때
      <span class="text-red-500">유의점</span>
      을 알려드리겠습니다
      </span>
      <span v-if="currentStep === 11" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">먼저 카메라에 머리부터 발 끝까지 나오도록 서주세요</span>
      <span v-if="currentStep === 12" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">그 다음 정확한 측정을 위해 약간 측면으로 돌아주세요</span>
      <span v-if="currentStep === 13" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">스쿼트가 측정이 되기 위해서는 무릎이 90도가 될 때까지 내려가야합니다</span>
      <span v-if="currentStep === 14" class="font-dgm sm:text-base md:text-lg lg:text-xl xl:text-2xl">자세 예시 동영상을 제공하겠습니다</span>
      </div>
    </section>

    <!-- 비디오 섹션 -->
    <section v-if="currentStep === 15" 
             class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-4/5 max-w-4xl">
      <video
        ref="tutorialVideo"
        class="w-[80vh] h-auto rounded-lg"
        controls
        autoplay
        @ended="videoEnded"
      >
        <source src="@/assets/Squat.mp4" type="video/mp4">
        브라우저가 비디오를 지원하지 않습니다.
      </video>
    </section>

    <!-- 튜토리얼 완료 버튼 -->
    <div v-if="currentStep === 16" 
         class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center space-y-4 z-50">
      <button class="px-4 py-2 nes-btn is-primary font-dgm z-50 text-sm" 
              @click="completeUiTutorial">튜토리얼 완료!</button>
    </div>

    <!-- 상단 UI 요소들 -->
    <div class="text-container absolute top-4 right-4 z-50">
      <button type="button" class="nes-btn is-primary text-1xl font-dgm disabled">스쿼트 횟수: 0</button>
    </div>

    <div class="timer-container text-2xl absolute top-4 left-4 z-0 nes-btn disabled">999:00</div>

    <!-- 하단 버튼들 -->
    <div class="button-container flex absolute inset-0 items-end pb-4 z-30">
      <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2">
        <ExitButton />
      </div>
      <div class="absolute bottom-4 right-4">
        <ReportButton />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter, useRoute } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import ExitButton from "@/components/button/ExitButton.vue"
import ReportButton from "@/components/button/ReportButton.vue"

const router = useRouter()
const route = useRoute()
const mainStore = useMainStore()

const currentStep = ref(1)

const nextStep = () => {
  if (currentStep.value < 14) {
    currentStep.value += 1
  } else if (currentStep.value === 14) {
    currentStep.value = 15  // 자동으로 비디오 재생
  }
}

const videoEnded = () => {
  currentStep.value = 16  // 비디오 종료 후 완료 버튼 표시
}

const completeUiTutorial = async () => {
  try {
    const success = await mainStore.completeTutorial(TUTORIAL_IDS.UI)
    if (success) {
      const nextExercise = route.params.exercise || TUTORIAL_IDS.SQUAT
      if (!nextExercise) {
        console.error("운동 정보가 누락되었습니다.")
        return
      }
      router.push({
        name: "Tutorial",
        params: { exercise: nextExercise },
      })
    } else {
      console.error("튜토리얼 완료 처리 실패")
    }
  } catch (error) {
    console.error("튜토리얼 완료 중 오류 발생:", error)
  }
}
</script>
