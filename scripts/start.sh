#!/bin/bash
APP_PATH="/home/ec2-user/app"
JAR_NAME="backend.jar"

echo "> [Start] 신규 애플리케이션 배포 시작"

# 1. 기존 로그 정리 (용량 관리)
if [ -f $APP_PATH/nohup.out ]; then
    echo "> 기존 로그 파일 백업"
    mv $APP_PATH/nohup.out $APP_PATH/nohup_$(date +%Y%m%d%H%M%S).out
fi

echo "> $JAR_NAME 실행 (Profile: prod)"
# 2. 실행 (Small 인스턴스에 맞게 메모리 1GB 할당)
# -Xms: 시작 메모리, -Xmx: 최대 메모리
nohup java -jar \
    -Xms1024M -Xmx1024M \
    -Dspring.profiles.active=prod \
    $APP_PATH/$JAR_NAME > $APP_PATH/nohup.out 2>&1 &

# 3. 프로세스 정상 실행 여부 확인
sleep 3
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
    echo "> [Error] 애플리케이션 실행 실패! nohup.out 로그를 확인하세요."
    exit 1
else
    echo "> [Success] 실행 완료 (PID: $CURRENT_PID)"
fi