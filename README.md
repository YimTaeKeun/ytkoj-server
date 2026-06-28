# YTK Online Judge
> 생성형 AI가 생성하는 여러 알고리즘 문제들을 풀어볼 수 있는 온라인 코드 채점 서비스.

> 해당 레포지토리는 Ytk Online Judge의 Server 레포지토리 입니다.

## 사이트 주소
https://ac.ytkoj.org

## 사용한 기술
### Front-End
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white) ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white) ![Styled Components](https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)
### Back-End
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Bash Script](https://img.shields.io/badge/bash_script-%23121011.svg?style=for-the-badge&logo=gnu-bash&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54) ![Celery](https://img.shields.io/badge/celery-%23a9cc54.svg?style=for-the-badge&logo=celery&logoColor=ddf4a4)

### Infrastructure
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)

## 프로젝트 구조
```text
/src/main/java/com/ytk/ytkoj
 ├── src/
 │    ├── main/
 │    │    ├── java/com/ytk/ytkoj/
 │    │    │    ├── domain/        # 도메인별 비즈니스 로직
 │    │    │    │    ├── auth/            # 로그인/회원가입 (소셜 로그인)
 │    │    │    │    ├── problem/         # 문제 관리 및 문제 생성기로 부터 데이터 수집
 │    │    │    │    ├── submission/      # 문제 제출 관리 및 채점 서버에 데이터 전송
 │    │    │    │    └── usr/             # 사용자 정보 관리
 │    │    │    ├── global/               # 공통 설정 (예외 처리, AOP, 보안, 개발 편의 클래스 등)
 │    │    │    └── YtkojApplication.java
 │    │    └── resources/
 │    │         └── application.properties # 애플리케이션 설정파일
 │    └── test/                           # 테스트 코드
 ├── .env                   # 환경변수
 └── build.gradle

```


## 실행 방법
### Back-End (Spring Boot)
#### 환경 설정
- 아래 .env 텍스트는 예시일 뿐이며, 사용자의 환경에 맞게 변경 적용합니다.
```text
# 환경 설정 개발 환경: dev, 배포 환경: prod
PROFILE=dev


# DB 설정
DB_HOST=localhost
DB_PORT=1234
DB_USER_NAME=exuser
DB_PASSWORD=ps
DB_NAME=oj

# 로그인 API 키
KAKAO_REST_API_KEY=<카카오 Rest api 키>
KAKAO_CLIENT_SECRET=<카카오 클라이언트 시크릿>

NAVER_CLIENT_ID=<네이버 클라이언트 아이디>
NAVER_CLIENT_SECRET=<네이버 클라이언트 시크릿>

# JWT 설정
JWT_SECRET_KEY=<JWT 시크릿 키>

# 접속 허용 ORIGIN (각 ORIGIN은 쉼표로 구분, 공백 X)
ALLOWED_ORIGIN_DEV=*
ALLOWED_ORIGIN_PROD=localhost,local
```
#### 실행
```bash
git clone https://github.com/YimTaeKeun/ytkoj-server.git
cd 클론한 폴더/
# .env 작성
vi .env
# 빌드
./gradlew build
```
