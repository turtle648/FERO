<template>
  <div class="fitness-modal">
    <button id="close-btn" @click="closeFitnessModal">X</button>
    <div>연습하기</div>
    <div>빠른매칭</div>
    <div>친구초대</div>
  </div>
</template>

<script setup>
import { defineEmits } from "vue"

const emit = defineEmits(["closeFitness"])
const closeFitnessModal = () => {
  emit("closeFitness")
}
</script>

<style scoped>
.fitness-modal {
  width: 40vh;
  height: 60vh;
  position: fixed;
  top: 50%; /* 화면의 세로 중앙 */
  left: 50%; /* 화면의 가로 중앙 */
  transform: translate(-50%, -50%); /* 자신의 크기만큼 반으로 이동 */
  background: rgba(255, 255, 255);
  display: flex;
  flex-direction: column; /* 세로 방향 정렬 */
  justify-content: center; /* 세로 방향으로 중앙 정렬 */
  align-items: center; /* 가로 방향으로 중앙 정렬 */
}

.fitness-modal div {
  margin-bottom: 10px; /* 각 div 사이 간격 추가 */
}

#close-btn {
  position: absolute;
  top: 2%;
  right: 2%;
}
</style>
