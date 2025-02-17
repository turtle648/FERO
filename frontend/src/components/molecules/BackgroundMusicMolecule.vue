<!-- molecules/BackgroundMusicMolecule.vue -->
<template>
    <AudioAtom 
      ref="audioPlayer"
      :src="require('@/assets/musics/background_music.mp3')"
      type="audio/mp3"
      :loop="true"
    />
  </template>
  
  <script setup>
  import AudioAtom from '@/components/atoms/AudioAtom.vue'
  import { ref, defineExpose } from 'vue'
  
  const audioPlayer = ref(null)
  
  defineExpose({
    play: () => audioPlayer.value?.play(),
    pause: () => audioPlayer.value?.pause()
  })
  </script>
  