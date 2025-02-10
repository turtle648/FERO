<template>
    <div class="fitness-modal">
      <button id="close-btn" @click="closeWithModal">X</button>
      <!-- 같이하기 모달의 내용을 여기에 추가 -->
    </div>
  </template>
  
  <script setup>
  import { defineEmits } from "vue"
  
  const emit = defineEmits(["closeWith"])
  const closeWithModal = () => {
    emit("closeWith")
  }
  </script>
  
  <style scoped>
  .fitness-modal {
    width: 40vh;
    height: 60vh;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: rgba(255, 255, 255);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  #close-btn {
    position: absolute;
    top: 2%;
    right: 2%;
  }
  </style>