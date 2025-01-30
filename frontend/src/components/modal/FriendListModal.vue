<template>
  <div class="friend-list-modal">
    <div class="content">
      <button id="close-btn" @click="closeFriendModal">X</button>

      <div class="menu">
        <div class="friend" @click="showList('friend')">친구목록</div>
        <div class="chatting" @click="showList('chatting')">채팅목록</div>
      </div>

      <!-- 친구목록 -->
      <div class="friend-list" v-if="activeList === 'friend'"></div>

      <!-- 채팅목록 -->
      <div class="chatting-list" v-if="activeList === 'chatting'"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue"

// 모달 닫기
const emit = defineEmits(["closeFriend"])
const closeFriendModal = () => {
  emit("closeFriend")
}

// 친구목록, 채팅목록 상태 관리
const activeList = ref("friend") // 기본값으로 친구목록
const showList = (list) => {
  activeList.value = list
}
</script>

<style scoped>
.friend-list-modal {
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

.menu {
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px; /* 아래 콘텐츠와 간격 추가 */
}

.friend {
  position: relative;
  z-index: 10;
}

.chatting {
  position: relative;
  z-index: 10;
}

.friend-list {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: paleturquoise;
}

.chatting-list {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: pink;
}
</style>
