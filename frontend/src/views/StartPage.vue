<template>
    <div>
      <button @click="goToMain">메인 페이지로</button>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const goToMain = () => {
    router.push('/main')
  }
  </script>