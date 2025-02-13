<template>
  <div>
    {{ roomId }} {{ peerToken }} 결과입니다
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getRouteData } from '@/router/routeData'

const roomId = ref("")
const peerToken = ref("")

onMounted(() => {
  const data = getRouteData()
  if (data) {
    roomId.value = data.roomId
    peerToken.value = data.peerToken
  }
})
</script>
