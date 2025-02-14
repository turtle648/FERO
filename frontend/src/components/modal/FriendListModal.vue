<template>
  <div class="friend-list-modal w-[40vh] h-[60vh] fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white flex">
    <div class="content w-full realtive">
      <div class="button-container z-20">
        <button class="absolute right-3 top-2 z-20" id="close-btn" @click="closeFriendModal">X</button>
      </div>

      <div class="menu">
        <div class="friend" @click="showList('friend')">친구목록</div>
        <div class="chatting" @click="showList('chatting')">채팅목록</div>
      </div>

      <!-- 친구목록 -->
      <div class="friend-list-container" v-if="activeList === 'friend'">
        <div>
          <li v-for="friend in friendList" :key="friend.id">{{ friend.nickname }} (Level: {{ friend.level }})</li>
        </div>
      </div>

      <!-- 채팅목록 -->
      <div class="chatting-list-container" v-if="activeList === 'chatting'"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted } from "vue"
import { useFriendStore } from "@/stores/friendStore"

const friendStore = useFriendStore()

// 친구목록 데이터
const friendList = ref([])

// 모달 닫기
const emit = defineEmits(["close-modal"])
const closeFriendModal = () => {
  emit("close-modal")
}

// 친구목록, 채팅목록 상태 관리
const activeList = ref("friend") // 기본값으로 친구목록
const showList = (list) => {
  activeList.value = list
}

// 친구목록 데이터 가져오기
const fetchFriendList = async () => {
  try {
    const response = await friendStore.getFriendList()

    if (response && response.length > 0) {
      // 데이터가 존재하면 저장
      friendList.value = response
    } else {
      // 데이터가 없으면 빈 배열로 초기화
      friendList.value = []
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchFriendList()
})
</script>

<style scoped>
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

.friend-list-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: paleturquoise;
}

.chatting-list-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: pink;
}
</style>
