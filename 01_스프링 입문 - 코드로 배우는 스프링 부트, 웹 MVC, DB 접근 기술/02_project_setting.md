# 프로젝트 환경설정

## 프로젝트 생성
- Java + Spring Boot(Spring Web, Thymeleaf) + Gradle + Jar + IntelliJ/Ecplipse
1) [Spring Initializr](https://start.spring.io)에서 프로젝트 생성
- Project(Gradle Project)
- Language(Java)
- Spring Boot(버전선택)
- Project Metadata 설정
- Dependencies(Spring Web, Thymeleaf)

2) 프로젝트 IntelliJ로 열기
- 생성된 프로젝트 압축 해제
- IntelliJ에서 Open or Import로 build.gradle 불러오기
- Open as a Project 선택

3) 프로젝트 구조 파악
- src > main에 java와 resources가 존재하고 java 하위에 실제 패키지와 소스 파일 존재. resources에 java 코드를 제외한 설정 파일 존재
- src > test에 java가 있고 java 하위에 테스트 코드에 관련된 파일이 존재
- build.gradle은 버전 설정과 라이브러리를 가져오는 설정 파일. repositories는 라이브러리를 다운 받을 사이트 설정. dependencies에서 라이브러리를 가져옴
- .gitignore는 소스파일만 올라갈 수 있도록 설정
- gradelw: 
- gradelw.bat: 
- settings.gradle: 

4) 스프링 프로젝트 실행
- 웹 브라우저에서 localhost:8080 접속하여 확인

+) IntelliJ 설정: Preferences > gradle 에서 Build and run usings와 Run tests using을 IntelliJ IDEA로 변경

## 라이브러리 살펴보기


## View 환경설정


## 빌드하고 실행하기

