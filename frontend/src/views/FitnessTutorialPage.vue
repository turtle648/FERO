<template>
  <div class="tutorial-page w-screen h-screen">
    <!-- 운동 컴포넌트 -->
    <component :is="currentExerciseComponent" />
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
