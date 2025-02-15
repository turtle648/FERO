<template>
    <div class="flex items-center justify-between p-4 border-b">
      <div class="flex items-center gap-4">
        <BaseImageAtom
          v-if="icon"
          :src="icon"
          :alt="title"
          class="w-10 h-10"
        />
        <TextAtom
          text-size="text-xl"
          font-weight="font-bold"
        >
          {{ title }}
        </TextAtom>
      </div>
      <ButtonAtom
        variant="secondary"
        class="!p-2"
        @click="$emit('close')"
      >
        ✕
      </ButtonAtom>
    </div>
  </template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue'
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import TextAtom from '@/components/atoms/TextAtom.vue'
  import ButtonAtom from '@/components/atoms/ButtonAtom.vue'
  
  defineProps({
    title: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      default: ''
    }
  })
  
  defineEmits(['close'])
  </script>