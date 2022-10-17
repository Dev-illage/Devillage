# Devillage
- 프로젝트 기간 : 2022.09.16 ~ 2022.10.
- [배포 URL](https://dev-illage.com/)
- [Front Github](https://github.com/yangddoddi/devillage-front)
<details>
    <summary>Preview</summary>

![image](https://user-images.githubusercontent.com/97802103/194878762-84c284c9-c507-46ff-bcc1-92b8d743eb57.png)
![image](https://user-images.githubusercontent.com/97802103/194982365-96b59c55-b8f4-4b46-8191-58475812c37e.png)

</details>
<br>

## Project Goal :golf:

- [x] 기본적인 CRUD 및 좋아요, 댓글, 대댓글, 북마크, 이미지 업로드 등 커뮤니티형 웹 어플리케이션에서 지원하는 기본적인 기능 구현
- [ ] Spring Security를 기반으로 Jwt(AccessToken, RefreshToken)와 OAuth2 인증 구현
- [ ] WebSocket을 활용한 채팅 기능 구현
- [ ] In-Memory DB (Redis)를 활용한 인증, 채팅처리 구현
- [ ] Two-Factor 인증 구현
- [ ] AWS를 이용한 Https 통신 서버 운영
- [ ] CI/CD를 활요안 자동 배포 파이프라인 구축
- [ ] SpringRestDocs를 활용한 API 문서화
- [ ] 최대한의 Restful한 API 설계
<br>

## Members :family:

|양은찬(FE/BE)|김경근(BE)|조규원(BE)|강지원(BE)|
|:--:|:--:|:--:|:--:|
|profile|profile|profile|<img src="https://user-images.githubusercontent.com/102658715/196131971-c2e4bd73-0865-4960-9b1a-3da4fcdb7183.PNG" width="200px" height="200px">|
|깃헙주소|깃헙주소|깃헙주소|[@gangdodan](https://github.com/gangdodan)|


                                                                                                                                         
<br>

## Stacks :wrench:

#### #Back-end 언어 및 프레임워크

<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
<br>
<br>

#### #Dependency

<img src="https://img.shields.io/badge/SpringDataJPA-007396?style=for-the-badge&logo=SpringDataJPA&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Oauth2-DC382D?style=for-the-badge&logo=Oauth2&logoColor=white"> <img src="https://img.shields.io/badge/SpringValidation-6DB33F?style=for-the-badge&logo=SpringValidation&logoColor=white"> <img src="https://img.shields.io/badge/springSecurity-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white">

<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-4479A1?style=for-the-badge&logo=QueryDSL&logoColor=white"> <img src="https://img.shields.io/badge/LOMBOK-4479A1?style=for-the-badge&logo=LOMBOK&logoColor=white"> <img src="https://img.shields.io/badge/SpringRestDocs-4479A1?style=for-the-badge&logo=SpringRestDocs&logoColor=white">
<br>
<br>


#### #Front-end 언어 및 프레임워크

<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white"> <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white">
<br>
<br>

#### #라이브러리

React.js, ReduxToolkit, Toast-Editor, antd, axios, jwt-decode <br />
Redux-persist, SASS, react-cookie, react-router-dom
<br>
<br>

#### #AWS

<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"> <img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white"> <img src="https://img.shields.io/badge/ELB-F7DF1E?style=for-the-badge&logo=ELB&logoColor=white">
<img src="https://img.shields.io/badge/ElasticCache-F7DF1E?style=for-the-badge&logo=ElasticCache&logoColor=white">
<br>
<br>

## Architecture :triangular_ruler:
<br>

## Documents :file_folder:

<details>
    <summary>기획 문서</summary>

- [요건 정의](https://ilyadelavie.notion.site/320d8097e3e943eea675e560bb2b0d1f)
- [정책서](https://ilyadelavie.notion.site/edc37f90d8104e45a7c297f630afd1a1)
- [Flow chart](https://ilyadelavie.notion.site/Flow-Chart-51b10f6d002349ba9296890e0071a333)
- [시나리오 정의서](9f97ae4679f44d37bf517ee9151ed5b8)

</details>

<details>
    <summary>개발 문서</summary>

- [API](https://ilyadelavie.notion.site/d77663d629b9456487067a73397ec29f?v=a12dc9c5377a4e5f972d9eb2a47b20e9)
- [ERD](https://ilyadelavie.notion.site/edc37f90d8104e45a7c297f630afd1a1)
- [Sequence Diagram](https://ilyadelavie.notion.site/Flow-Chart-51b10f6d002349ba9296890e0071a333)
- [클라이언트 통신](9f97ae4679f44d37bf517ee9151ed5b8)

</details>
<br>

## Ground Rule :bulb:
1. 커밋 시 클래스, 메서드 경계를 넘지 않게 최소 단위로 수정한다.
아무리 사소한 것이더라도 해당 메서드나 클래스 경계에 수정할 것이 그 부분 밖에 없다면 그것만 커밋할 것.  
(단, 특정 메서드에 밀접한 관계가 있는 DTO작성 시 같이 커밋할 수 있습니다.)
2. 1개 로직 완성 시마다 PullReqeust
3. 테스트는 반드시 단위테스트로 다른 레이어에 영향을 받지 않아야하며, 한 개 메서드당 실패 테스트도 최소 한 개 이상 포함한다.
4. 컨트롤러 테스트 시 RestDocs도 생성할 것 (HTML 제외)
<br>

## Commit Strategy :sparkles:
|이름|내용|
|:--:|:--|
|Feat|새로운 기능 추가|
|Fix|버그 수정|
|Docs|문서 수정|
|Style|코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우|
|Refactor|코드 리펙토링|
|Test|테스트 코드, 리펙토링 테스트 코드 추가|
|Chore|빌드 업무 수정, 패키지 매니저 수정|


