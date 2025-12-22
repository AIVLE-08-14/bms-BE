#!/bin/bash
# 실행 중인 backend.jar 프로세스 종료
PID=$(pgrep -f backend.jar)

if [ -z "$PID" ]; then
    echo "> 현재 실행 중인 애플리케이션이 없습니다."
else
    echo "> 기존 프로세스 종료: kill -15 $PID"
    kill -15 $PID
    sleep 5
fi