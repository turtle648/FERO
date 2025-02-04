<template>
    <div class="tutorial-page">
      <h1>스쿼트 튜토리얼</h1>
      <!-- 튜토리얼 내용 -->
      <button @click="completeTutorial">튜토리얼 완료</button>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const completeTutorial = () => {
    // 여기에 튜토리얼 완료 로직 추가
    router.push('/main')
  }
  </script>
  
  <style scoped>
  .tutorial-page {
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  </style>
  