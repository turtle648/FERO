<template>
  <div>
    <button @click="goToMain">Start</button>
    <!-- 토큰이 없으면 모달 띄우기 -->
    <SignInUp v-if="showModal" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import SignInUp from '@/components/modal/SignInUp.vue'

const router = useRouter()
const showModal = ref(false)

// 토큰이 존재하는지 확인
// Pinia 상태 관리 사용하여 토큰 유무 확인해도 괜찮음.
const token = localStorage.getItem('authToken')

const goToMain = () => {
  if (!token) {
    // 토큰이 없으면 모달 띄우기
    showModal.value = true
  } else {
    // 토큰이 있으면 메인 페이지로 이동
    router.push('/main')
  }
}

// 모달 창 닫기
const closeModal = () => { showModal.value = false }
</script>
