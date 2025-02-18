<!-- molecules/BackgroundImageMolecule.vue -->
<template>
    <BaseImageAtom 
      :src= backgroundImage
      alt="배경이미지 로딩 에러"
      class="absolute inset-0 w-full h-full object-cover -z-10"
    />
  </template>
  
  <script setup>
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import backgroundImage from '@/assets/images/background_startpage.png'
  </script>
  