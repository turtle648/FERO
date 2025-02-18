<!-- atoms/AudioAtom.vue -->
<template>
    <audio ref="audio" :loop="loop">
      <source :src="src" :type="type">
    </audio>
</template>

<script setup>
import { defineProps, defineExpose, ref } from 'vue'

defineProps({
src: {
    type: String,
    required: true
},
type: {
    type: String,
    default: 'audio/mp3'
},
loop: {
    type: Boolean,
    default: false
}
})

const audio = ref(null)

defineExpose({
play: () => audio.value?.play(),
pause: () => audio.value?.pause()
})
</script>