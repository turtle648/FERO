import cv2
import numpy as np
import base64
import os
import CustomAiGym
from fastapi import FastAPI, WebSocket
from concurrent.futures import ThreadPoolExecutor
from starlette.websockets import WebSocketDisconnect

app = FastAPI()

# 프레임 수집을 위한 변수
frame_buffer = []
full_buffer = []
fps = 30
duration = 3  # 1초 동안 수집
buffer_size = fps * duration
count = 1

# 모델 로드
shared_model = "yolo11n-pose.pt"

left_leg = CustomAiGym.AIGym(show=True, kpts=[12, 14, 16], model=shared_model, line_width=2)
left_arm = CustomAiGym.AIGym(show=True, kpts=[6, 8, 10], model=shared_model, line_width=2)
right_leg = CustomAiGym.AIGym(show=True, kpts=[11, 13, 15], model=shared_model, line_width=2)
right_arm = CustomAiGym.AIGym(show=True, kpts=[5, 7, 9], model=shared_model, line_width=2)

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
    global frame_buffer, full_buffer

    print("웹소켓 연결 요청 받음")
    try:
        await websocket.accept()
        print("웹소켓 연결 수락됨")

        # 비디오 작성을 위한 초기화
        video_writer = None  # 초기화
        while True:
            data = await websocket.receive_text()
            frame = decode_base64_image(data)
            frame_buffer.append(frame)
            full_buffer.append(frame)

            if len(frame_buffer) >= buffer_size:
                # 비디오 작성을 위한 초기화
                if video_writer is None:
                    height, width, _ = frame_buffer[0].shape
                    video_writer = cv2.VideoWriter("workouts.avi", cv2.VideoWriter_fourcc(*"mp4v"), fps, (width, height))

                for im0 in frame_buffer:
                    with ThreadPoolExecutor() as executor:
                        results = list(executor.map(process_with_model, [right_arm], [im0]))

                    # 평균화된 이미지 생성
                    combined_im0 = results[0][0]
                                      # results[1][0].astype(np.float32) +
                                      # results[2][0].astype(np.float32) +
                                      # results[3][0].astype(np.float32)) / 4).astype(np.uint8)

                    # 비디오에 기록
                    video_writer.write(combined_im0)

                    message = (
                        # f"LeftLeg:{results[0][1]}, " +
                        # f"LeftArm:{results[1][1]}, " +
                        # f"RightLeg:{results[2][1]}, " +
                        f"RightArm:{results[0][1]}"
                    )

                    await websocket.send_text(message)
                    print(message)

                # 프레임 버퍼 초기화
                frame_buffer = []

    except WebSocketDisconnect:
        print("웹소켓 연결 종료")
        if video_writer is not None:
            video_writer.release()  # 비디오 작성을 마무리
