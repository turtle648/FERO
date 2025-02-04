<script setup>
import { defineProps, onMounted, onUnmounted, ref } from 'vue'

const props = defineProps(['track'])

const audioElement = ref(null)

onMounted(() => {
  if (audioElement.value) {
    props.track.attach(audioElement.value)
  }
})

onUnmounted(() => {
  props.track.detach()
})
</script>

<template>
  <audio ref="audioElement" :id="track.sid"></audio>
</template>
