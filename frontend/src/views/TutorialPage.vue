<template>
  <div class="tutorial-page w-screen h-screen bg-black bg-opacity-50 p-5 flex flex-col items-center relative">
    <!-- 튜토리얼 내용 -->
    <div class="time"></div>
    <div class="count"></div>
    <img src="@/assets/furni_1.png" alt="" class="w-4/5" />
    <component :is="currentExerciseComponent" />
    <button @click="completeTutorial">튜토리얼 완료</button>
  </div>
  <!-- <div class="desciption"></div> -->
</template>

<script setup>
import { useRouter, useRoute } from "vue-router"
import SquatComponent from "@/components/SquatComponent.vue"

const router = useRouter()
const route = useRoute()

const exerciseType = route.params.exercise

const exerciseComponents = {
  squat: SquatComponent,
}

const currentExerciseComponent = exerciseComponents[exerciseType] || null

const completeTutorial = () => {
  router.push("/main")
}
</script>

<style scoped>
/* .desciption {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1;
} */
</style>
