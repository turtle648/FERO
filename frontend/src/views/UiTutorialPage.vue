<template>
  <div class="ui-tutorial-page">
    <h1 class="z-10">UI 튜토리얼</h1>
    <p>여기에서 UI 사용법을 배울 수 있습니다.</p>
    <button @click="completeUiTutorial">튜토리얼 완료</button>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"

const router = useRouter()
const mainStore = useMainStore()

const completeUiTutorial = async () => {
  try {
    // 메인스토어의 completeTutorial 함수 호출
    const success = await mainStore.completeTutorial(TUTORIAL_IDS.UI)
    
    if (success) {
      router.push('/main')
    } else {
      console.error('튜토리얼 완료 처리 실패')
    }
  } catch (error) {
    console.error('튜토리얼 완료 중 오류 발생:', error)
  }
}

</script>

<style scoped>
.ui-tutorial-page {
  @apply flex flex-col items-center justify-center h-screen bg-gray-100;
}
</style>
