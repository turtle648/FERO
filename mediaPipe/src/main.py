import mediapipe as mp
import cv2
import numpy as np
import math

# MediaPipe Pose 초기화
mp_pose = mp.solutions.pose
pose = mp_pose.Pose(
    min_detection_confidence=0.5,
    min_tracking_confidence=0.5,
    model_complexity=1  # 정확도와 속도의 균형을 위해 1로 설정
)

def calculate_angle(a, b, c):
    """세 점 사이의 각도를 계산합니다."""
    a = np.array(a)
    b = np.array(b)
    c = np.array(c)
    
    radians = np.arctan2(c[1]-b[1], c[0]-b[0]) - np.arctan2(a[1]-b[1], a[0]-b[0])
    angle = np.abs(radians*180.0/np.pi)
    
    if angle > 180.0:
        angle = 360-angle
    return angle

def calculate_plank_metrics(landmarks):
    """플랭크 자세의 여러 메트릭을 계산합니다."""
    
    # 관련 랜드마크 추출
    shoulder = [landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].x,
                landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].y]
    elbow = [landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].x,
             landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].y]
    wrist = [landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].x,
             landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].y]
    hip = [landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].x,
           landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].y]
    knee = [landmarks[mp_pose.PoseLandmark.LEFT_KNEE.value].x,
            landmarks[mp_pose.PoseLandmark.LEFT_KNEE.value].y]
    ankle = [landmarks[mp_pose.PoseLandmark.LEFT_ANKLE.value].x,
             landmarks[mp_pose.PoseLandmark.LEFT_ANKLE.value].y]
    
    # 각도 계산
    arm_shoulder_angle = calculate_angle(elbow, shoulder, hip)
    body_angle = calculate_angle(shoulder, hip, ankle)
    
    # 바닥과의 거리 확인
    hip_height = hip[1]
    shoulder_height = shoulder[1]
    elbow_height = elbow[1]
    
    is_on_ground = hip_height > 0.7 and shoulder_height > 0.7 and elbow_height > 0.7
    
    return {
        'arm_shoulder_angle': arm_shoulder_angle,
        'body_angle': body_angle,
        'is_on_ground': is_on_ground
    }

def check_plank(landmarks):
    """플랭크 자세를 확인합니다."""
    metrics = calculate_plank_metrics(landmarks)
    
    # 조건 확인
    conditions = {
        'body_straight': 170 <= metrics['body_angle'] <= 180,  # 몸이 일자
        'arm_angle': 75 <= metrics['arm_shoulder_angle'] <= 85,  # 팔-어깨 각도 약 80도
        'on_ground': metrics['is_on_ground']  # 바닥에 있는지
    }
    
    is_plank = all(conditions.values())
    
    return is_plank, conditions, metrics

# 웹캠 캡처 시작
cap = cv2.VideoCapture(0)

while cap.isOpened():
    success, image = cap.read()
    if not success:
        continue
    
    # BGR을 RGB로 변환
    image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    
    # 포즈 감지
    results = pose.process(image_rgb)
    
# 감지된 포즈가 있다면
    if results.pose_landmarks:
        # 어깨와 몸통 부분만 그리기
        connections = [
            (mp_pose.PoseLandmark.LEFT_SHOULDER, mp_pose.PoseLandmark.RIGHT_SHOULDER),
            (mp_pose.PoseLandmark.LEFT_SHOULDER, mp_pose.PoseLandmark.LEFT_HIP),
            (mp_pose.PoseLandmark.RIGHT_SHOULDER, mp_pose.PoseLandmark.RIGHT_HIP),
            (mp_pose.PoseLandmark.LEFT_HIP, mp_pose.PoseLandmark.RIGHT_HIP),
            (mp_pose.PoseLandmark.LEFT_HIP, mp_pose.PoseLandmark.LEFT_KNEE),
            (mp_pose.PoseLandmark.RIGHT_HIP, mp_pose.PoseLandmark.RIGHT_KNEE),
            (mp_pose.PoseLandmark.LEFT_KNEE, mp_pose.PoseLandmark.LEFT_ANKLE),
            (mp_pose.PoseLandmark.RIGHT_KNEE, mp_pose.PoseLandmark.RIGHT_ANKLE)
        ]
        
        # 선택된 연결선만 그리기
        for connection in connections:
            start_point = results.pose_landmarks.landmark[connection[0].value]
            end_point = results.pose_landmarks.landmark[connection[1].value]
            
            # 좌표를 픽셀로 변환
            h, w, _ = image.shape
            start_point = (int(start_point.x * w), int(start_point.y * h))
            end_point = (int(end_point.x * w), int(end_point.y * h))
            
            cv2.line(image, start_point, end_point, (0, 255, 0), 3)
        
        # 플랭크 자세 확인 - 이 부분을 if문 안으로 이동
        try:
            is_plank, conditions, metrics = check_plank(results.pose_landmarks.landmark)
            
            # 상태 표시
            y_pos = 30
            status = "Perfect Plank!" if is_plank else "Not a plank"
            color = (0, 255, 0) if is_plank else (0, 0, 255)
            cv2.putText(image, f'Status: {status}', (10, y_pos), 
                        cv2.FONT_HERSHEY_SIMPLEX, 1, color, 2)
            y_pos += 30
            
            cv2.putText(image, f'Body angle: {metrics["body_angle"]:.1f}°', 
                        (10, y_pos), cv2.FONT_HERSHEY_SIMPLEX, 1, 
                        (0, 255, 0) if conditions['body_straight'] else (0, 0, 255), 2)
            y_pos += 30
            
            cv2.putText(image, f'Arm angle: {metrics["arm_shoulder_angle"]:.1f}°', 
                        (10, y_pos), cv2.FONT_HERSHEY_SIMPLEX, 1,
                        (0, 255, 0) if conditions['arm_angle'] else (0, 0, 255), 2)
        except Exception as e:
            cv2.putText(image, "Error in pose analysis", (10, 30), 
                        cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
    else:
        # 포즈가 감지되지 않았을 때 메시지 표시
        cv2.putText(image, "No pose detected", (10, 30), 
                    cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

    # 화면에 표시
    cv2.imshow('Plank Detection', image)
    
    if cv2.waitKey(5) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()