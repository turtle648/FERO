<!-- WebRTC.vue -->
<template>
  <div class="video-container">
    <!-- 나의 화면 (전체 화면) -->

    <!-- ver1: 그냥 카메라로 내 화면 띄우기기 -->
    <!-- <video 
      ref="myFace" 
      v-show="isMyVideoOn"
      class="my-video" 
      playsinline 
      autoplay
    ></video> -->

    <!-- ver2: 스쿼트 컴포넌트 갖고오기기 -->
    <SquatComponent
      v-show="isMyVideoOn"
      class="my-video"
      ref="myFace"
    />

    <!-- ver3: 운동모달에서 선택한 운동 컴포넌트 띄우기기 -->

    <!-- 상대방 화면 (우측 상단) -->
    <div class="peer-container">
      <video 
        ref="peerVideo" 
        class="peer-video" 
        playsinline 
        autoplay
      ></video>
    </div>

    <!-- 컨트롤 버튼 영역 -->
    <div class="controls">
      <button 
        @click="toggleMyVideo" 
        class="control-btn"
      >
        {{ isMyVideoOn ? '화면 끄기' : '화면 켜기' }}
      </button>
      
      <div class="audio-controls">
        <button 
          @click="toggleMyAudio" 
          class="control-btn"
        >
          {{ isMyAudioOn ? '내 마이크 끄기' : '내 마이크 켜기' }}
        </button>
        <button 
          @click="togglePeerAudio" 
          class="control-btn"
        >
          {{ isPeerAudioOn ? '상대방 소리 끄기' : '상대방 소리 켜기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.video-container {
  @apply relative w-full h-screen bg-black;
}

.my-video {
  @apply w-full h-full object-cover relative z-10;
}

.peer-container {
  @apply absolute top-[15%] right-[5%] w-1/5 aspect-[9/16] z-20;
}

.peer-video {
  @apply w-full h-full object-cover;
}

.controls {
  @apply fixed bottom-5 left-5 flex flex-col-reverse gap-2.5 z-20;
}

.audio-controls {
  @apply flex flex-col gap-2.5;
}

.control-btn {
  @apply px-5 py-2.5 
         rounded cursor-pointer 
         bg-black/50 text-white 
         border border-white/30 
         font-semibold 
         min-w-[180px]
         transition-colors duration-300 ease-in-out
         shadow-lg;
}

.control-btn:hover {
  @apply bg-black/70 border-white/50;
}
</style>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps } from 'vue'

// ver2
import SquatComponent from '@/components/SquatComponent.vue'

// 초기 상태를 false로 설정
const isMyVideoOn = ref(true)
const isMyAudioOn = ref(false)  // 변경
const isPeerAudioOn = ref(false)  // 변경

const toggleMyVideo = () => {
  isMyVideoOn.value = !isMyVideoOn.value
  if (myStream) {
    myStream.getVideoTracks().forEach(track => {
      track.enabled = isMyVideoOn.value
    })
  }
}

const toggleMyAudio = () => {
  isMyAudioOn.value = !isMyAudioOn.value
  if (myStream) {
    myStream.getAudioTracks().forEach(track => {
      track.enabled = isMyAudioOn.value
    })
  }
}

const togglePeerAudio = () => {
  isPeerAudioOn.value = !isPeerAudioOn.value
  if (peerVideo.value && peerVideo.value.srcObject) {
    peerVideo.value.srcObject.getAudioTracks().forEach(track => {
      track.enabled = isPeerAudioOn.value
    })
  }
}


const props = defineProps(['roomId'])
const myFace = ref(null)
const peerVideo = ref(null)
let webSocket
let myStream
let currentPeerId = ref(null) // 현재 연결된 Peer의 ID를 저장

// ICE 서버 설정 - TURN 서버 포함
const iceServerConfig = {
  iceServers: [{
    urls: 'turn:43.201.250.216:3478?transport=tcp',
    username: 'ssafy',
    credential: 'e103'
  }]
}

// RTCPeerConnection 설정
let myPeerConnection = null

// 미디어 스트림 가져오기 함수 수정
const getMedia = async () => {
  try {
    myStream = await navigator.mediaDevices.getUserMedia({
      audio: true,
      video: true
    })
    
    // 오디오 트랙 초기 상태를 비활성화
    myStream.getAudioTracks().forEach(track => {
      track.enabled = false
    })
    
    if (myFace.value) {
      myFace.value.srcObject = myStream
    }
  } catch (e) {
    console.error("미디어 스트림 에러:", e)
  }
}

// WebSocket 메시지 전송
const sendMessage = (message) => {
  if (webSocket && webSocket.readyState === WebSocket.OPEN) {
    webSocket.send(JSON.stringify(message))
  }
}

// Offer 생성 및 전송
const createOffer = async (receiverId) => {
  try {
    currentPeerId.value = receiverId // 현재 연결하려는 Peer의 ID 저장
    const offer = await myPeerConnection.createOffer()
    await myPeerConnection.setLocalDescription(offer)
    
    sendMessage({
      type: "offer",
      sdp: offer,
      room: props.roomId,
      receiver: receiverId
    })
  } catch (error) {
    console.error("Offer 생성 중 에러:", error)
  }
}

// WebRTC 연결 초기화
const initRTCPeerConnection = () => {
  myPeerConnection = new RTCPeerConnection(iceServerConfig)

  // 미디어 트랙 추가
  if (myStream) {
    myStream.getTracks().forEach(track => {
      myPeerConnection.addTrack(track, myStream)
    })
  }

  // ICE candidate 이벤트
  myPeerConnection.onicecandidate = (event) => {
    if (event.candidate) {
      sendMessage({
        type: "candidate",
        candidate: event.candidate,
        room: props.roomId,
        receiver: currentPeerId.value // 현재 연결된 Peer의 ID를 receiver로 전송
      })
    }
  }

  // 스트림 추가 이벤트
  myPeerConnection.ontrack = (event) => {
    if (peerVideo.value) {
      peerVideo.value.srcObject = event.streams[0]
    }
  }
}

// WebSocket 메시지 핸들러
const handleWebSocketMessage = async (event) => {
  const message = JSON.parse(event.data)
  
  switch (message.type) {
    case "all_users": {
      await getMedia()
      initRTCPeerConnection()
      
      // 기존 유저들에게 offer 보내기
      message.allUsers.forEach(user => {
        createOffer(user.id)
      })
      break
    }

    case "offer": {
      currentPeerId.value = message.sender // offer를 보낸 Peer의 ID 저장
      
      if (!myPeerConnection) {
        await getMedia()
        initRTCPeerConnection()
      }
      
      await myPeerConnection.setRemoteDescription(new RTCSessionDescription(message.sdp))
      
      const answer = await myPeerConnection.createAnswer()
      await myPeerConnection.setLocalDescription(answer)
      
      sendMessage({
        type: "answer",
        sdp: answer,
        room: props.roomId,
        receiver: message.sender
      })
      break
    }

    case "answer": {
      await myPeerConnection.setRemoteDescription(new RTCSessionDescription(message.sdp))
      break
    }

    case "candidate": {
      if (message.candidate && myPeerConnection) {
        try {
          await myPeerConnection.addIceCandidate(new RTCIceCandidate(message.candidate))
        } catch (e) {
          console.error("ICE candidate 추가 실패:", e)
        }
      }
      break
    }

    case "room_full": {
      alert("입장 인원이 초과되었습니다.")
      break
    }

    case "user_exit": {
      if (peerVideo.value) {
        peerVideo.value.srcObject = null
      }
      if (myPeerConnection) {
        myPeerConnection.close()
        myPeerConnection = null
      }
      currentPeerId.value = null // Peer ID 초기화
      break
    }

    default: {
      console.warn("알 수 없는 메시지 타입:", message.type)
      break
    }
  }
}

// 컴포넌트 마운트 시 실행
onMounted(async () => {
  // WebSocket 연결
  webSocket = new WebSocket('wss://i12e103.p.ssafy.io:8076/api/v1/videorooms')
  
  webSocket.onopen = () => {
    console.log("WebSocket 연결됨")
    // 방 입장 메시지 전송
    sendMessage({
      type: "join_room",
      room: props.roomId
    })
  }

  webSocket.onmessage = handleWebSocketMessage
  webSocket.onerror = (error) => {
    console.error("WebSocket 에러:", error)
  }
  webSocket.onclose = () => {
    console.log("WebSocket 연결 종료")
  }
})

// 컴포넌트 언마운트 시 실행
onBeforeUnmount(() => {
  if (myStream) {
    myStream.getTracks().forEach(track => track.stop())
  }
  
  if (myPeerConnection) {
    myPeerConnection.close()
    myPeerConnection = null
  }
  
  if (webSocket) {
    webSocket.close()
  }

  currentPeerId.value = null
})
</script>