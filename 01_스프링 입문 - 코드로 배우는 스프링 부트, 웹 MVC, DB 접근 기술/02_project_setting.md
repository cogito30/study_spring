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
- Gradle은 의존관계가 있는 라이브러리를 함께 다운로드함
- 좌측 탭의 External Libraries에서 라이브러리 확인
- 우측 탭의 Gradle > Dependencies에서 라이브러리의 의존관계 확인
- System.out.println 대신 log로 출력해야함
- spring-boot-starter-web
  - spring-boot-starter-tomcat: 톰캣(웹서버)
  - spring-webmvc: 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타입리프 템플릿 엔진(View)
- spring-boot-starter: 스프링 부트 + 스프링 코어 + 로깅
  - spring-boot > spring-core
  - spring-boot-starter-logging > logback, slf4j
- spring-boot-starter-test
  - junit: 테스트 프레임워크
  - mockito: 목 라이브러리
  - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
  - spring-test: 스프링 통한 테스트 지원

## View 환경설정
(Welcome page 기능)
1) resources > static에 index.html 생성
2) 프로젝트 실행 후 브라우저에서 localhost:8080 접속

- java Enterprise 환경에서 Spring이 대부분. 필요한 내용은 빠르게 검색하는 능력 필요
- Spring 페이지에 접속하여 Reference Doc에서 필요한 내용 검색하여 사용

(thymeleaf 템플릿 엔진 사용)
- 공식 사이트 및 spring 공식 사이트, spring boot 메뉴얼 확인
1) src > main > java > hello.hellospring.controller(package) 생성
2) src > main > java > hello.hellospring.controller에 HelloController(class) 생성
3) src > resources > templates에 hello.html 생성
- model과 thymeleaf를 통해서 html에 데이터 삽입

(thymeleaf 템플릿 엔진 동작 과정)
1) 웹 브라우저에서 localhost:8080/hello를 던짐
2) 내장 톰켓 서버에서 url 주소를 파싱하여 Controller에서 url이 일치하는 메서드를 실행
3) spring에서 model을 만들어서 메서드에 넘겨줌
4) Controller에서 리턴값으로 문자를 반환하면 View Resolver가 resources > templates 하위의 html 파일을 찾아서 처리
5) html을 반환
+) spring-boot-devtools 라이브러리 추가시 html 파일을 컴파일만 해주면 서버 재시작 없이 view 파일 변경 가능. IntelliJ 컴파일 방법은 build > recompile

## 빌드하고 실행하기
1) `./gradlew build` 명령어로 빌드
2) `cd build/libs`로 폴더 이동
3) `java -jav ~.jar`로 빌드된 파일 실행
+) `./gradlew clean build` 명령어로 재빌드