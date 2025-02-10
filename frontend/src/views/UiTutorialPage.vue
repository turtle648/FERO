<template>
    <div class="ui-tutorial-page">
      <h1>UI 튜토리얼</h1>
      <p>여기에서 UI 사용법을 배울 수 있습니다.</p>
      <button @click="completeUiTutorial">튜토리얼 완료</button>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  import { useMainStore } from '@/stores/mainStore'
  
  const router = useRouter()
  const mainStore = useMainStore()
  
  const completeUiTutorial = () => {
    // 튜토리얼 ID 1번 상태 업데이트
    const tutorial = mainStore.tutorial.find(t => t.tutorialId === 1)
    if (tutorial) tutorial.completed = true
  
    router.push('/main') // 메인 페이지로 이동
  }
  </script>
  
  <style scoped>
  .ui-tutorial-page {
    @apply flex flex-col items-center justify-center h-screen bg-gray-100;
  }
  </style>
  