<!-- organisms/BackgroundOrganism.vue -->
<template>
    <div>
      <BackgroundImageMolecule />
      <BackgroundMusicMolecule ref="audioPlayer" />
    </div>
  </template>
  
  <script setup>
  import BackgroundImageMolecule from '@/components/molecules/BackgroundImageMolecule.vue'
  import BackgroundMusicMolecule from '@/components/molecules/BackgroundMusicMolecule.vue'
  import { ref, defineExpose } from 'vue'

  const audioPlayer = ref(null)
  
  defineExpose({
    play: () => audioPlayer.value?.play(),
    pause: () => audioPlayer.value?.pause()
  })
  </script>
  