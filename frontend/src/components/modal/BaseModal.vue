<!-- components/modal/BaseModal.vue -->
<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white w-[75%] h-[70%] max-w-4xl max-h-[80vh] rounded-lg shadow-lg flex flex-col">
        <!-- 헤더 영역 -->
        <div class="flex justify-between items-center p-4 border-b">
          <h2 class="text-xl font-bold"><slot name="header">{{ title }}</slot></h2>
          <button 
            class="text-gray-500 hover:text-gray-700 text-2xl focus:outline-none" 
            @click="$emit('close-modal')"
          >
            ×
          </button>
        </div>
        
        <!-- 컨텐츠 영역 -->
        <div class="flex-1 overflow-y-auto p-4">
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
  