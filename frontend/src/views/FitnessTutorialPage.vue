<template>
  <div class="tutorial-page w-screen h-screen flex flex-col items-center relative">
    <!-- 운동 컴포넌트 -->
    <component :is="currentExerciseComponent" class="z-0" />
  </div>
</template>

<script setup>
import { useRoute } from "vue-router"
import SquatComponent from "@/components/SquatComponent.vue"

const route = useRoute()

const exerciseType = route.params.exercise

const exerciseComponents = {
  2: SquatComponent,
}

const currentExerciseComponent = exerciseComponents[exerciseType] || null
</script>

<style scoped></style>
