<!-- molecules/BaseButtonMolecule.vue -->
<template>
    <ButtonAtom
      class="hover:scale-105 active:scale-95"
      @click="$emit('click')"
    >
      <slot></slot>
    </ButtonAtom>
  </template>
  
  <script setup>
  import ButtonAtom from '@/components/atoms/ButtonAtom.vue'
  import { defineEmits } from 'vue'
  
  defineEmits(['click'])
  </script>
  