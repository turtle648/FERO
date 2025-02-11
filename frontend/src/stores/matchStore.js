// stores/matchStore.js
import { defineStore } from 'pinia'

// 추가로 고려할 수 있는 것
// 상태타입 정의 추가
// 연결 상태 모니터링 추가
// cleanup함수 추가
// 타임아웃 처리 추가

export const useMatchStore = defineStore('match', {
  state: () => ({
    matchStatus: 'idle', // 'idle', 'searching', 'found', 'accepted'
    matchedUser: null,
    webSocket: null,
    webrtcConnection: null,
    signalingData: {
      offer: null,
      answer: null,
      candidates: []
    }
  }),

  actions: {
    // WebSocket 연결 초기화
    initializeWebSocket() {
      this.webSocket = new WebSocket('wss://i12e103.p.ssafy.io:8076/api/v1/videorooms')
      this.setupWebSocketListeners()
    },

    // WebSocket 이벤트 리스너 설정
    setupWebSocketListeners() {
      this.webSocket.onopen = () => {
        console.log('WebSocket 연결됨')
        this.startMatching()
      }

      this.webSocket.onmessage = (event) => {
        const data = JSON.parse(event.data)
        this.handleWebSocketMessage(data)
      }

      this.webSocket.onerror = (error) => {
        console.error('WebSocket 에러:', error)
      }

      this.webSocket.onclose = () => {
        console.log('WebSocket 연결 종료')
        this.reconnectWebSocket()
      }
    },

    // 재연결 로직
    async reconnectWebSocket() {
      let retryCount = 0
      const maxRetries = 3

      while (retryCount < maxRetries) {
        try {
          await new Promise(resolve => setTimeout(resolve, 2000)) // 2초 대기
          this.initializeWebSocket()
          return // 성공하면 종료
        } catch (error) {
          retryCount++
          console.error(`재연결 시도 ${retryCount} 실패:`, error)
        }
      }
      console.error('최대 재시도 횟수 초과')
    },

    // 매칭 시작
    startMatching() {
      this.matchStatus = 'searching'
      if (this.webSocket?.readyState === WebSocket.OPEN) {
        this.webSocket.send(JSON.stringify({
          type: 'START_MATCHING'
        }))
      }
    },

    // 매칭 수락
    acceptMatch() {
      this.matchStatus = 'accepted'
      if (this.webSocket?.readyState === WebSocket.OPEN) {
        this.webSocket.send(JSON.stringify({
          type: 'ACCEPT_MATCH',
          userId: this.matchedUser?.id
        }))
      }
    },

    // 매칭 거절
    declineMatch() {
      this.matchStatus = 'searching'
      if (this.webSocket?.readyState === WebSocket.OPEN) {
        this.webSocket.send(JSON.stringify({
          type: 'DECLINE_MATCH',
          userId: this.matchedUser?.id
        }))
      }
      this.matchedUser = null
    },

    // WebSocket 메시지 처리
    handleWebSocketMessage(data) {
      switch (data.type) {
        case 'MATCH_FOUND':
          this.matchStatus = 'found'
          this.matchedUser = data.user
          break
        case 'MATCH_ACCEPTED':
          this.matchStatus = 'accepted'
          break
        case 'MATCH_DECLINED':
          this.matchStatus = 'searching'
          this.matchedUser = null
          break
        case 'webrtc':
          this.handleWebRTCMessage(data.data)
          break
      }
    },

    // WebRTC 시그널링 데이터 전송
    sendWebRTCData(data) {
      if (this.webSocket?.readyState === WebSocket.OPEN) {
        this.webSocket.send(JSON.stringify({
          type: 'webrtc',
          data: data,
          target: this.matchedUser?.id
        }))
      }
    },

    // WebRTC 시그널링 메시지 처리
    handleWebRTCMessage(data) {
      switch (data.type) {
        case 'offer':
          this.signalingData.offer = data.sdp
          this.handleOffer(data.sdp)
          break
        case 'answer':
          this.signalingData.answer = data.sdp
          if (this.webrtcConnection) {
            this.webrtcConnection.setRemoteDescription(new RTCSessionDescription(data.sdp))
          }
          break
        case 'candidate':
          this.signalingData.candidates.push(data.candidate)
          this.handleCandidate(data.candidate)
          break
      }
    },

    // Answer 처리 로직
    async handleOffer(offer) {
      try {
        await this.webrtcConnection.setRemoteDescription(new RTCSessionDescription(offer))
        const answer = await this.webrtcConnection.createAnswer()
        await this.webrtcConnection.setLocalDescription(answer)
        
        this.sendWebRTCData({
          type: 'answer',
          sdp: answer
        })
      } catch (error) {
        console.error('Answer 생성 실패:', error)
      }
    },

    // ICE candidate 처리
    async handleCandidate(candidate) {
      try {
        await this.webrtcConnection.addIceCandidate(new RTCIceCandidate(candidate))
      } catch (error) {
        console.error('ICE candidate 추가 실패:', error)
      }
    }
  }
})
