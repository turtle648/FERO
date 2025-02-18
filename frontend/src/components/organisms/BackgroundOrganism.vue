<!-- organisms/BackgroundOrganism.vue -->
<template>
    <div>
      <BackgroundImageMolecule />
      <BackgroundMusicMolecule ref="audioPlayer" />
    </div>
  </template>
  
  <script setup>
  import { ref, defineExpose } from 'vue'
  import BackgroundImageMolecule from '@/components/molecules/BackgroundImageMolecule.vue'
  import BackgroundMusicMolecule from '@/components/molecules/BackgroundMusicMolecule.vue'
  
  const audioPlayer = ref(null)
  const isPlaying = ref(false)
  
  defineExpose({
    play: () => {
      audioPlayer.value?.play()
      isPlaying.value = true
    },
    pause: () => {
      audioPlayer.value?.pause()
      isPlaying.value = false
    },
    isPlaying: isPlaying
  })
  </script>
  