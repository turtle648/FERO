<template>
  <div class="tutorial-page w-screen h-screen flex flex-col items-center relative">
    <!-- 튜토리얼 내용 -->
    <img src="@/assets/furni_1.png" alt="" class="w-4/5 absolute z-10" />
    <div class="w-screen h-screen bg-black bg-opacity-60 z-5"></div>
    <component :is="currentExerciseComponent" class="z-0" />

    <!-- 다음 -->
    <button>다음?</button>

    <button @click="completeTutorial" class="absolute bottom-0">튜토리얼 완료</button>
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

<style scoped></style>
