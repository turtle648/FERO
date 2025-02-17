<!-- organisms/StartButtonOrganism.vue -->
<template>
    <BaseButtonMolecule
      class="fixed bottom-[5%] left-1/2 -translate-x-1/2 
        px-8 py-4 whitespace-nowrap
        font-['Press_Start_2P'] text-2xl
        tracking-wider leading-none
        [text-shadow:2px_2px_0_#000,_-2px_-2px_0_#000,_2px_-2px_0_#000,_-2px_2px_0_#000]
        animate-[blink_1.5s_infinite]
        sm:text-xl sm:px-6 sm:py-3
        xs:text-lg xs:px-4 xs:py-2"
      @click="$emit('click')"
    >
      <ButtonTextMolecule />
    </BaseButtonMolecule>
  </template>
  
  <script setup>
  import BaseButtonMolecule from '@/components/molecules/BaseButtonMolecule.vue'
  import ButtonTextMolecule from '@/components/molecules/ButtonTextMolecule.vue'
  import { defineEmits } from 'vue'

  defineEmits(['click'])
  </script>
  
  <style scoped>
  @keyframes blink {
    0%, 100% { opacity: 1; }
    50% { opacity: 0; }
  }
  </style>
  