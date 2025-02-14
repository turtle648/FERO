// stores/friendStore.js
import { defineStore } from "pinia"
import { ref } from "vue"
import axios from "axios"

export const useFriendStore = defineStore("friend", () => {
  const friendList = ref([])
  const authToken = ref(localStorage.getItem("authToken"))

  const api = axios.create({
    baseURL: "https://i12e103.p.ssafy.io:8076/api/v1",
    // baseURL: "http://localhost:8076/api/v1",

    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${authToken.value}`,
    },
  })

  async function getFriendList() {
    try {
      const response = await api.get("/friend-list")
      friendList.value = response.data

      console.log("[📊] Friend Data:", JSON.parse(JSON.stringify(friendList.value)))
    } catch (error) {
      console.error("[❗] Load Error:", error)
      throw error
    }
  }

  return {
    friendList,
    authToken,
    getFriendList,
  }
})
