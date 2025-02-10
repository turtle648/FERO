<template>
    <div class="modal">
        <div class="modal-content flex flex-col items-center p-4">
          <button id="close-btn" @click="closeCharacterModal">X</button>
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
    </div>
  </template>
  
  <script setup>
  import { ref, defineEmits } from 'vue'
  import { assets } from '@/assets.js'

  // 모달 닫기
  const emit = defineEmits(["closeCharacter"])
  const closeCharacterModal = () => {
    emit("closeCharacter")
  }
  
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

  .modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: rgba(0, 0, 0, 0.5); /* 배경에 반투명 검정색 추가 */
  }

  .modal-content {
    position: absolute;
    background: rgba(255, 255, 255, 0.8); /* 콘텐츠 영역 배경을 반투명 흰색으로 설정 */
    padding: 0;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    width: 90%;
    height: 70%;
    text-align: center;
    display: flex;
    flex-direction: column; /* 요소들을 세로로 나열 */
  }
  </style>
  