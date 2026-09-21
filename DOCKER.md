# Docker

## Build 가이드

```bash
# RDS 없이 로컬 테스트용 (H2)
docker build -t petclinic-was:test .

# RDS 없는 상태에서 MySQL 연결 실패(503) 재현용
docker build --build-arg DB_PROFILE=MySQL -t petclinic-was:mysql-test .

# 실제 RDS 연동용 (MySQL)
docker build --build-arg DB_PROFILE=MySQL -t petclinic-was:latest .
```

## Run

```bash
# 로컬 테스트 (H2)
docker run -d --name petclinic-test -p 8081:8080 petclinic-was:test

# RDS 없는 상태에서 MySQL 연결 실패(503) 재현
docker run -d --name petclinic-test -p 8081:8080 petclinic-was:mysql-test

# RDS 연동
docker run -d --name petclinic -p 8080:8080 \
  -e JAVA_OPTIONS="-Djdbc.url=jdbc:mysql://<RDS_ENDPOINT>:3306/petclinic -Djdbc.username=<USER> -Djdbc.password=<PASSWORD>" \
  petclinic-was:latest
```

접속: [http://localhost:8081](http://localhost:8081) (테스트) / [http://localhost:8080](http://localhost:8080) (RDS 연동)

## 컨테이너 재실행 (이름 충돌 시)

같은 이름(`petclinic-test`, `petclinic`)으로 재실행하려면 기존 컨테이너 삭제 후 실행

```bash
docker rm -f petclinic-test
docker run -d --name petclinic-test -p 8081:8080 petclinic-was:test
```

## RDS 연동 시 (이미지 재빌드 불필요)

`petclinic-was:mysql-test` 또는 `petclinic-was:latest` 이미지는 그대로 두고, 컨테이너만 지우고 실제 RDS 정보로 재실행

```bash
docker rm -f petclinic-test
docker run -d --name petclinic-test -p 8081:8080 \
  -e JAVA_OPTIONS="-Djdbc.url=jdbc:mysql://<RDS_ENDPOINT>:3306/petclinic -Djdbc.username=<USER> -Djdbc.password=<PASSWORD>" \
  petclinic-was:mysql-test
```

## 로그 확인

```bash
docker logs petclinic-test
docker logs -f petclinic-test   # 실시간
```

## 정리

```bash
docker stop petclinic-test
docker rm petclinic-test
```
