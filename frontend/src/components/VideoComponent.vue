<!-- @/components/VideoComponent.vue -->
<template>
  <div class="video-container" :class="{ 'fullscreen': isFullscreen }">
    <video 
      ref="videoElement"
      autoplay 
      playsinline
      muted
      class="video-element"
    ></video>
    
    <!-- 연결 상태 표시 -->
    <div v-if="connectionStatus !== 'connected'" class="connection-status">
      {{ connectionStatusMessage }}
    </div>

    <!-- 컨트롤 버튼 -->
    <div class="video-controls">
      <button @click="toggleFullscreen">
        {{ isFullscreen ? '작게보기' : '전체화면' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed, defineProps } from 'vue'
import { useMatchStore } from '@/stores/matchStore'

const props = defineProps({
  peerId: {
    type: String,
    required: true
  },
  isInitiator: {
    type: Boolean,
    default: false
  }
})

const videoElement = ref(null)
const isFullscreen = ref(false)
const connectionStatus = ref('initializing') // 'initializing', 'connecting', 'connected', 'error'
const matchStore = useMatchStore()

// WebRTC 설정
const peerConnection = ref(null)
const configuration = {
  iceServers: [
    { urls: 'stun:stun.l.google.com:19302' }
  ]
}

// 연결 상태 메시지
const connectionStatusMessage = computed(() => {
  switch (connectionStatus.value) {
    case 'initializing':
      return '비디오 초기화 중...'
    case 'connecting':
      return '연결 중...'
    case 'error':
      return '연결 실패'
    default:
      return ''
  }
})

// 에러 처리 함수
const handleError = (error) => {
  console.error('WebRTC 에러:', error)
  connectionStatus.value = 'error'
  // 필요한 경우 추가적인 에러 처리
}

// WebRTC 연결 초기화
const initializeWebRTC = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: false
    })

    if (videoElement.value) {
      videoElement.value.srcObject = stream
    }

    peerConnection.value = new RTCPeerConnection(configuration)
    matchStore.webrtcConnection = peerConnection.value

    // 연결 상태 모니터링
    peerConnection.value.onconnectionstatechange = () => {
      console.log('Connection State:', peerConnection.value.connectionState)
      switch(peerConnection.value.connectionState) {
        case 'connected':
          connectionStatus.value = 'connected'
          break
        case 'disconnected':
        case 'failed':
          connectionStatus.value = 'error'
          retryConnection()
          break
      }
    }

    // ICE candidate 이벤트 처리
    peerConnection.value.onicecandidate = event => {
      if (event.candidate) {
        matchStore.sendWebRTCData({
          type: 'candidate',
          candidate: event.candidate
        })
      }
    }

    // 원격 스트림 수신 처리
    peerConnection.value.ontrack = event => {
      if (videoElement.value) {
        videoElement.value.srcObject = event.streams[0]
      }
    }

    // 스트림 트랙 추가
    stream.getTracks().forEach(track => {
      peerConnection.value.addTrack(track, stream)
    })

    // Offer/Answer 처리
    if (props.isInitiator) {
      const offer = await peerConnection.value.createOffer()
      await peerConnection.value.setLocalDescription(offer)
      matchStore.sendWebRTCData({
        type: 'offer',
        sdp: offer
      })
    }

    connectionStatus.value = 'connecting'
  } catch (error) {
    handleError(error)
  }
}

// 재연결 시도 함수
const retryConnection = async () => {
  try {
    await initializeWebRTC()
  } catch (error) {
    console.error('재연결 실패:', error)
    setTimeout(retryConnection, 5000)
  }
}

// 전체화면 토글
const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
}

onMounted(() => {
  initializeWebRTC()
})

onBeforeUnmount(() => {
  // 연결 정리
  if (peerConnection.value) {
    peerConnection.value.close()
  }
  // 비디오 스트림 정리
  if (videoElement.value && videoElement.value.srcObject) {
    videoElement.value.srcObject.getTracks().forEach(track => track.stop())
  }
})
</script>

<style scoped>
.video-container {
  @apply relative w-[300px] h-[200px] bg-black rounded-lg overflow-hidden;
  transition: all 0.3s ease;
}

.video-container.fullscreen {
  @apply fixed top-0 left-0 w-screen h-screen z-50;
}

.video-element {
  @apply w-full h-full object-cover;
}

.connection-status {
  @apply absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2
         bg-black bg-opacity-70 text-white px-4 py-2 rounded-lg;
}

.video-controls {
  @apply absolute bottom-4 right-4 flex gap-2;
}

.video-controls button {
  @apply px-3 py-1 bg-white bg-opacity-80 rounded-lg text-sm
         hover:bg-opacity-100 transition-all duration-200;
}
</style>
