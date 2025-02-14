<template>
  <div class="modal" @click="closeModalOutside">
    <div class="modal-content" @click.stop>
      <!-- 로그인 모드 -->
      <div v-if="!isSignUpMode">
        <div class="sign-in-mode-header">
          <img src="@/assets/images/LogInTitle.png" alt="">
          <button class="close-btn" @click="close">X</button>
        </div>
        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <label for="id">ID</label>
            <input type="text" id="id" v-model="id" placeholder="아이디를 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="password">Password</label>
            <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" required />
          </div>
          <div>
            <button type="submit" class="log-in-button">로그인</button>
          </div>
          </form>
        <hr>
        <div class="sign-in-mode-footer">
          <a href="#" class="disabled-link">ID 찾기</a>
          <a href="#" class="disabled-link">PW 찾기</a>
          <a href="#" class="disabled-link" @click="openSignUp">회원가입</a>
        </div>
        <hr>
        
        <div class="social-login">
          <img class="social-login-icon" src="@/assets/images/icon/kakao-talk-icon.png" alt="카카오톡" @click="handleSocialLogIn('kakao')">
          <img class="social-login-icon" src="@/assets/images/icon/google-icon.png">
          <img class="social-login-icon" src="@/assets/images/icon/facebook-icon.png">
          <img class="social-login-icon" src="@/assets/images/icon/instagram-icon.png">
        </div>  
      </div>

      <!-- 회원가입 모드 -->
      <div v-else>
        <div class="sign-up-mode-header">
          <img 
            class="sign-up-step-icon"
            :src="require(`@/assets/images/icon/sign-up-${signUpPage}.png`)" >
          <button class="close-btn" @click="close">X</button>
        </div>
        <form v-if="signUpPage === 1" @submit.prevent="handleSignUp1">
          <div class="input-group">
            <label for="user-email">Email</label>
            <div class="input-with-button">
              <input type="email" id="user-email" v-model="email" placeholder="이메일을 입력하세요" required :disabled="isEmailAvailable" />
              <button type="button" class="check-email-button" @click="checkEmailDuplicate(email)">중복 체크</button>
            </div>
          </div>
          <div class="input-group">
            <input type="text" v-model="emailConfirmCode" placeholder="인증 코드를 입력하세요" required />
          </div>
          <div>
            <button type="submit">다음</button>
          </div>
        </form>
        
        <form v-if="signUpPage === 2" @submit.prevent="handleSignUp2">
          <div class="input-group">
            <label for="signup-id">ID</label>
            <input type="text" id="signup-id" v-model="id" placeholder="아이디를 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="signup-password">Password</label>
            <input type="password" id="signup-password" v-model="password" placeholder="비밀번호를 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="confirm-password">Confirm Password</label>
            <input type="password" id="confirm-password" v-model="confirmPassword" placeholder="비밀번호를 다시 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="user-name">Name</label>
            <input type="text" id="user-name" v-model="name" placeholder="이름을 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="user-phone">Phone Number</label>
            <input type="text" id="user-phone" v-model="phone" placeholder="전화번호를 입력하세요" required />
          </div>
          <div>
            <button type="submit">다음</button>
          </div>
        </form>

        <form v-if="signUpPage === 3" @submit.prevent="handleSignUp3">
            <div class="input-group select-character-info">
              <div class="input-group">
                <label for="user-nickname">User Nickname</label>
                <input type="text" id="user-nickname" v-model="userNickname" placeholder="유저 닉네임" required />
              </div>
              <div class="input-group">
                <label for="user-gender">Gender</label>
                <select id="gender" v-model="gender" required>
                  <option value="M">M</option>
                  <option value="F">F</option>
                </select>
              </div>
            </div>
            <hr>
            <div class="select-box"> 
              <img class="defalt-character" 
              :class="{'selected-character': gender === 'M'}"
              src="@/assets/images/character/defalt_character_M.png"
              @click="selectCharacter('M')"
              >
              <img class="defalt-character" 
              :class="{'selected-character': gender === 'F'}"
              src="@/assets/images/character/defalt_character_F.png"
              @click="selectCharacter('F')"
              >
            </div>
          <div>
            <button type="submit">완료</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, watchEffect, defineEmits } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/store'

const emit = defineEmits(['close'])
const userStore = useUserStore() // Pinia store 사용
const router = useRouter()
const isSignUpMode = ref(false)
const signUpPage = ref(1)

const id = ref('')
const password = ref('')
const confirmPassword = ref('')
const name = ref('')
const phone = ref('')
const email = ref('')
const isEmailAvailable = ref(false)
const emailConfirmCode = ref('')
const gender = ref('')
const userNickname = ref('')
const avatar = ref('') 

// watch()를 사용하여 gender 변경 감지
watchEffect(() => {
  if (gender.value === 'F') { avatar.value = '0-0-1' } 
  else { avatar.value = '0-0-0' }
})

// 변수 초기화
const resetFields = () => {
  id.value = ''
  password.value = ''
  confirmPassword.value = ''
  email.value = ''
  phone.value = ''
  name.value = ''
  gender.value = ''
  isEmailAvailable.value = false
  emailConfirmCode.value = ''
  userNickname.value = ''
}

// 로그인 API 호출
const handleSubmit = async () => {
  try {
    const result = await userStore.logIn(id.value, password.value)
    if (result) { 
      userStore.setAccessToken(result)
      router.push('/main') 
    }
  } catch (error) {console.log(error)}
}

// 이메일 중복확인 API
const checkEmailDuplicate = (email) => {
  userStore.checkEmailDuplicateAPI(email)
    .then((result) => {
      if (result) {
        isEmailAvailable.value = true
        alert('이메일 중복확인이 완료되었습니다. 인증코드를 입력해주세요.')
        userStore.sendEmail(email)
      }
    })
}

// 인증 코드 확인 후 2페이지로 이동
const handleSignUp1 = async () => {
  const result = await userStore.verifyEmail(emailConfirmCode.value, email.value)
  if (result) { signUpPage.value = 2} 
  else { alert('인증 코드가 유효하지 않습니다. 다시 시도해주세요.')}
}

// 인증 코드 확인 후 3페이지로 이동
const handleSignUp2 = () => {
  if (password.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  } else if (!isEmailAvailable.value) {
    alert('이메일 중복체크를 진행해 주세요.')
    return
  } else {
    userStore.signUp(id.value, password.value, name.value, phone.value, email.value)
      .then((result) => {
        userStore.setSessionId(result.message) // Pinia에서 sessionId 설정
        signUpPage.value = 3
      })
  }
}

const handleSignUp3 = async () => {
  const result = await userStore.registerCharacter(gender.value, userNickname.value, avatar.value, userStore.sessionId)
  if (result) {
    alert('회원가입이 완료되었습니다.')
  }
  closeSignUp()
}

const selectCharacter = (gen) => { gender.value = gen}


const handleSocialLogIn = (platform) => { alert(`${platform} 소셜 로그인! 미구현`) }

const openSignUp = () => { isSignUpMode.value = true; resetFields(); }

const closeSignUp = () => { isSignUpMode.value = false; resetFields(); }

const close = () => { resetFields(); emit('close'); }

const closeModalOutside = () => { resetFields(); emit('close'); }
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
  z-index: 9999;
}

.modal-content {
  background-color: white;
  margin: 10%;
  padding: 5%;
  border-radius: 8px;
  width: 100%;
  height: auto;
  overflow-y: auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #dcdcdc;
  color: #333;
  font-size: 14px;
  transition: background-color 0.3s;
}

.social-login {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 20px;
}

.social-button {
  flex: 1;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 12px;
  cursor: pointer;
  text-align: center;
  transition: background-color 0.3s;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  font-size: 14px;
  font-weight: bold;
}

.input-group input {
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

.input-group select {
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

hr {
  border: 1px solid #f0f0f0;
}

.sign-in-mode-header,
.sign-up-mode-header {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

button.close-btn {
  position: absolute;
  top: 0%;
  right: 0%;
  padding: 0;
  background-color: transparent;
  font-size: 16px;
  cursor: pointer;
  color: #333;
}

button.log-in-button {
  width: 100%;
  background-color: #dcdcdc;
  color: #333;
}

input:disabled {
  background-color: #f5f5f5;
}

.sign-in-mode-footer {
  display: flex;
  justify-content: space-around; /* 가로로 정렬 */
}

.sign-in-mode-footer a.disabled-link {
  color: #0000005d; /* 검은색으로 보이게 */
}

.social-login-icon {
  width: 15%; /* 부모 요소의 너비에 맞게 크기 조정 */
  height: 15%; /* 부모 요소의 높이에 맞게 크기 조정 */
  object-fit: cover ;
}

.sign-up-step-icon {
  width: 15%; /* 부모 요소의 너비에 맞게 크기 조정 */
  height: 15%; /* 부모 요소의 높이에 맞게 크기 조정 */
  object-fit: cover ;
}

.input-with-button {
  display: flex;
  justify-content: space-between; /* 입력란과 버튼을 양옆으로 배치 */
  margin: 0%;
  padding: 0%;
}

.input-with-button button {
  height: 35px;
  margin-left: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  text-align: center;
  white-space: nowrap; /* 글자가 한 줄로 유지됨 */
  min-width: 60px; /* 버튼이 너무 작아지지 않도록 최소 너비 설정 */
  padding: 0 12px; /* 좌우 여백 추가 */
}


.input-group input {
  width: 100%;
  padding: 8px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.input-group {
  margin-bottom: 10px;
  padding: 0 0 0 0;
}

.select-character-info {
  display: flex;
  justify-content: space-between; /* 중앙 정렬 (space-between도 가능) */
  align-items: center; /* 세로축 중앙 정렬 */
  gap: 15px; /* 요소 간 간격 */
}

.input-group select {
  height: 35px;
  font-size: 14px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.select-box {
  display: flex;
  justify-content: space-around; /* 이미지들이 가로로 고르게 분포되도록 */
  align-items: center; /* 이미지들이 세로로 정렬되도록 */
}

.defalt-character {
  /* select박스의 width기준 */
  width: 40%;
  border: 3px solid;
  border-color:white;
}

.selected-character {
  border: 3px solid #ff9f9f; /* 선택된 캐릭터에 테두리 추가 */
}
</style>
  