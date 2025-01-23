<template>
    <div class="modal" @click="closeModalOutside">
      <div class="modal-content" @click.stop>
          
        <!-- 닫기 버튼 -->
        <button @click="close">Close</button>
        
        <!-- 아이디/비밀번호 폼 -->
        <form @submit.prevent="handleSubmit">
          <div>
            <label for="id">ID</label>
            <input type="text" id="id" v-model="id" placeholder="Enter your ID" required />
          </div>
          <div>
            <label for="password">Password</label>
            <input type="password" id="password" v-model="password" placeholder="Enter your password" required />
          </div>
          <div>
            <button type="submit">Sign In</button>
          </div>
        </form>
        
        <hr>
        <!-- 소셜 로그인 버튼들 -->
        <div class="social-login">
          <button @click="handleSocialLogin('kakao')">
            Kakao
          </button>
          <button @click="handleSocialLogin('facebook')">
            Facebook
          </button>
          <button @click="handleSocialLogin('google')">
            Google
          </button>
          <button @click="handleSocialLogin('naver')">
            Naver
          </button>
        </div>
        <hr>
        
        <!-- 회원가입 버튼 -->
        <div>
          <button @click="goToSignUp">Sign Up</button>
        </div>
  
      </div>
    </div>
</template>
  
<script setup>
  import { ref } from 'vue'
  import { defineEmits } from 'vue'
  import { useRouter } from 'vue-router'
  
  // 부모 컴포넌트로 모달 닫기 이벤트 전송
  const emit = defineEmits(['close'])
  
  // 아이디, 비밀번호 상태
  const id = ref('')
  const password = ref('')
  
  // 라우터 사용
  const router = useRouter()
  
  // 로그인 폼 제출 처리
  const handleSubmit = async () => {
    try {
        // 백엔드 URL이 없을 경우에 대비한 임시 로직
        const mockResponse = {
            token: '', // 임시 토큰
        };

        // 실제 API 호출이 아닌 임시 응답으로 처리
        const data = mockResponse;

        if (data.token) {
        // 토큰이 있으면 저장 후, 메인 페이지로 이동
        localStorage.setItem('authToken', data.token);
        router.push('/main');
        } else {
        // 토큰이 없으면 경고
        alert('로그인 실패: 서버에서 토큰을 반환하지 않았습니다.');
        }
    } catch (error) {
        // 에러 핸들링
        console.error('로그인 처리 중 에러 발생:', error);
        alert('로그인 처리 중 문제가 발생했습니다.');
    }
    };

  
  // 소셜 로그인 처리
  const handleSocialLogin = () => { alert('아직 구현 안했습니다만?') }
  
  // 회원가입 페이지로 이동
  const goToSignUp = () => { router.push('/signup') }
  
  // 모달 닫기 처리
  const close = () => { emit('close') }
  
  // 배경 클릭 시 모달 닫기 처리
  const closeModalOutside = () => { emit('close') }
</script>
  
<style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
  }
  button {
    margin-top: 10px;
  }
/* 소셜 로그인 버튼 컨테이너 */
.social-login {
  display: flex; /* Flexbox 활성화 */
  justify-content: space-between; /* 버튼 간 간격 조절 */
  gap: 10px; /* 버튼 간 간격 추가 */
  margin-top: 20px; /* 컨테이너 상단 여백 */
}

/* 개별 버튼 스타일 */
.social-button {
  flex: 1; /* 버튼 크기를 동일하게 설정 */
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  text-align: center;
}

/* 각각의 소셜 로그인 버튼 색상 */
.kakao {
  background-color: #fee500;
  color: #000000;
}

.facebook {
  background-color: #4267B2;
  color: white;
}

.google {
  background-color: #DB4437;
  color: white;
}

.naver {
  background-color: #1EC800;
  color: white;
}
</style>
  