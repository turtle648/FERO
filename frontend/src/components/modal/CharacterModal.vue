<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" @click="closeCharacterModal">
    <div class="relative w-[70vw] h-[85vh] bg-white/90 rounded-lg shadow-xl overflow-hidden flex flex-col items-center p-6"
         @click.stop>
      
      <!-- 모달 종료 버튼 -->
      <button @click="closeCharacterModal"
              class="absolute top-2 right-2 text-gray-600 hover:text-black text-lg">
        ✖
      </button>

      <!-- 선택된 캐릭터 미리보기 -->
      <div class="relative w-[40vw] aspect-square flex items-center justify-center mb-4 border-2 border-gray-500 bg-gray-100 rounded-md">
        <img v-if="selected.face" :src="selected.face" class="absolute w-[80%] top-0">
        <img v-if="selected.hair" :src="selected.hair" class="absolute w-[80%] top-0">
        <img v-if="selected.body" :src="selected.body" class="absolute w-[80%] bottom-0">
      </div>

      <!-- 카테고리 선택 -->
      <div class="flex space-x-4 mb-4">
        <button v-for="category in categories" :key="category"
          @click="currentCategory = category"
          class="w-[15vw] px-4 py-2 border rounded-md transition duration-200 ease-in-out"
          :class="{ 'bg-blue-500 text-white': currentCategory === category }">
          {{ category }}
        </button>
      </div>

      <hr class="w-full border-gray-300 mb-4">

      <!-- 선택 가능한 요소들 -->
      <div class="flex flex-col items-center w-full overflow-y-auto" :style="{ maxHeight: selectionBoxHeight }">
        <div class="grid grid-cols-3 gap-2 p-3 rounded-md">
          <img v-for="item in assets[currentCategory]" :key="item"
            :src="item" @click="selectItem(currentCategory, item)"
            class="w-[15vw] aspect-square border-2 p-1 bg-white cursor-pointer rounded-md transition duration-200 ease-in-out"
            :class="{ 'border-blue-500': selected[currentCategory] === item }"/>
        </div>
      </div>

      <!-- 완료 버튼 -->
      <div class="absolute bottom-0 left-0 w-full flex justify-center p-4 rounded-b-lg">
        <button @click="confirmSelection"
                class="w-[15vw] mt-4 px-4 py-2 border rounded-md bg-blue-500 text-white hover:bg-green-600 transition duration-200 ease-in-out">
          완료
        </button>
      </div>

    </div>
  </div>
</template>
  
<script setup>
import { ref, computed, defineEmits } from 'vue'
import { assets } from '@/assets.js'

// 모달 닫기
const emit = defineEmits(["closeCharacter"])
const closeCharacterModal = () => {
  emit("closeCharacter")
}

// 완료 버튼 클릭 시 선택 데이터 처리
const confirmSelection = () => {
  console.log("선택 완료:", selected.value);
  closeCharacterModal();
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

// 선택 요소 박스의 maxHeight 동적 계산
const selectionBoxHeight = computed(() => {
  return `calc(70vh - 5rem - 40vw)`;
})
</script>
