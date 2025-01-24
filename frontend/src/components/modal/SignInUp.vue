<template>
    <div class="modal" @click="closeModalOutside">
      <div class="modal-content">
        <h3>Sign In / Sign Up</h3>
        <!-- 닫기 버튼 -->
        <button @click="close">Close</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineEmits } from 'vue'
  
  // 모달을 닫을 때 부모 컴포넌트에 이벤트를 보내는 emit 정의
  const emit = defineEmits(['close'])
  
  // StartPage에서 모달 닫기 처리
  const close = () => { emit('close') }

  // 배경 클릭 시 모달 닫기 처리
  const closeModalOutside = () => { emit('close') }
  </script>
  
  <style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
  }
  </style>
  