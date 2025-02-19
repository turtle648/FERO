<template>
  <div class="tutorial-page w-screen h-screen flex flex-col items-center relative">
    <!-- <div class="w-screen h-screen bg-black bg-opacity-60 z-5"></div> -->
    <component :is="currentExerciseComponent" class="z-0 fixed inset-0 overflow-hidden" />
  </div>
</template>

<script setup>
import { useRoute } from "vue-router"
import SquatComponent from "@/components/SquatComponent.vue"
import { onMounted } from "vue"

const route = useRoute()

const exerciseType = route.params.exercise

const exerciseComponents = {
  2: SquatComponent,
}

const currentExerciseComponent = exerciseComponents[exerciseType] || null

onMounted(() => {
  console.log(exerciseType)
})
</script>

<style scoped></style>
