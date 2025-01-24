import cv2
import numpy as np
from concurrent.futures import ThreadPoolExecutor
from ultralytics import solutions


cap = cv2.VideoCapture(0)
assert cap.isOpened(), "Error reading video file"
w, h, fps = (int(cap.get(x)) for x in (cv2.CAP_PROP_FRAME_WIDTH, cv2.CAP_PROP_FRAME_HEIGHT, cv2.CAP_PROP_FPS))

# Init AIGym
shared_model = "yolo11n-pose.pt"
left_leg = solutions.AIGym(show=True, kpts=[12, 14, 16], model=shared_model, line_width=2)
left_arm = solutions.AIGym(show=True, kpts=[6, 8, 10], model=shared_model, line_width=2)
right_leg = solutions.AIGym(show=True, kpts=[11, 13, 15], model=shared_model, line_width=2)
right_arm = solutions.AIGym(show=True, kpts=[5, 7, 9], model=shared_model, line_width=2)

# 멀티스레드 함수
def process_with_model(model, frame):
    process_frame = model.monitor(frame)
    print(model.count)
    return process_frame

# Process video
while cap.isOpened():
    success, im0 = cap.read()
    if not success:
        print("카메라에서 프레임을 읽을 수 없습니다. 스트림 종료.")
        break

    im0_processed = im0.copy()
    with ThreadPoolExecutor() as executor:
        results = list(executor.map(process_with_model, [left_leg, left_arm, right_leg, right_arm], [im0_processed] * 4))

    # 병합: numpy 연산으로 병합
    combined_im0 = ((results[0].astype(np.float32) +
                     results[1].astype(np.float32) +
                     results[2].astype(np.float32) +
                     results[3].astype(np.float32)) / 4).astype(np.uint8)

    # 화면에 표시
    cv2.imshow("AI Gym Monitoring", combined_im0)

    # ESC 키를 누르면 종료
    if cv2.waitKey(1) & 0xFF == 27:
        break

# 종료 처리
cap.release()
cv2.destroyAllWindows()
