<!-- views/StartPage.vue -->
<template>
  <StartPageLayout
    :showModal="showModal"
    @goToMain="goToMain"
    @closeModal="closeModal"
    ref="audioPlayerRef"
  />
</template>

<script setup>
import { ref,nextTick } from 'vue'
import { useRouter } from 'vue-router'
import StartPageLayout from '@/components/layouts/StartPageLayout.vue'

const router = useRouter()
const showModal = ref(false)
const token = localStorage.getItem('authToken')

const goToMain = async () => {
  if (!token) {
    showModal.value = true
    await nextTick()
  } else {
    try {
      await router.push('/main')
    } catch (error) {
      console.error('라우팅 에러:', error)
    }
  }
}

const closeModal = () => { 
  showModal.value = false 
}
</script>
