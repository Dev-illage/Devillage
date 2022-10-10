## 개발 목표

1. 기본적인 CRUD 및 좋아요, 댓글, 대댓글, 북마크, 이미지 업로드 등 커뮤니티형 웹 어플리케이션에서 지원하는 기본적인 기능 구현
2. Spring Security를 기반으로 Jwt(AccessToken, RefreshToken)와 OAuth2 인증 구현
3. WebSocket을 활용한 채팅 기능 구현
4. In-Memory DB (Redis)를 활용한 인증, 채팅처리 구현
5. Two-Factor 인증 구현
6. AWS를 이용한 Https 통신 서버 운영
7. CI/CD를 활요안 자동 배포 파이프라인 구축

## 기술 스택

### 백엔드

#### 언어 및 프레임워크

Java, SpringBoot

#### 디펜던시

SpringDataJPA, SpringDataRedis, Oauth2, Spring-Validation, LOMBOK
SpringRestDocs, Spring Security, MySQL, QueryDSL

### 프론트엔드

#### 언어 및 프레임워크

Javascript(React.js)

#### 라이브러리

React.js, ReduxToolkit, Toast-Editor, antd, axios, jwt-decode
Redux-persist, SASS, react-cookie, react-router-dom, 

### AWS

EC2, S3, Route53, ELB, ElasticCache