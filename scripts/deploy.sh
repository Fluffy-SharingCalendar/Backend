#!/bin/bash

cd /home/ec2-user/Backend

DOCKER_APP_NAME=fluffy

# 실행중인 blue가 있는지 확인
# 프로젝트의 실행 중인 컨테이너를 확인하고, 해당 컨테이너가 실행 중인지 여부를 EXIST_BLUE 변수에 저장
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# 로그 파일 경로
LOG_FILE="/home/ec2-user/deploy.log"

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

# 블루 인스턴스가 없으면 블루 배포
if [ -z "$EXIST_BLUE" ]; then
    echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build
    # 배포 후 안정화 시간 대기
    sleep 30  

    # 블루 상태 확인
    BLUE_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

    # 블루가 비정상 상태면 에러 처리
    if [ -z "$BLUE_HEALTH" ]; then
        echo "블루 배포 실패: $(date)" >> $LOG_FILE
        # Docker 로그 추가
        sudo docker logs ${DOCKER_APP_NAME}-blue 2>&1 | tee -a $LOG_FILE

        # GitHub 이슈 생성
        LOG_CONTENT=$(tail -n 50 $LOG_FILE)

        curl -X POST -H "Authorization: token $GITHUB_TOKEN" \
             -H "Content-Type: application/json" \
             -d '{
                   "title": "[RunTimeError] Blue Deployment Error Detected",
                   "body": "An error occurred during Blue deployment at '"$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"'. Please check the server logs for details.",
                   "labels": ["bug", "deployment"]
                 }' \
             https://api.github.com/repos/$REPO/issues

  # blue가 현재 실행되고 있는 경우에만 green을 종료
    else
      # 로그 파일에 "green 중단 시작"이라는 내용을 추가
      echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

      # docker-compose.green.yml 파일을 사용하여 spring-green 프로젝트의 컨테이너를 중지
      sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

      # 사용하지 않는 이미지 삭제
      sudo docker image prune -af

      echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
    fi

# blue가 실행중이면 green up
else
	  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
	  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

    sleep 30

    GREEN_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)

    if [ -z "$GREEN_HEALTH" ]; then
        echo "그린 배포 실패: $(date)" >> $LOG_FILE
        sudo docker logs ${DOCKER_APP_NAME}-green 2>&1 | tee -a $LOG_FILE

        LOG_CONTENT=$(tail -n 50 $LOG_FILE)

        curl -X POST -H "Authorization: token $GITHUB_TOKEN" \
             -H "Content-Type: application/json" \
             -d '{
                   "title": "[RunTimeError] Green Deployment Error Detected",
                   "body": "An error occurred during Green deployment at '"$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"'. Please check the server logs for details.,
                   "labels": ["bug", "deployment"]
                 }' \
             https://api.github.com/repos/$REPO/issues

    else

      echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

      sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down

      sudo docker image prune -af

      echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
    fi
fi
