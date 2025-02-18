<!-- modal-background -->
<div class="modal-bg fixed inset-0 flex justify-center items-center bg-black bg-opacity-50" @click="closeStatusModal">
  <!-- modal -->
  <div class="status-modal bg-[#c3c3c3] w-[90%] h-[80%] max-w-4xl max-h-[80vh] shadow-lg flex flex-col relative" @click.stop>
    <!-- close-button -->
    <div class="button-container absolute top-2 right-2 z-10 flex items-center justify-between">
      <h2 class="font-dgm text-3xl">Status</h2>
      <button class="nes-btn px-[1.5%] py-[1%]" @click="closeStatusModal">x</button>
    </div>

    <!-- status-container -->
    <!-- 요소 1: 닉네임, 레벨, 랭크-->
    <div class="status-element status-1">
      <div class="font-dgm text-xl" style="margin-top: 2%">Nickname: {{ userInfo.userNickname }}</div>
      <div class="font-dgm text-xl" style="margin-top: 2%">Lv. {{ userInfo.level }}</div>
      <div class="rank-container font-dgm text-xl">Rank: !!api수정을 통해 받기</div>
    </div>

    </div>

    <!-- 요소 3: 퀘스트 달성률, 게이지바 -->
    <div class="status-element status-3">
      <div style="margin-bottom: 2%">
        <!-- <span style="margin-right: 10%;">미션 클리어 수: !!퀘스트api완성후추가!!</span> -->
        <!-- <span>월간 클리어 수: !!퀘스트api완성후추가!!</span> -->
      </div>

      <!-- progress -->
      <div class="progress-box">
        <span class="font-dgm">일일 달성률:</span>
        <progress class="nes-progress is-primary w-[24vh] h-[3vh]" value="80" max="100"></progress>
        <!-- 여기서 박스를 채우기 위해 v-for로 박스를 순회하며 스타일을 적용 -->
        <!-- <span v-for="n in 5" :key="n"
        :class="{
          filled: (85 % 100) / 20 >= n,
          empty: (85 % 100) / 20 < n,
        }"
        class="progress-box-cell"
      ></span>
      (!!퀘스트api완성후추가!!%)
      85% -->
        <!-- ({{ userStatus.todayQuestProgress }}%) -->
      </div>
    </div>

    <!-- 요소 4: 그래프 이미지 박스 -->
    <div class="status-element status-4">
      <div class="status-graph">
        <PolarArea :data="chartData" :options="options" />
      </div>
    </div>
  </div>
</div>