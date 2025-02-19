<template>
  <ButtonAtom @click="$emit('toggleAudio')">
    <i class="text-white text-2xl" :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'">click</i>
  </ButtonAtom>
</template>

<script setup>
import { ref } from 'vue'
import ButtonAtom from '@/components/atoms/ButtonAtom.vue'

const isPlaying = ref(false)

defineEmits(['toggleAudio'])
</script>
