<template>
  <div class="setting-modal">
    <div class="content">
      <button id="close-btn" @click="closeSettingModal">X</button>
      <ul class="menu-list">
        <li class="title">설정</li>
        <li>설정에 들어올 정보들</li>
        <li>회원정보</li>
        <li>회원탈퇴</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from "vue"

const emit = defineEmits(["closeSetting"])
const closeSettingModal = () => {
  emit("closeSetting")
}
</script>

<style scoped>
.setting-modal {
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
  text-align: center;
}

#close-btn {
  position: fixed;
  right: 3%;
  top: 2%;
}

.title {
  margin-bottom: 20px;
  font-size: 3vh;
}

.menu-list {
  list-style-type: none;
  padding: 0;
}

.menu-list li {
  margin-bottom: 10px;
}
</style>
