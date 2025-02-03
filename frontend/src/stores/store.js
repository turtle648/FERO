// stores/user.js
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    accessToken: null,
    sessionId: null,
  }),
  actions: {
    setAccessToken(token) {
      this.accessToken = token;
      localStorage.setItem('authToken', token); // 로컬스토리지에 저장
    },
    setSessionId(sessionId) {
      this.sessionId = sessionId;
      localStorage.setItem('sessionId', sessionId); // 로컬스토리지에 저장
    },
    clearSession() {
      this.accessToken = null;
      this.sessionId = null;
      localStorage.removeItem('authToken');
      localStorage.removeItem('sessionId');
    },
  },
});
