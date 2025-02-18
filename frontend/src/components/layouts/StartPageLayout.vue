<!-- layouts/StartPageLayout.vue -->
<template>
  <div class="h-full w-full fixed overflow-hidden">
    <BackgroundOrganism ref="audioPlayer" />
    <AudioControlButton 
      class="fixed top-4 right-4 z-50"
      @toggleAudio="handleAudioToggle"
    />
    <LogoOrganism />
    <StartButtonOrganism @click="$emit('goToMain')" />
    <SignInUpModal v-if="showModal" @close="$emit('closeModal')" />
  </div>
</template>

<script setup>
import BackgroundOrganism from '@/components/organisms/BackgroundOrganism.vue'
import LogoOrganism from '@/components/organisms/LogoOrganism.vue'
import StartButtonOrganism from '@/components/organisms/StartButtonOrganism.vue'
import SignInUpModal from '@/components/modal/SignInUpModal.vue'
import AudioControlButton from '@/components/molecules/AudioControlButton.vue'
import { defineProps, defineEmits, defineExpose, ref } from 'vue'

defineProps({
  showModal: Boolean
})

defineEmits(['goToMain', 'closeModal'])

const audioPlayer = ref(null)

defineExpose({
  playAudio: () => audioPlayer.value?.play(),
  pauseAudio: () => audioPlayer.value?.pause()
})

const handleAudioToggle = () => {
  if (audioPlayer.value) {
    if (audioPlayer.value.isPlaying) {
      audioPlayer.value.pause()
    } else {
      audioPlayer.value.play()
    }
  }
}
</script>
