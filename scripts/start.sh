#!/bin/bash
APP_PATH="/home/ec2-user/app"
JAR_NAME="backend.jar"

echo "> 신규 애플리케이션 실행"
# prod 프로필로 백그라운드 실행
nohup java -jar -Dspring.profiles.active=prod $APP_PATH/$JAR_NAME > $APP_PATH/nohup.out 2>&1 &

echo "> 실행 완료"