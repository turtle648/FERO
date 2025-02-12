<template>
    <div class="quest-modal">
        <div class="content">
            <button id="close-btn" @click="closeQuestModal">X</button>
            <h1>오늘의 퀘스트</h1>
            <!-- 로딩이 있으면 표기 -->
            <div v-if="isLoading">데이터 불러오는 중</div>

            <!-- 퀘스트 데이터 -->
            <!-- 이후에 퀘스트 데이터에 ccs 넣을거면 -->
            <!-- :class="{ 'completed': quest.is_completed }" 써서 동적 바인딩-->
            <div v-else>
                <div class="quest-item">
                    <p>시간: {{ questData.quest_time }}</p>
                    <p>운동 횟수: {{ questData.real_cnt }}</p>
                    <p>목표 갯수: {{ questData.exercise_cnt }}</p>
                    <p>상태: {{ questData.is_completed ? '완료' : '미완료' }}</p>
                    <p>메세지: {{ questData.message }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineEmits, ref, onMounted } from "vue"
import axios from "axios"


// 상태 관리
// 퀘스트 데이터 저장
const questData = ref([])
// 로딩 상태 저장
const isLoading = ref(false)


// 모달 닫기
const emit = defineEmits(['quest-modal'])
const closeQuestModal = () => {
emit('close-modal')
}


// API 호출 함수
const getQuestData = async () => {
    isLoading.value = true
    try {
        const today = new Date().toISOString().split('T')[0]
        const token = localStorage.getItem('authToken')

        // 추후에 API 나오면 변경
        const response = await axios.get('/dummy_data/getQuestData.json', {
            headers: {
                Authorization: `Bearer ${token}`
            },
            params: {
                date: today
            }
        })

        questData.value = response.data

    } catch (error) {
        console.error('퀘스트 데이터 조회 실패', error)
    } finally {
        isLoading.value = false
    }
}


// 마운트 시 데이터 호출
onMounted(() => {
    getQuestData()
})
</script>

<style scoped>
.quest-modal {
  width: 40vh;
  height: 60vh;
  position: fixed;
  top: 50%; /* 화면의 세로 중앙 */
  left: 50%; /* 화면의 가로 중앙 */
  transform: translate(-50%, -50%); /* 자신의 크기만큼 반으로 이동 */
  background: rgba(255, 255, 255);
  display: flex;
}

.content {
  width: 100%;
  position: relative;
  text-align: center;
}

#close-btn {
  position: absolute;
  right: 3%;
  top: 2%;
  z-index: 11;
}
</style>