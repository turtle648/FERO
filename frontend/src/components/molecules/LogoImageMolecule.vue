<!-- molecules/LogoImageMolecule.vue -->
<template>
  <div class="flex flex-col items-center justify-center h-screen w-full relative top-10">
    <BaseImageAtom 
      :src= LogoImage
      alt="로고 이미지 로딩 에러"
      class="w-[80%] max-w-4xl"
    />
    <BaseImageAtom 
      :src= LogoImage2
      alt="로고 이미지2 로딩 에러"
      class="w-[80%]"
    />
  </div>
</template>
  
  <script setup>
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import LogoImage from '@/assets/images/logo.png'
  import LogoImage2 from '@/assets/images/logo2.png'
  </script>
  