// stores/matchStore.js
import { defineStore } from 'pinia'

export const useMatchStore = defineStore('match', {
  state: () => ({
    matchStatus: 'idle', // 'idle', 'searching', 'found', 'accepted'
    matchedUser: null,
    webSocket: null
  }),

  actions: {
    // WebSocket 연결 초기화
    initializeWebSocket() {
      this.webSocket = new WebSocket('wss://your-server/match')
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
      }
    }
  }
})
