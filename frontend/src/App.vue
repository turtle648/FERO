<template>
  <router-view v-if="!isDesktop" />
  <QRComponent v-else />
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import QRComponent from "@/components/QRComponent.vue"

// matchMedia()를 사용하여 초기값 설정
const mediaQuery = window.matchMedia("(min-width: 821px)")
const isDesktop = ref(mediaQuery.matches)

// 반응형 감지를 위한 핸들러 함수
const checkScreenSize = (event) => {
  isDesktop.value = event.matches
}

onMounted(() => {
  // matchMedia 이벤트 리스너 사용 (resize 대신)
  mediaQuery.addEventListener("change", checkScreenSize)
})

onUnmounted(() => {
  mediaQuery.removeEventListener("change", checkScreenSize)
})
</script>

<style scoped></style>
