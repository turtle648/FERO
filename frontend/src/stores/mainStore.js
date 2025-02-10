// stores/mainStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMainStore = defineStore('main', () => {
  const data = ref(null)
  
  async function fetchData() {
    try {
      const response = await fetch('https://i12e103.p.ssafy.io:8076/api/v1/Tutorial')
      data.value = await response.json()
    } catch (error) {
      console.error(error)
    }
  }

  return {
    data,
    fetchData
  }
})
