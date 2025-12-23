# stop.sh 예시
#!/bin/bash
PID=$(pgrep -f backend.jar)
if [ -z "$PID" ]; then
    echo "> 현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
    exit 0 # 에러 없이 종료
else
    echo "> kill -15 $PID"
    kill -15 $PID
    sleep 5
fi