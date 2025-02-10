import cv2

from ultralytics import solutions

cap = cv2.VideoCapture(0)
assert cap.isOpened(), "Error reading video file"
w, h, fps = (int(cap.get(x)) for x in (cv2.CAP_PROP_FRAME_WIDTH, cv2.CAP_PROP_FRAME_HEIGHT, cv2.CAP_PROP_FPS))

# Video writer
video_writer = cv2.VideoWriter("workouts.avi", cv2.VideoWriter_fourcc(*"mp4v"), fps, (w, h))

# Init AIGym
left_leg = solutions.AIGym(
    show=False,  # Display the frame
    kpts=[12, 14, 16],  # keypoints index of person for monitoring specific exercise, by default it's for pushup
    model="yolo11n-pose.pt",  # Path to the YOLO11 pose estimation model file
    line_width=2,  # Adjust the line width for bounding boxes and text display
)

left_arm = solutions.AIGym(
    show=False,  # Display the frame
    kpts=[6, 8, 10],  # keypoints index of person for monitoring specific exercise, by default it's for pushup
    model="yolo11n-pose.pt",  # Path to the YOLO11 pose estimation model file
    line_width=2,  # Adjust the line width for bounding boxes and text display
)

right_leg = solutions.AIGym(
    show=False,  # Display the frame
    kpts=[11, 13, 15],  # keypoints index of person for monitoring specific exercise, by default it's for pushup
    model="yolo11n-pose.pt",  # Path to the YOLO11 pose estimation model file
    line_width=2,  # Adjust the line width for bounding boxes and text display
)

right_arm = solutions.AIGym(
    show=False,  # Display the frame
    kpts=[5, 7, 9],  # keypoints index of person for monitoring specific exercise, by default it's for pushup
    model="yolo11n-pose.pt",  # Path to the YOLO11 pose estimation model file
    line_width=2,  # Adjust the line width for bounding boxes and text display
)



# Process video
# 실시간 카메라 스트림 처리
while cap.isOpened():
    success, im0 = cap.read()
    if not success:
        print("카메라에서 프레임을 읽을 수 없습니다. 스트림 종료.")
        break

    # 프레임 처리
    im0_left_leg = left_leg.monitor(im0)  # leg keypoints를 처리
    im0_left_arm = left_arm.monitor(im0)  # arm keypoints를 처리
    im0_right_leg = right_leg.monitor(im0)  # leg keypoints를 처리
    im0_right_arm = right_arm.monitor(im0)  # arm keypoints를 처리

    # 결과를 통합 (leg와 arm의 시각화를 병합)
    combined_im0 = cv2.addWeighted(im0_left_leg, 0.5,
                                   im0_left_arm, 0.5,0)  # 가중 평균으로 병합

    combined_im1 = cv2.addWeighted(im0_right_leg, 0.5,
                                   im0_left_arm, 0.5, 0)

    result = cv2.addWeighted(combined_im0, 0.5,
                                   combined_im1, 0.5,0)  # 가중 평균으로 병합
    # 화면에 표시
    cv2.imshow("AI Gym Monitoring", result)

    # ESC 키를 누르면 종료
    if cv2.waitKey(1) & 0xFF == 27:  # 27은 ESC 키의 ASCII 코드
        break

# 종료 처리
cap.release()
cv2.destroyAllWindows()

