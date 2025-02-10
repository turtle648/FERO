{/* 
drawStatus의 사용 예시
App.vue
<template>
  <div id="app">
    <router-view />
    <canvas id="canvas" width="500" height="500"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { drawStatus } from '@/components/modal/_tests_/drawStatus.js' // 수정: drawStatus에서 export 수정
const graphData = ref([70, 30, 15, 72, 50, 75]);

const drawGraph = async () => {
  drawStatus(graphData.value);  // 수정: .value 추가
}

onMounted(() => {
  drawGraph();
});
</script> 
<style scoped>
#app {
  text-align: center;
  padding: 20px;
}

canvas {
  margin-top: 20px;
}
</style>
*/}



export const drawStatus = (graphData) => {
    const CANVAS_SIZE = 500;
    const CANVAS_CENTER = CANVAS_SIZE / 2;
    const GRAPH_RADIUS = 200;
    const TAG_RADIUS = 20;
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');
  
    ctx.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);  // Clear before drawing new graph
  
    drawBackground(ctx, GRAPH_RADIUS, CANVAS_CENTER);
    drawData(ctx, GRAPH_RADIUS, CANVAS_CENTER, graphData); // 수정: graphData를 .value 없이 그대로 사용
    drawTagCircles(ctx, GRAPH_RADIUS, CANVAS_CENTER, TAG_RADIUS);
  
    function drawData(ctx, r, center, data) {
      ctx.beginPath();
      for (let i = 0; i < data.length; i++) {  // 수정: 인덱스를 i로 수정
        const dataPercent = data[i] / 100;
        ctx.lineTo(
          center + r * Math.cos(Math.PI / 3 * i - Math.PI / 2) * (4 / 7) * dataPercent
          + r * Math.cos(Math.PI / 3 * i - Math.PI / 2) * (3 / 7),
          center + r * Math.sin(Math.PI / 3 * i - Math.PI / 2) * (4 / 7) * dataPercent
          + r * Math.sin(Math.PI / 3 * i - Math.PI / 2) * (3 / 7)
        );
      }
      ctx.closePath();
      ctx.strokeStyle = "#3b89db";
      ctx.fillStyle = "rgba(59,137,219, 0.2)";
      ctx.stroke();
      ctx.fill();
    }
  
    function drawBackground(ctx, r, center) {
      drawHexagon(ctx, r, center);
      drawHexagon(ctx, r * (6 / 7), center);
      drawHexagon(ctx, r * (5 / 7), center);
      drawHexagon(ctx, r * (4 / 7), center);
      drawHexagon(ctx, r * (3 / 7), center);
      drawTagTitles(ctx, r + 20, center);
    }
  
    function drawHexagon(ctx, r, center) {
      ctx.beginPath();
      for (let i = 0; i < 6; i++) {
        ctx.lineTo(
          center + r * Math.cos(Math.PI / 3 * i - Math.PI / 2),
          center + r * Math.sin(Math.PI / 3 * i - Math.PI / 2)
        );
      }
      ctx.closePath();
      ctx.strokeStyle = "#eee";
      ctx.stroke();
    }
  
    function drawTagCircles(ctx, r, center, size) {
      for (let i = 0; i < 6; i++) {
        ctx.beginPath();
        ctx.arc(
          center + r * Math.cos(Math.PI / 3 * i - Math.PI / 2),
          center + r * Math.sin(Math.PI / 3 * i - Math.PI / 2),
          size, 0, Math.PI * 2
        );
        ctx.closePath();
        ctx.fillStyle = "#b0b0b0";
        ctx.fill();
  
        ctx.beginPath();
        ctx.fillStyle = "#fff";
        ctx.font = '20px san-serif';
        ctx.fillText(
          `${i + 1}`,
          center + r * Math.cos(Math.PI / 3 * i - Math.PI / 2) - size / 2 + 4.5,
          center + r * Math.sin(Math.PI / 3 * i - Math.PI / 2) + size / 2 - 3.5
        );
        ctx.fill();
        ctx.closePath();
      }
    }
  
    function drawTagTitles(ctx, r, center) {
      ctx.fillStyle = "#000";
      ctx.font = '16px san-serif';
      ctx.beginPath();
      ctx.fillText('태그1',
        center + r * Math.cos(Math.PI / 3 * 0 - Math.PI / 2) - 19,
        center + r * Math.sin(Math.PI / 3 * 0 - Math.PI / 2) - 5);
      ctx.fillText('태그2',
        center + r * Math.cos(Math.PI / 3 * 1 - Math.PI / 2) - 35,
        center + r * Math.sin(Math.PI / 3 * 1 - Math.PI / 2) - 19);
      ctx.fillText('태그3',
        center + r * Math.cos(Math.PI / 3 * 2 - Math.PI / 2) - 38,
        center + r * Math.sin(Math.PI / 3 * 2 - Math.PI / 2) + 32);
      ctx.fillText('태그4',
        center + r * Math.cos(Math.PI / 3 * 3 - Math.PI / 2) - 19,
        center + r * Math.sin(Math.PI / 3 * 3 - Math.PI / 2) + 20);
      ctx.fillText('태그5',
        center + r * Math.cos(Math.PI / 3 * 4 - Math.PI / 2) - 5,
        center + r * Math.sin(Math.PI / 3 * 4 - Math.PI / 2) + 32);
      ctx.fillText('태그6',
        center + r * Math.cos(Math.PI / 3 * 5 - Math.PI / 2) - 2,
        center + r * Math.sin(Math.PI / 3 * 5 - Math.PI / 2) - 19);
      ctx.fill();
      ctx.closePath();
    }
  };
