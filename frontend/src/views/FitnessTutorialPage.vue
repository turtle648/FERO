<template>
  <div class="tutorial-page w-screen h-screen flex flex-col items-center relative">
    <!-- 튜토리얼 내용 -->
    <!-- <img src="@/assets/furni_1.png" alt="" class="w-4/5 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-10" /> -->
    <div class="w-screen h-screen bg-black bg-opacity-60 z-5"></div>
    <component :is="currentExerciseComponent" class="z-0" />
  </div>
  <!-- <div class="desciption"></div> -->
</template>

<script setup>
import { useRoute } from "vue-router"
import SquatComponent from "@/components/SquatComponent.vue"

const route = useRoute()

const exerciseType = route.params.exercise

const exerciseComponents = {
  1: SquatComponent,
}

const currentExerciseComponent = exerciseComponents[exerciseType] || null
</script>

<style scoped></style>
