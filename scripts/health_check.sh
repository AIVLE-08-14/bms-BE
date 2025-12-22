#!/bin/bash

# 1. 포트 확인
echo "> Health check 시작..."
for retry_count in {1..10}
do
  echo "> 접속 시도 ($retry_count/10)..."
  # 8080 포트로 요청을 보내고 HTTP 상태 코드를 가져옴
  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)

  if [ $RESPONSE -eq 200 ]; then
    echo "> Health check 성공! 서버가 정상적으로 실행 중입니다."
    exit 0
  else
    echo "> 응답 없음 (상태 코드: $RESPONSE). 10초 후 재시도..."
  fi
  sleep 10
done

echo "> Health check 실패. 서버 실행에 문제가 있습니다."
exit 1