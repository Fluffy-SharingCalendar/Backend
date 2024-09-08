# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 복사할 파일 위치 설정
ARG JAR_FILE=*.jar

# 파일 복사
COPY ${JAR_FILE} fluffy.jar

# 포트 설정
EXPOSE 8080

# 커맨드 실행
ENTRYPOINT ["java","-jar","/fluffy.jar"]