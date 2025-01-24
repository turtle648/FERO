import base64
import os

import cv2
import numpy as np

import CustomMediaPipe
from fastapi import FastAPI, WebSocket
from starlette.websockets import WebSocketDisconnect

app = FastAPI()

# 프레임 수집을 위한 변수
frame_buffer = []
full_buffer = []
fps = 30
duration = 1  # 1초 동안 수집
buffer_size = fps * duration
count = 1


def decode_base64_image(base64_str):
    header, encoded = base64_str.split(',', 1)
    decoded_image = base64.b64decode(encoded)
    image_array = np.frombuffer(decoded_image, np.uint8)
    image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
    return image

def process_with_model(model, frame):
    process = model.monitor(frame)
    print(f"count:: {process[1]}")
    return process


def process_video(frames, base_filename="temp_video", extension="mp4"):
    # 파일 이름과 확장자 설정
    global count
    filename = f"{base_filename}.{extension}"

    # 같은 이름의 파일이 존재하는지 확인하고, 존재할 경우 숫자를 증가시킴
    while os.path.exists(filename):
        filename = f"{base_filename}_{count}.{extension}"
        count += 1

    height, width, _ = frames[0].shape
    video = cv2.VideoWriter(
        filename, cv2.VideoWriter_fourcc(*"mp4v"), fps, (width, height)
    )

    for frame in frames:
        video.write(frame)
    video.release()
    return filename

def encode_frame_to_base64(frame):
    _, buffer = cv2.imencode('.png', frame)
    frame_base64 = base64.b64encode(buffer).decode('utf-8')
    return f"data:image/png;base64,{frame_base64}"



@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    print("웹소켓 연결 요청 받음")
    model = CustomMediaPipe.AIGym()

    try:
        await websocket.accept()
        print("웹소켓 연결 수락됨")

        # 비디오 작성을 위한 초기화
        video_writer = None  # 초기화
        while True:
            data = await websocket.receive_text()
            frame = decode_base64_image(data)

            result = model.monitor(frame)
            await websocket.send_text(f"stage: {result[0]}, counter: {result[1]}")


    except WebSocketDisconnect:
        print("웹소켓 연결 종료")
        if video_writer is not None:
            video_writer.release()  # 비디오 작성을 마무리




