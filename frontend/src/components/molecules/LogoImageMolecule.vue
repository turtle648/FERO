<!-- molecules/LogoImageMolecule.vue -->
<template>
    <BaseImageAtom 
      :src= LogoImage
      alt="로고 이미지 로딩 에러"
      class="w-[80%] max-w-4xl"
    />
  </template>
  
  <script setup>
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import LogoImage from '@/assets/images/logo.png'
  </script>
  