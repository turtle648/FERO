FROM gradle:7.4-jdk-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /build

# 도커 캐시 사용 - 새로운 Gradle만 다운로드
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 스프링 프로젝트 복사
COPY . /build
RUN gradle clean build --parallel 

# Java 11 JDK 가 설치된 Docker 이미지를 기반으로 새 이미지 생성
FROM openjdk:11.0-slim
WORKDIR /app

# 빌드한 jar파일을 /app.jar 로복사
COPY --from=builder /build/build/libs/*.jar /app.jar 

EXPOSE 8076

#  root 권한 x, nobody 권한으로 실행함
USER nobody
ENTRYPOINT [                                                            \
   "java",                                                              \
   "-Dspring.profiles.active=prod",                                     \
   "-Dfile.encoding=UTF-8",                                             \
   "-Djava.security.egd=file:/dev/./urandom",                           \
   "-Dsun.net.inetaddr.ttl=0",                                          \
   "-jar",                                                              \
   "/app.jar"                                                         \
]