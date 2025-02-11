<template>
  <div class="alarm-modal">
    <div class="content">
      <button id="close-btn" @click="closeAlarmModal">X</button>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from "vue"

// 모달 닫기
const emit = defineEmits(['close-modal'])
const closeAlarmModal = () => {
  emit('close-modal')
}
</script>

<style scoped>
.alarm-modal {
  width: 40vh;
  height: 60vh;
  position: fixed;
  top: 50%; /* 화면의 세로 중앙 */
  left: 50%; /* 화면의 가로 중앙 */
  transform: translate(-50%, -50%); /* 자신의 크기만큼 반으로 이동 */
  background: rgba(255, 255, 255);
  display: flex;
}

.content {
  width: 100%;
  position: relative;
  text-align: center;
}

#close-btn {
  position: absolute;
  right: 3%;
  top: 2%;
  z-index: 11;
}
</style>
