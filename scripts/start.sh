#!/bin/bash
APP_PATH="/home/ec2-user/app" # 경로가 bms인 것으로 보여 수정했습니다.
JAR_NAME="backend.jar"

echo "> [Start] 신규 애플리케이션 배포 시작"

# 1. 대상 디렉토리 및 JAR 권한 강제 부여
# 권한 문제 해결을 위해 실행 전 소유권과 권한을 다시 한번 설정합니다.
sudo chown -R ec2-user:ec2-user $APP_PATH
chmod +x $APP_PATH/$JAR_NAME

# 2. 기존 로그 정리
if [ -f $APP_PATH/nohup.out ]; then
    echo "> 기존 로그 파일 백업"
    mv $APP_PATH/nohup.out $APP_PATH/nohup_$(date +%Y%m%d%H%M%S).out
fi

echo "> $JAR_NAME 실행 (Profile: prod)"

# 3. 실행 (백그라운드 유지를 위해 nohup은 쓰되, 권한 문제를 위해 명시적 경로 사용)
# 만약 '그냥 실행'하고 싶다면 아래 줄에서 nohup과 끝의 &를 빼면 되지만,
# 배포가 끝나지 않으므로 CodeDeploy에서는 아래 방식이 필수입니다.
nohup java -jar \
    -Xms1024M -Xmx1024M \
    -Dspring.profiles.active=prod \
    $APP_PATH/$JAR_NAME > $APP_PATH/nohup.out 2>&1 &

# 4. 프로세스 확인
sleep 5
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
    echo "> [Error] 실행 실패! 로그 확인: tail -n 50 $APP_PATH/nohup.out"
    exit 1
else
    echo "> [Success] 실행 완료 (PID: $CURRENT_PID)"
fi