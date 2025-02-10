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
  import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
  import { useMatchStore } from '@/stores/matchStore'
  
  const props = defineProps({
    peerId: {
      type: String,
      required: true
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
  
  // WebRTC 연결 초기화
  const initializeWebRTC = async () => {
    try {
      // 로컬 비디오 스트림 가져오기
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
        audio: false // 오디오는 비활성화
      })
  
      // 비디오 요소에 스트림 연결
      if (videoElement.value) {
        videoElement.value.srcObject = stream
      }
  
      // RTCPeerConnection 설정
      peerConnection.value = new RTCPeerConnection(configuration)
      
      // 스트림 트랙 추가
      stream.getTracks().forEach(track => {
        peerConnection.value.addTrack(track, stream)
      })
  
      // ICE candidate 이벤트 처리
      peerConnection.value.onicecandidate = event => {
        if (event.candidate) {
          // WebSocket을 통해 상대방에게 candidate 전송
          matchStore.sendWebRTCData({
            type: 'candidate',
            candidate: event.candidate,
            peerId: props.peerId
          })
        }
      }
  
      connectionStatus.value = 'connected'
    } catch (error) {
      console.error('WebRTC 초기화 실패:', error)
      connectionStatus.value = 'error'
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
  