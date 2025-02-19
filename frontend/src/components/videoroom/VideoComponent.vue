<template>
  <div class="video-container">
    <SquatComponent
      v-show="isMyVideoOn"
      class="my-video z-50"
      ref="myFace"
      @set-count="setCount"
      @get-time-left="setTime"
      :command="command"
    />

    <div class="peer-container" :class="{ hidden: isClose }">
      <MediapipeOnlyComponent
        ref="peerVideo"
        class="peer-video z-20"
        :peerStream="peerStream"
      />
    </div>

    <div class="controls">
      <button @click="toggleMyVideo" class="control-btn">
        {{ isMyVideoOn ? "화면 끄기" : "화면 켜기" }}
      </button>

      <div class="audio-controls">
        <button @click="toggleMyAudio" class="control-btn">
          {{ isMyAudioOn ? "내 마이크 끄기" : "내 마이크 켜기" }}
        </button>
        <button @click="togglePeerAudio" class="control-btn">
          {{ isPeerAudioOn ? "상대방 소리 끄기" : "상대방 소리 켜기" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  ref,
  onMounted,
  onBeforeUnmount,
  defineEmits,
  defineProps,
  watch,
} from "vue";
import SquatComponent from "@/components/SquatComponent.vue";
import axios from "axios";
import MediapipeOnlyComponent from "@/components/MediapipeOnlyComponent.vue";
// import router from '@/router'
// import { setRouteData } from '@/router/routeData'
// 기본 상태 관리
const isMyVideoOn = ref(true);
const isMyAudioOn = ref(true);
const isPeerAudioOn = ref(true);
const needToSendFinal = ref(false);
const isMyExerciseComplete = ref(false);
const isPeerExerciseComplete = ref(false);
const isClose = ref(false);

const emit = defineEmits(["setIsMatched"]);
const props = defineProps(["exercise"]);
const count = ref(0);
const peerCount = ref(0);
const time = ref(-1);
const myFace = ref(null);
const peerVideo = ref(null);
const peerStream = ref(null);
const roomId = ref(null);
const currentPeerId = ref(null);
const peerToken = ref(null);
const command = ref("");

let webSocket;
let myStream;
let myPeerConnection = null;

// 운동 카운트 설정
const setCount = (value) => {
  count.value = value;
};

// 시간 설정
const setTime = (value) => {
  time.value = value;
};

// 운동 종료 처리 함수
const finishExercise = () => {
  isMyExerciseComplete.value = true;

  sendMessage({
    type: "exercise_complete",
    receiver: currentPeerId.value,
    myCount: count.value,
  });

  // 상대방도 운동을 완료했다면 방 종료
  if (isPeerExerciseComplete.value) {
    finishRoom();
  }
};

// 방 종료 처리
const finishRoom = () => {
  if (needToSendFinal.value) {
    console.log("상대방의 점수: " + peerCount.value);

    sendMessage({
      type: "final",
      auth: localStorage.getItem("authToken"),
      receiver: currentPeerId.value,
      remainTime: time.value === 0 ? "0" : time.value,
      myCount: count.value === null || count.value === 0 ? "0" : count.value,
      peerCount:
        peerCount.value === null || peerCount.value === 0
          ? "0"
          : peerCount.value,
      exerciseType: props.exercise,
    });
  }

  // 정리 작업은 따로 실행하지 않고 info 메시지를 기다림
  console.log("final 메시지 전송 완료, info 메시지 대기 중");
};

// 정리 및 페이지 이동
const cleanupAndNavigate = (finalRoomId, finalPeerToken) => {
  if (peerVideo.value) {
    peerVideo.value.srcObject = null;
  }
  if (myPeerConnection) {
    isClose.value = true;
    myPeerConnection.close();
    myPeerConnection = null;
  }

  console.log(finalRoomId + ":" + finalPeerToken);

  if (webSocket && webSocket.readyState === WebSocket.OPEN) {
    webSocket.close();
  }

  // setRouteData({
  //   roomId: finalRoomId,
  //   peerToken: finalPeerToken
  // })

  // router.push('/rank-result')
  // (스쿼트)컴포넌트로 데이터를 보냄
  command.value = {
    roomId: finalRoomId,
    peerToken: finalPeerToken,
    remainTime: time.value,
  };
  console.log(command.value);
};

// 비디오/오디오 토글 함수들
const toggleMyVideo = () => {
  isMyVideoOn.value = !isMyVideoOn.value;
  if (myStream) {
    myStream.getVideoTracks().forEach((track) => {
      track.enabled = isMyVideoOn.value;
    });
  }
};

const toggleMyAudio = () => {
  isMyAudioOn.value = !isMyAudioOn.value;
  if (myStream) {
    myStream.getAudioTracks().forEach((track) => {
      track.enabled = isMyAudioOn.value;
    });
  }
};

const togglePeerAudio = () => {
  isPeerAudioOn.value = !isPeerAudioOn.value;
  if (peerVideo.value && peerVideo.value.srcObject) {
    peerVideo.value.srcObject.getAudioTracks().forEach((track) => {
      track.enabled = isPeerAudioOn.value;
    });
  }
};

// 시간 감시
watch(time, (newValue) => {
  console.log("⌛ Time updated:", newValue);
  if (time.value === 0) {
    finishExercise();
  }
});

// 운동 카운트 감시
watch(count, (newCount) => {
  console.log("👟 운동 개수 증가: " + newCount);
  sendMessage({
    type: "count",
    myCount: count.value,
    receiver: currentPeerId.value,
  });
});

// WebSocket 메시지 전송
const sendMessage = (message) => {
  if (webSocket && webSocket.readyState === WebSocket.OPEN) {
    webSocket.send(JSON.stringify(message));
  }
};

// WebSocket 메시지 핸들러
const handleWebSocketMessage = async (event) => {
  const message = JSON.parse(event.data);

  switch (message.type) {
    case "info": {
      console.log("info 도착:: " + message.room + "," + message.peerToken);
      console.log("기존의 피어토큰", peerToken.value);
      console.log("info 도착:: 받은 피어토큰", message.peerToken);
      // 첫 번째 info 메시지인 경우
      roomId.value = message.room;
      peerToken.value = message.peerToken || peerToken.value;

      // final 메시지 전송 후의 info 메시지인 경우
      if (needToSendFinal.value === false && roomId.value) {
        console.log("최종 info 메시지 수신, 정리 작업 시작");
        cleanupAndNavigate(message.room, peerToken.value);
        return;
      }

      break;
    }

    case "final": {
      needToSendFinal.value = false; // final 메시지 이후 flag 설정
      break;
    }

    case "all_users": {
      await getMedia();
      initRTCPeerConnection();
      emit("setIsMatched", true);
      message.allUsers.forEach((user) => {
        createOffer(user.id);
      });
      break;
    }

    case "count": {
      peerCount.value = message.peerCount;
      console.log("🏋️‍♀️ 상대방의 점수: " + peerCount.value);
      break;
    }

    case "exercise_complete": {
      isPeerExerciseComplete.value = true;
      console.log("exercise_complete에서 받은 peerToken:", message.peerToken);
      console.log("exercise_complete에서 받은 count:", message.myCount);
      peerCount.value = message.myCount;
      console.log("exercise_complete 후 peerCount:", peerCount.value);

      if (isMyExerciseComplete.value) {
        console.log("finishRoom 직전 peerCount:", peerCount.value);
        finishRoom();
      }
      break;
    }

    case "user_exit": {
      // 상대방이 강제 종료한 경우
      sendMessage({
        type: "final",
        auth: localStorage.getItem("authToken"),
        receiver: currentPeerId.value,
        myCount: count.value === null || count.value === 0 ? "0" : count.value,
        peerCount:
          peerCount.value === null || peerCount.value === 0
            ? "0"
            : peerCount.value,
        remainTime: time.value,
        exerciseType: props.exercise,
      });

      console.log("currentPeerId:", currentPeerId.value);
      console.log("상대방 강제 종료로 인한 final 메시지 전송");
      needToSendFinal.value = false; // info를 기다리도록 flag 설정

      break;
    }

    case "offer": {
      currentPeerId.value = message.sender;

      if (!myPeerConnection) {
        await getMedia();
        initRTCPeerConnection();
      }

      await myPeerConnection.setRemoteDescription(
        new RTCSessionDescription(message.sdp)
      );
      const answer = await myPeerConnection.createAnswer();
      await myPeerConnection.setLocalDescription(answer);

      sendMessage({
        type: "answer",
        sdp: answer,
        receiver: message.sender,
      });
      break;
    }

    case "answer": {
      needToSendFinal.value = true;

      await myPeerConnection.setRemoteDescription(
        new RTCSessionDescription(message.sdp)
      );
      break;
    }

    case "candidate": {
      if (message.candidate && myPeerConnection) {
        try {
          await myPeerConnection.addIceCandidate(
            new RTCIceCandidate(message.candidate)
          );
        } catch (e) {
          console.error("ICE candidate 추가 실패:", e);
        }
      }
      break;
    }

    case "room_full": {
      alert("입장 인원이 초과되었습니다.");
      break;
    }

    default: {
      console.warn("알 수 없는 메시지 타입:", message.type);
      break;
    }
  }
};

// 미디어 스트림 가져오기
const getMedia = async () => {
  try {
    myStream = await navigator.mediaDevices.getUserMedia({
      audio: true,
      video: true,
    });

    if (myFace.value) {
      myFace.value.srcObject = myStream;
    }
  } catch (e) {
    console.error("미디어 스트림 에러:", e);
  }
};

// RTCPeerConnection 초기화
const initRTCPeerConnection = () => {
  const iceServerConfig = {
    iceServers: [
      {
        urls: "turn:43.201.250.216:3478?transport=tcp",
        username: "ssafy",
        credential: "e103",
      },
    ],
  };

  myPeerConnection = new RTCPeerConnection(iceServerConfig);

  if (myStream) {
    myStream.getTracks().forEach((track) => {
      myPeerConnection.addTrack(track, myStream);
    });
  }

  myPeerConnection.onicecandidate = (event) => {
    if (event.candidate) {
      sendMessage({
        type: "candidate",
        candidate: event.candidate,
        receiver: currentPeerId.value,
      });
    }
  };

  myPeerConnection.ontrack = (event) => {
    if (peerVideo.value) {
      peerVideo.value.srcObject = event.streams[0];
      peerStream.value = event.streams[0];
      console.log(peerStream.value);
    }
  };
};

// Offer 생성
const createOffer = async (receiverId) => {
  try {
    currentPeerId.value = receiverId;
    const offer = await myPeerConnection.createOffer();
    await myPeerConnection.setLocalDescription(offer);
    sendMessage({
      type: "offer",
      sdp: offer,
      receiver: receiverId,
    });
  } catch (error) {
    console.error("Offer 생성 중 에러:", error);
  }
};

// 방 입장
async function clickSubmitRoomId() {
  const res = await axios.post(
    "https://i12e103.p.ssafy.io:8076/api/v1/matching/enter",
    null,
    {
      headers: { Authorization: `${localStorage.getItem("authToken")}` },
      params: { exerciseType: props.exercise },
    }
  );

  if (res.status === 200) {
    console.log("📜 매칭시도");
  }
}

// 컴포넌트 마운트
onMounted(() => {
  webSocket = new WebSocket("wss://i12e103.p.ssafy.io:8076/api/v1/videorooms");

  webSocket.onopen = async () => {
    console.log("WebSocket 연결됨");
    sendMessage({
      type: "auth",
      auth: localStorage.getItem("authToken"),
      exerciseType: props.exercise,
    });
    await clickSubmitRoomId();
  };

  webSocket.onmessage = handleWebSocketMessage;
  webSocket.onerror = (error) => {
    console.error("WebSocket 에러:", error);
  };
  webSocket.onclose = () => {
    console.log("WebSocket 연결 종료");
  };
});

// 컴포넌트 언마운트
onBeforeUnmount(() => {
  if (myStream) {
    myStream.getTracks().forEach((track) => track.stop());
  }

  if (myPeerConnection) {
    myPeerConnection.close();
    myPeerConnection = null;
  }

  if (webSocket) {
    webSocket.close();
  }

  currentPeerId.value = null;
});
</script>

<style scoped>
@media (max-width: 768px) {
  video,
  canvas {
    object-fit: contain;
  }
}

@media (min-width: 769px) {
  video,
  canvas {
    object-fit: cover;
  }
}

.video-container {
  @apply relative w-full h-screen bg-black;
}

.my-video {
  @apply w-full h-full object-contain relative z-10;
}

.peer-container {
  @apply absolute top-[15%] right-[5%] w-1/5 aspect-[9/16] z-20;
}

.peer-video {
  @apply w-full h-full object-contain;
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

.hidden {
  z-index: -1;
}
</style>
