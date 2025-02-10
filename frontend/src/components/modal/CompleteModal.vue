<template>
  <div class="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50 z-[9999]">
    <div class="bg-white p-6 rounded-lg shadow-lg text-center">
      <p class="text-lg font-bold mb-4">튜토리얼 완료!</p>
      <button @click="completeTutorial" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router"

const router = useRouter()

const completeTutorial = () => {
  router.push("/main")
}
</script>

<style scoped></style>
