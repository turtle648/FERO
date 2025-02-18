<!-- components/modal/BaseModal.vue -->
<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white w-[75%] h-[70%] max-w-4xl max-h-[80vh] rounded-lg shadow-lg flex flex-col">
        <!-- 헤더 영역 -->
        
        <div 
          class="relative w-full h-[10vh] flex justify-between items-center pl-[4vw] pr-[4vw] text-white text-xl font-bold"
          :style="{ 
            backgroundImage: `url(${require('@/assets/images/modal_header_background.png')})`, 
            backgroundSize: '100% 100%',  /* 부모 요소에 완전히 맞춤 (잘리는 부분 없음) */
            backgroundRepeat: 'no-repeat', /* 반복 방지 */
            backgroundPosition: 'center' /* 중앙 정렬 */
          }"
        >
          <!-- <img class="w-full" src="@/assets/images/modal_header_background.png" alt=""> -->
          <h2 class="font-bold" style="font-size: 2vh;"><slot name="header">{{ title }}</slot></h2>
          <button 
            class="nes-btn is-normal bg-white text-gray-500 flex items-center justify-center shadow-md leading-none"
            style="width:2vh; height:2vh; font-size: 2vh;"
            @click="$emit('close-modal')"
          >
            ×
          </button>
        </div>
        
        <!-- 컨텐츠 영역 -->
        <div 
          class="flex-1 overflow-y-auto pl-[7vw] pr-[7vw] pt-[2vh] pb-[4vh] max-h-[80vh] min-h-0"
          :style="{ 
            backgroundImage: `url(${require('@/assets/images/modal_body_background.png')})`, 
            backgroundSize: '100% 100%',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center'
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