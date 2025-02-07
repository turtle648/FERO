<template>
    <div>
        <h1>single</h1>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps({
  exercise: {
    type: String,
    required: true
  },
  count: {
    type: String,
    required: true
  }
})
</script>

<style scoped>

</style>