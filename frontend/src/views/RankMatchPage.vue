<!-- views/RankMatchPage.vue -->
<template>
    <div class="rank-match-container">
        <!-- 매칭 진행 상태 표시 -->
        <div v-if="matchStatus === 'searching'" class="matching-status">
        <div class="status-text">매칭을 찾는 중입니다...</div>
        <div class="loading-spinner"></div>
        </div>

        <!-- 매칭 발견 시 수락/거절 UI -->
        <div v-if="matchStatus === 'found'" class="match-found">
        <h3>매치가 발견되었습니다!</h3>
        <div class="button-group">
            <button @click="acceptMatch" class="accept-button">수락</button>
            <button @click="declineMatch" class="decline-button">거절</button>
        </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const matchStatus = ref('searching')

// 매칭 수락 처리
const acceptMatch = () => {
router.push({
    name: 'RankMode',
    params: {
    exercise: 'squat' // 현재는 스쿼트만 지원
    }
})
}

// 매칭 거절 처리
const declineMatch = () => {
matchStatus.value = 'searching'
}

// 컴포넌트 마운트 시 매칭 시작
onMounted(() => {
// 테스트를 위해 3초 후 매칭 발견으로 상태 변경
setTimeout(() => {
    matchStatus.value = 'found'
}, 3000)
})
</script>

<style scoped>
.rank-match-container {
@apply flex flex-col items-center justify-center min-h-screen bg-gray-100;
}

.matching-status {
@apply flex flex-col items-center space-y-4;
}

.status-text {
@apply text-xl font-semibold text-gray-700;
}

.loading-spinner {
@apply w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin;
}

.match-found {
@apply flex flex-col items-center space-y-6 p-6 bg-white rounded-lg shadow-md;
}

.button-group {
@apply flex space-x-4;
}

.accept-button {
@apply px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600;
}

.decline-button {
@apply px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600;
}
</style>
