// 레벨/경험치 조회 API
<<<<<<< HEAD
import { defineStore } from "pinia"
import { ref } from "vue"
import axios from "axios"
import { useUserStore } from "@/stores/store"

export const useUserDataStore = defineStore("userData", () => {
  const BASE_URL = "https://i12e103.p.ssafy.io:8076/api/v1"
  const userStore = useUserStore()
=======
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import { useUserStore } from "@/stores/store";

export const useUserDataStore = defineStore("userData", () => {
  const BASE_URL = "https://i12e103.p.ssafy.io:8076/api/v1";
  const userStore = useUserStore();
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

  const userInfo = ref({
    userName: "",
    userNickname: "",
    userEmail: "",
    phoneNumber: "",
    level: null,
    gender: "",
    experience: null,
    avatar: [null, null, null],
    userStats: {},
<<<<<<< HEAD
  })
=======
  });
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

  // 본인정보 확인 API 호출
  const checkUserInfo = async () => {
    try {
<<<<<<< HEAD
      const token = userStore.accessToken
      if (!token) {
        console.log("정보조회 실패: 토큰이 없습니다.")
        return false
=======
      const token = userStore.accessToken;
      if (!token) {
        console.log("정보조회 실패: 토큰이 없습니다.");
        return false;
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
      }

      const headers = {
        Authorization: token, // Bearer 붙이지 않음 (확인 필요)
        "Content-Type": "application/json",
<<<<<<< HEAD
      }

      const response = await axios.get(`${BASE_URL}/auth/me`, { headers })
      if (response.status !== 200) throw new Error("서버에서 응답 실패")

      console.log("유저정보 조회 성공", response.data)

      // 구조 분해 할당 + 기본값 설정
      const { userName = "", userNickname = "", userEmail = "", phoneNumber = "", level = null, gender = "", experience = null, avatar = "0-0-0", userStats = {} } = response.data

      // avatar 변환 (ex: "2-6-4" --> [2, 6, 4])
      userInfo.value.avatar = avatar.split("-").map(Number)

      const userStatsArr = new Array(userStats.absStats, userStats.absStats, userStats.absStats, userStats.absStats, userStats.absStats, userStats.absStats)
=======
      };

      const response = await axios.get(`${BASE_URL}/auth/me`, { headers });
      if (response.status !== 200) throw new Error("서버에서 응답 실패");

      console.log("유저정보 조회 성공", response.data);

      // 구조 분해 할당 + 기본값 설정
      const {
        userName = "",
        userNickname = "",
        userEmail = "",
        phoneNumber = "",
        level = null,
        gender = "",
        experience = null,
        avatar = "0-0-0",
        userStats = {},
      } = response.data;

      // avatar 변환 (ex: "2-6-4" --> [2, 6, 4])
      userInfo.value.avatar = avatar.split("-").map(Number);

      const userStatsArr = new Array(
        userStats.absStats,
        userStats.absStats,
        userStats.absStats,
        userStats.absStats,
        userStats.absStats,
        userStats.absStats
      );
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

      // userInfo 업데이트
      Object.assign(userInfo.value, {
        userName,
        userNickname,
        userEmail,
        phoneNumber,
        level,
        gender,
        experience,
        userStats,
        userStatsArr,
<<<<<<< HEAD
      })

      return userInfo
    } catch (error) {
      console.error("유저정보 조회 요청 중 에러 발생: ", error)
      if (error.response) {
        console.error("에러 응답:", error.response.data)
      }
      return false
    }
  }
=======
      });

      return userInfo;
    } catch (error) {
      console.error("유저정보 조회 요청 중 에러 발생: ", error);
      if (error.response) {
        console.error("에러 응답:", error.response.data);
      }
      return false;
    }
  };
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

  // 아바타 수정 API
  const updateAvatar = async (newAvatar) => {
    try {
      const token = userStore.accessToken;
      if (!token) {
<<<<<<< HEAD
        console.error("아바타 수정 실패: 토큰이 없습니다.")
        return false
=======
        console.error("아바타 수정 실패: 토큰이 없습니다.");
        return false;
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
      }

      const headers = {
        Authorization: token,
        "Content-Type": "application/json",
<<<<<<< HEAD
      }
=======
      };
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

      const response = await axios.put(`${BASE_URL}/auth/updateAvatar`, null, {
        headers,
        params: { newAvatar },
<<<<<<< HEAD
      })

      if (response.status === 200) {
        console.log("아바타 수정 성공", response.data)

        // userInfo.avatar 업데이트
        userInfo.value.avatar = newAvatar.split("-").map(Number)
=======
      });

      if (response.status === 200) {
        console.log("아바타 수정 성공", response.data);

        // userInfo.avatar 업데이트
        userInfo.value.avatar = newAvatar.split("-").map(Number);
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

        return true;
      } else {
<<<<<<< HEAD
        console.error("아바타 수정 실패: 서버에서 응답 실패")
        return false
      }
    } catch (error) {
      console.error("아바타 수정 요청 중 에러 발생:", error)
      if (error.response) console.error("에러 응답:", error.response.data)
      return false
=======
        console.error("아바타 수정 실패: 서버에서 응답 실패");
        return false;
      }
    } catch (error) {
      console.error("아바타 수정 요청 중 에러 발생:", error);
      if (error.response) console.error("에러 응답:", error.response.data);
      return false;
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
    }
  };

  // 유저 레벨 조회
  const checkUserLevel = async () => {
    try {
<<<<<<< HEAD
      const token = userStore.accessToken
      if (!token) {
        console.error("정보조회 실패: 토큰이 없습니다.")
        return
=======
      const token = userStore.accessToken;
      if (!token) {
        console.error("정보조회 실패: 토큰이 없습니다.");
        return;
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
      }

      const headers = {
        Authorization: token, // Bearer 붙이지 않음 (확인 필요)
        "Content-Type": "application/json",
<<<<<<< HEAD
      }

      const response = await axios.get(`${BASE_URL}/userStats/level`, {
        headers,
      })

      if (response.status === 200) {
        console.log("레벨 조회 성공", response.data)
        return response.data
      } else {
        console.error("레벨 실패: 서버에서 응답 실패")
        return 9999
      }
    } catch (error) {
      console.error("레벨 조회 요청 중 에러 발생: ", error)
      if (error.response) {
        console.error("에러 응답:", error.response.data)
      }
      return 9999
    }
  }
=======
      };

      const response = await axios.get(`${BASE_URL}/userStats/level`, {
        headers,
      });

      if (response.status === 200) {
        console.log("레벨 조회 성공", response.data);
        return response.data;
      } else {
        console.error("레벨 실패: 서버에서 응답 실패");
        return 9999;
      }
    } catch (error) {
      console.error("레벨 조회 요청 중 에러 발생: ", error);
      if (error.response) {
        console.error("에러 응답:", error.response.data);
      }
      return 9999;
    }
  };
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

  // 유저 경험치 조회
  const checkUserExperience = async () => {
    try {
<<<<<<< HEAD
      const token = userStore.accessToken
      if (!token) {
        console.error("경험치 조회 실패: 토큰이 없습니다.")
        return 50
      }

      const headers = {
        Authorization: token, // Bearer 붙이지 않음 (확인 필요)
        "Content-Type": "application/json",
      }

      const response = await axios.get(`${BASE_URL}/userStats/exp`, {
        headers,
      })

      if (response.status === 200) {
        console.log("경험치 조회 성공", response.data)
        return response.data
      } else {
        console.error("경험치 조회 실패: 서버에서 응답 실패")
        return 0
      }
    } catch (error) {
      console.error("경험치 조회 요청 중 에러 발생: ", error)
      if (error.response) {
        console.error("에러 응답:", error.response.data)
      }
      return 0
    }
  }

=======
      const token = userStore.accessToken;
      if (!token) {
        console.error("경험치 조회 실패: 토큰이 없습니다.");
        return 50;
      }

      const headers = {
        Authorization: token, // Bearer 붙이지 않음 (확인 필요)
        "Content-Type": "application/json",
      };

      const response = await axios.get(`${BASE_URL}/userStats/exp`, {
        headers,
      });

      if (response.status === 200) {
        console.log("경험치 조회 성공", response.data);
        return response.data;
      } else {
        console.error("경험치 조회 실패: 서버에서 응답 실패");
        return 0;
      }
    } catch (error) {
      console.error("경험치 조회 요청 중 에러 발생: ", error);
      if (error.response) {
        console.error("에러 응답:", error.response.data);
      }
      return 0;
    }
  };

>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
  return {
    userInfo,
    checkUserInfo,
    updateAvatar,
    // 삭제 예정(테스트용 API)
    checkUserLevel,
    checkUserExperience,
<<<<<<< HEAD
  }
})
=======
  };
});
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
