<template>
    <div>
        <input type="text" v-model=roomId placeholder="방 번호를 입력하세요">
        <button v-on:click="clickSubmitRoomId">생성하기</button>
        <VideoComponent v-if="submittedRoomId!=''" :room-id="submittedRoomId"/>
    </div>
</template>
  
<script setup>
import VideoComponent from '@/components/videoroom/VideoComponent.vue';
import { ref } from 'vue'

const roomId = ref('');
const submittedRoomId = ref('');

function clickSubmitRoomId () {
    submittedRoomId.value = roomId.value;
    console.log("📜 방 번호 ID Video Component 전달11: " + submittedRoomId.value);
}


</script>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style>
