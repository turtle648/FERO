<template>
  <router-view v-if="!isDesktop" />
  <QRComponent v-else />
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import QRComponent from '@/views/QRComponent.vue'

const isDesktop = ref(false)
const checkScreenSize = () => {
  isDesktop.value = window.innerWidth > 821
}
onMounted(() => {
  checkScreenSize()
  window.addEventListener("resize", checkScreenSize)
})

onUnmounted(() => {
  window.removeEventListener("resize", checkScreenSize)
})
</script>

<style scoped></style>
