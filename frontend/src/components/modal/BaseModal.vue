<!-- components/modal/BaseModal.vue -->
<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white w-[75%] h-[70%] max-w-4xl max-h-[80vh] rounded-lg shadow-lg flex flex-col">
        <!-- 헤더 영역 -->
        
        <div 
          class="relative w-full h-[10vh] flex items-center pl-[4vw] text-white text-xl font-bold"
          :style="{ 
            backgroundImage: `url(${require('@/assets/images/modal_header_background.png')})`, 
            backgroundSize: '100% 100%',  /* 부모 요소에 완전히 맞춤 (잘리는 부분 없음) */
            backgroundRepeat: 'no-repeat', /* 반복 방지 */
            backgroundPosition: 'center' /* 중앙 정렬 */
          }"
        >
          <!-- <img class="w-full" src="@/assets/images/modal_header_background.png" alt=""> -->
          <h2 class="text-xl font-bold"><slot name="header">{{ title }}</slot></h2>
          <button 
            class="nes-btn is-normal absolute top-5 right-4 bg-white text-gray-500 text-2xl w-8 h-8 flex items-center justify-center shadow-md leading-none"
            @click="$emit('close-modal')"
          >
            ×
          </button>
        </div>
        
        <!-- 컨텐츠 영역 -->
        <div 
          class="flex-1 overflow-y-auto p-[7vw]"
          :style="{ 
            backgroundImage: `url(${require('@/assets/images/modal_body_background.png')})`, 
            backgroundSize: '100% 100%',  /* 부모 요소에 딱 맞게 채움 (잘리는 부분 없음) */
            backgroundRepeat: 'no-repeat', /* 반복 방지 */
            backgroundPosition: 'center' /* 중앙 정렬 */
          }"
        >
          <slot></slot>
        </div>
      </div>
    </div>
  </Teleport>
</template>
  
<script setup>
defineProps({
  title: {
    type: String,
    default: ''
  }
})

defineEmits(['close-modal'])
</script>

<style scoped>
img {
  image-rendering: pixelated;
}
</style>