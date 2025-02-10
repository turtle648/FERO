<template>
  <div>
    <h1>multi</h1>
  </div>
</template>

<script setup>
import { defineProps, ref } from "vue"

defineProps({
  exercise: {
    type: String,
    required: true,
  },
})
// eslint-disable-next-line
const defaultCount = ref(2)
</script>

<style scoped></style>
