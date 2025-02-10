<template>
    <div class="flex flex-col items-center p-4">
      <!-- 선택된 캐릭터 미리보기 -->
      <div class="relative w-40 h-40 flex items-center justify-center mb-4 border-2 border-gray-500">
        <!-- 얼굴 -->
        <img v-if="selected.face" :src="selected.face" class="absolute w-20 top-20">  
        <!-- 머리 -->
        <img v-if="selected.hair" :src="selected.hair" class="absolute w-20 top-20">
        <!-- 몸 (머리와 얼굴보다 아래) -->
        <img v-if="selected.body" :src="selected.body" class="absolute w-20" style="top: 35%;">
      </div>
  
      <!-- 카테고리 선택 -->
      <div class="flex space-x-4 mb-4">
        <button v-for="category in categories" :key="category"
          @click="currentCategory = category"
          class="px-4 py-2 border rounded-md"
          :class="{ 'bg-blue-500 text-white': currentCategory === category }">
          {{ category }}
        </button>
      </div>
  
      <!-- 선택 가능한 요소들 -->
      <div class="grid grid-cols-3 gap-2">
        <img v-for="item in assets[currentCategory]" :key="item"
          :src="item" @click="selectItem(currentCategory, item)"
          class="w-16 h-16 border-2 p-1 cursor-pointer"
          :class="{ 'border-blue-500': selected[currentCategory] === item }"/>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { assets } from '@/assets.js'
  
  const categories = ['hair', 'face', 'body']
  const currentCategory = ref('hair')
  
  const selected = ref({
    hair: null,
    face: null,
    body: null
  })
  
  const selectItem = (category, item) => {
    // 이미 선택된 경우, 선택 해제
    if (selected.value[category] === item) {
      selected.value[category] = null
    } else {
      selected.value[category] = item
    }
  }
  </script>
  
  <style scoped>
  .absolute {
    position: absolute;
  }
  </style>
  