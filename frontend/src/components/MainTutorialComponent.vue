<template>
  <div class="img-container z-50 fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" @click="nextStep">
    <!-- 설명 단계별로 표시 -->
    <section class="message-list flex items-center absolute top-3/4 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
      <!-- 이미지 -->
      <img src="@/assets/images/pesocom.png" class="w-[15vh] object-contain mt-40" alt="Tutorial Image" />

      <!-- Balloon -->
      <div class="desc-container nes-balloon from-left ml-4 w-[39vh] h-[13vh] -translate-y-1 flex justify-center items-center">
        <span v-if="currentStep === 1" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">안녕하세요 용사님</span>
        <span v-if="currentStep === 2" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">메인페이지 기능에 대해 설명해드릴게요</span>
        <span v-if="currentStep === 3" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">상단의 유저정보를 클릭하면 스탯을 확인할 수 있습니다</span>
        <span v-if="currentStep === 4" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">메인의 중앙에 있는 캐릭터를 클릭하면</span>
        <span v-if="currentStep === 5" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">캐릭터의 얼굴, 옷, 헤어스타일을 바꿀 수 있습니다</span>
        <span v-if="currentStep === 6" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">이제 하단 아이콘에 대해 설명 드리겠습니다</span>
        <span v-if="currentStep === 7" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">가장 왼쪽의 Calendar를 클릭하면</span>
        <span v-if="currentStep === 8" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">퀘스트 달성 여부와 스탯 성장률을 볼 수 있습니다</span>
        <span v-if="currentStep === 9" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">Record를 클릭하면 유저의 전적 기록을 확인할 수 있습니다</span>
        <span v-if="currentStep === 10" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">가운데 Fitness 버튼은 운동을 할 때 사용할 수 있습니다</span>
        <span v-if="currentStep === 11" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">하고 싶은 종목과 모드를 선택하여 운동을 할 수 있습니다</span>
        <span v-if="currentStep === 12" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">모드는 혼자 운동을 할 수 있는 싱글 모드</span>
        <span v-if="currentStep === 13" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">랜덤 매칭을 하고 랭크를 올릴 수 있는 랭크모드가 있습니다</span>
        <span v-if="currentStep === 14" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">Quest 버튼은 매일 퀘스트를 확인할 수 있습니다</span>
        <span v-if="currentStep === 15" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">그리고 마지막으로 Setting 버튼에서는 회원탈퇴와</span>
        <span v-if="currentStep === 16" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">음악 볼륨 조절을 할 수 있습니다</span>
        <span v-if="currentStep === 17" class="font-dgm sm:text-xs md:text-lg lg:text-xl xl:text-2xl">자 그럼 바로 운동을 하러 가볼까요?</span>
      </div>
    </section>

    <!-- 튜토리얼 완료 버튼 -->
    <div v-if="currentStep === 17" class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center space-y-4 z-50">
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
  if (currentStep.value < 17) {
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
