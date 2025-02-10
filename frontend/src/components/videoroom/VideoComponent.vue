<template>
    <div>
        <h1>WebRTC &amp; Spring WebSocket Signaling</h1>
        <button @click="createOffer">Connection</button>
        <br />
        <div>나</div>
        <video ref="myFace" playsinline autoplay width="300" height="300"></video>
        <br />
        <div>상대</div>
        <div id="peerZone" style="width:1280px; height:720px; margin:0; padding:0;">
            <video ref="peerVideo" playsinline autoplay width="1280" height="720"></video>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps } from 'vue'

const props = defineProps(['roomId']);

/* ===== DOM 참조 ===== */
const myFace = ref(null)
const peerVideo = ref(null)

/* ===== 미디어 및 연결 관련 변수 ===== */
const myStream = ref(null)
let ws = null  // WebSocket 객체
const userUUID = ref(null)  // 클라이언트에서 생성한 고유 ID (서버는 session.getId() 사용)
const room = ref(null)  // 방 ID
const remoteUser = ref(null) // 상대방의 ID (all_users 메시지를 통해 획득)

/* ===== ICE 서버 설정 (STUN + TURN) ===== */
const iceServerConfig = {
    iceServers: [
        { 
            urls: 'turn:13.125.110.247:3478?transport=tcp', 
            username: 'ssafy', 
            credential: 'e103' 
        }
    ]
}

/* RTCPeerConnection 생성 */
const myPeerConnection = ref(new RTCPeerConnection(iceServerConfig))

/* ===== 미디어 스트림 획득 ===== */
async function getMedia() {
    try {
        myStream.value = await navigator.mediaDevices.getUserMedia({
            audio: true,
            video: true
        })
        if (myFace.value) {
            myFace.value.srcObject = myStream.value
        }
    } catch (error) {
        console.error("미디어 장치 접근 실패:", error)
    }
}

/* ===== WebSocket 메시지 전송 ===== */
function send(message) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify(message))
    }
}

/* ===== Offer 생성 버튼 클릭 ===== */
async function createOffer(event) {
    event.preventDefault();
    
    // 미디어 스트림 획득 및 RTCPeerConnection에 트랙 추가
    await getMedia()
    myStream.value.getTracks().forEach(track => myPeerConnection.value.addTrack(track, myStream.value))

    // offer SDP 생성
    const offer = await myPeerConnection.value.createOffer()
    await myPeerConnection.value.setLocalDescription(offer)

    // 상대방이 이미 존재하는 경우 offer 전송
    if (remoteUser.value) {
        send({
            type: "offer",
            sdp: offer,
            receiver: remoteUser.value, // 상대방 ID (서버에 저장된 세션 id와 일치해야 함)
            sender: userUUID.value,
            room: room.value
        })
        console.log("Offer 전송:", offer)
    } else {
        console.warn("전송할 상대방 정보가 없습니다.")
    }
}

/* ===== 수신 메시지 처리 ===== */
function handleMessage(message) {
    const type = message.type
    if (type === "all_users") {
        // 방 입장 후 서버에서 본인을 제외한 기존 유저 목록 전달
        console.log("all_users 메시지:", message.allUsers)
        if (message.allUsers && message.allUsers.length > 0) {
        // 여기서는 첫 번째 유저를 상대방으로 선택 (복수 피어일 경우 추가 로직 필요)
            remoteUser.value = message.allUsers[0].id
        }
    } else if (type === "offer") {
        console.log("Offer 수신:", message.sdp)
        handleOffer(message)
    } else if (type === "answer") {
        console.log("Answer 수신:", message.sdp)
        handleAnswer(message)
    } else if (type === "candidate") {
        console.log("Candidate 수신:", message.candidate)
        handleCandidate(message)
    } else if (type === "user_exit") {
        console.log("상대 피어 퇴장:", message.sender)
        // 필요한 경우 화면 처리 등 추가
    } else if (type === "room_full") {
        alert("방이 가득 찼습니다.")
    } else {
        console.warn("알 수 없는 메시지 타입:", type)
    }
}

/* ===== Offer 수신 처리 ===== */
async function handleOffer(message) {
    await myPeerConnection.value.setRemoteDescription(new RTCSessionDescription(message.sdp))
    if (!myStream.value) {
        await getMedia()
        myStream.value.getTracks().forEach(track => myPeerConnection.value.addTrack(track, myStream.value))
    }
    const answer = await myPeerConnection.value.createAnswer()
    await myPeerConnection.value.setLocalDescription(answer)
    send({
        type: "answer",
        sdp: answer,
        receiver: message.sender,
        sender: userUUID.value,
        room: room.value
    })
}

/* ===== Answer 수신 처리 ===== */
async function handleAnswer(message) {
    await myPeerConnection.value.setRemoteDescription(new RTCSessionDescription(message.sdp))
}

/* ===== ICE Candidate 수신 처리 ===== */
function handleCandidate(message) {
    const candidate = new RTCIceCandidate(message.candidate)
    myPeerConnection.value.addIceCandidate(candidate)
}

/* ===== WebSocket 연결 및 이벤트 설정 ===== */
onMounted(() => {
    room.value = props.roomId;

    // 서버에 연결할 때는 실제 서버 주소와 포트를 사용 (예제에서는 localhost:8080)
    ws = new WebSocket("wss://i12e103.p.ssafy.io:8076/api/v1/videorooms")

    ws.onopen = () => {
        console.log("WebSocket 연결 성공")
        // 클라이언트 고유 식별자 생성 (서버에서는 session.getId() 사용)
        userUUID.value = generateUUID()
        // 방 입장을 위한 메시지 전송 (SignalingHandler에서 MSG_TYPE_JOIN 은 "join_room"으로 처리)
        send({
            sender: userUUID.value,
            type: "join_room",
            room: room.value,
        })
    }

    ws.onmessage = (event) => {
        const data = JSON.parse(event.data)
        handleMessage(data)
    }

    ws.onerror = (error) => {
        console.error("WebSocket 에러:", error)
    }

    ws.onclose = () => {
        console.log("WebSocket 연결 종료")
    }

    /* ===== RTCPeerConnection 이벤트 설정 ===== */
    // ICE Candidate 생성 시 signaling 서버로 전송
    myPeerConnection.value.onicecandidate = (event) => {
        if (event.candidate) {
            send({
                type: "candidate",
                candidate: event.candidate,
                receiver: remoteUser.value,  // 상대방 ID (이미 저장되어 있어야 함)
                sender: userUUID.value,
                room: room.value
            })
        }
    }

    // 원격 스트림 수신 시 화면에 출력
    myPeerConnection.value.ontrack = (event) => {
        console.log("📹 원격 스트림 수신:", event.streams[0].getTracks());

        if (peerVideo.value) {
            peerVideo.value.srcObject = event.streams[0]
        }
    }
})

onBeforeUnmount(() => {
    if (ws) {
        ws.close()
    }
})

/* ===== 유틸리티: UUID 생성 (간단 버전) ===== */
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
    })
}

</script>