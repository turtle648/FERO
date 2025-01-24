import cv2 # openCV
import mediapipe as mp
import numpy as np

class AIGym():
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.stage = None
        self.counter = 0
        self.mp_pose = mp.solutions.pose
        self.pose = self.mp_pose.Pose(
            min_detection_confidence=0.5,
            min_tracking_confidence=0.5
        )
        self.mp_drawing = mp.solutions.drawing_utils

    def calculate_angle(self, a, b, c):
        # 각 값을 받아 넘파이 배열로 변환
        a = np.array(a)  # First
        b = np.array(b)  # Mid
        c = np.array(c)  # End

        # 세 좌표를 가지고 각도로 변환하는 코드
        radians = np.arctan2(c[1] - b[1], c[0] - b[0]) - np.arctan2(a[1] - b[1], a[0] - b[0])
        angle = np.abs(radians * 180.0 / np.pi)

        # 180도가 넘으면 360에서 뺀 값을 계산한다.
        if angle > 180.0:
            angle = 360 - angle

        return angle

    def monitor(self, im0):
        image = cv2.cvtColor(im0, cv2.COLOR_BGR2RGB)
        image_height, image_width, _ = im0.shape

        image.flags.writeable = False
        tracks = self.pose.process(image)
        # image.flags.writeable = True
        #
        # image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        if not tracks.pose_landmarks:
            print("설정 오류 ㅎ")
        print(
            f'LEFT_SHOULDER coordinates: ('
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER].x}, '
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER].y})'
        )

        print(
            f'LEFT_ELBOW coordinates: ('
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ELBOW].x}, '
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ELBOW].y})'
        )

        print(
            f'LEFT_WRIST coordinates: ('
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_WRIST].x}, '
            f'{tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_WRIST].y})'
        )

        shoulder = [tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER].x,
                    tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_SHOULDER].y]
        elbow = [tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ELBOW].x,
                 tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_ELBOW].y]
        wrist = [tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_WRIST].x,
                 tracks.pose_landmarks.landmark[self.mp_pose.PoseLandmark.LEFT_WRIST].y]


        angle = self.calculate_angle(shoulder, elbow, wrist)
        print(f"angle: {angle}")

        # cv2.putText(image, str(angle),
        #             tuple(np.multiply(elbow, [640, 480]).astype(int)),
        #             cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2, cv2.LINE_AA
        # )

        if angle > 160:
            self.stage = "down"
        if angle < 30 and self.stage == 'down':
            self.stage = "up"
            self.counter += 1
            print(self.counter)

        print(shoulder)
        return self.stage, self.counter