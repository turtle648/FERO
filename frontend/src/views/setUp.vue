<template>
    <div class="flex flex-col items-center justify-center min-h-screen bg-gray-100 text-gray-800 px-6">
      <div class="max-w-md bg-white p-6 rounded-xl shadow-lg">
        <h1 class="text-xl font-bold text-center mb-4">📲 PWA 설치 안내</h1>
        <p class="text-sm text-gray-600 text-center mb-4">보다 편리하게 사용하려면 PWA를 설치하세요.</p>
  
        <!-- iOS 안내 -->
        <div class="mb-6">
          <h2 class="text-lg font-semibold flex items-center">
            <AppleIcon class="w-5 h-5 mr-2 text-gray-700" /> Apple (Safari)
          </h2>
          <ol class="list-decimal list-inside text-sm text-gray-700 mt-2 space-y-1">
            <li>Safari 브라우저에서 이 페이지를 엽니다.</li>
            <li>하단의 <strong>공유 버튼</strong> (<ShareIcon class="inline w-4 h-4" />)을 탭합니다.</li>
            <li><strong>"홈 화면에 추가"</strong> 버튼을 선택합니다.</li>
            <li>앱 이름을 확인한 후 <strong>"추가"</strong> 버튼을 누릅니다.</li>
          </ol>
        </div>
        <hr>
        <!-- Android 안내 -->
        <div>
          <h2 class="text-lg font-semibold flex items-center">
            <AndroidIcon class="w-5 h-5 mr-2 text-green-600" /> Android (Chrome)
          </h2>
          <ol class="list-decimal list-inside text-sm text-gray-700 mt-2 space-y-1">
            <li>Chrome 브라우저에서 이 페이지를 엽니다.</li>
            <li>주소창 우측의 <strong>메뉴 버튼 ( ⋮ )</strong>을 탭합니다.</li>
            <li><strong>"앱 설치"</strong> 또는 <strong>"홈 화면에 추가"</strong>를 선택합니다.</li>
            <li>확인 메시지가 나타나면 <strong>"추가"</strong> 버튼을 누릅니다.</li>
          </ol>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { AppleIcon, AndroidIcon, ShareIcon } from 'lucide-vue-next';
  </script>  