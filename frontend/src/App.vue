<template>
  <router-view v-if="!isDesktop" />
  <QRComponent v-else />
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import QRComponent from "@/components/QRComponent.vue"

const isDesktop = ref(window.matchMedia("(min-width: 821px)").matches)
const checkScreenSize = () => {
  isDesktop.value = window.innerWidth > 821
}

onMounted(() => {
  window.addEventListener("resize", checkScreenSize)
})

onUnmounted(() => {
  window.removeEventListener("resize", checkScreenSize)
})
</script>

<style scoped></style>
