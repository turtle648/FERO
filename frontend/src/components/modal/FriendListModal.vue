<template>
  <div class="friend-list-modal w-[40vh] h-[60vh] fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex">
    <div class="content w-full relative">
      <div class="button-container">
        <button class="absolute right-3 top-2 z-20" id="close-btn" @click="closeFriendModal">X</button>
      </div>

      <div class="menu flex justify-around h-[7vh] bg-white">
        <div class="content menu-size" @click="showList('friend')">친구목록</div>
        <div class="content menu-size" @click="showList('chatting')">채팅목록</div>
      </div>

      <!-- 친구목록 -->
      <div class="friend-list list-container" v-if="activeList === 'friend'">
        <ul>
          <!-- <li v-for="friend in friendList" :key="friend.id">{{ friend.nickname }} (Level: {{ friend.level }})</li> -->
          <li class="content">Lv.2 닉네임 (thscodl04)</li>
          <li class="content">Lv.2 닉네임 (thscodl04)</li>
          <li class="content">Lv.2 닉네임 (thscodl04)</li>
          <li class="content">Lv.2 닉네임 (thscodl04)</li>
          <li class="content">Lv.2 닉네임 (thscodl04)</li>
        </ul>
      </div>

      <!-- 채팅목록 -->
      <div class="chatting-list list-container" v-if="activeList === 'chatting'">
        <ul>
          <li class="content">채팅</li>
          <li class="content">채팅</li>
          <li class="content">채팅</li>
          <li class="content">채팅</li>
        </ul>
      </div>
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
.content {
  @apply h-[6vh];
}

.menu-size {
  @apply relative z-10 w-full h-[5vh];
}
.list-container {
  @apply w-full h-[53vh] overflow-y-auto bg-blue-500;
}
</style>
