<template>
    <div>
        <button v-on:click="clickSubmitRoomId">매치시작</button>
        <VideoComponent v-if="submittedRoomId" class="fixed inset-0 overflow-hidden"/>
    </div>
</template>
  
<script setup>
import VideoComponent from '@/components/videoroom/VideoComponent.vue';
import { ref } from 'vue'

const submittedRoomId = ref(false);

const clickSubmitRoomId = (isClicked) => {
    submittedRoomId.value = isClicked;
}

</script>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style> 
