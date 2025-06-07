# 03 스프링 웹 개발 기초

## 정적 컨텐츠
- 정적 컨텐츠: 파일을 그대로 웹 브라우저에 내려주는 것
- Spring Boot 문서의 Static Content 설명서 확인
1) src > main > resources > static에 hello-static.html 생성
2) 웹 브라우저에서 localhost:8080/hello-static.html 접속

(정적 컨텐츠 동작 과정)
1) 웹 브라우저에서 url로 접속
2) 스프링 부트의 내장 톰켓 서버에서 url을 받아서 url 파싱
3) url 파싱과 같은 주소가 Controller에 있는지 탐색
4) 없을 경우 resources > static 안의 html 파일 탐색 후 반환

## MVC와 템플릿 엔진
- 템플릿 엔진: HTML을 동적으로 내림
- MVC: Model, View, Controller
- Model1 방식은 View에 모든 것을 작성. JSP 사용

1) src > main > java > hello.hellospring.controller의 HelloController(class)에 코드 추가
2) src > main > resources > static에 hello-template.html 생성
3) 웹 브라우저에서 localhost:8080/hello-mvc?name=spring!!! 접속
- name 쿼리가 없다면 에러 페이지

+) cmd + P로 옵션 정보 확인

(MVC 템플릿 엔진 동작 과정)
1) 웹 브라우저에서 url로 접속
2) 스프링 부트의 내장 톰켓 서버에서 url을 받아서 url 파싱
3) url 파싱을 통해 얻은 주소가 Controller에 있는지 탐색
4) 있는 경우 viewResolver에서 html 파일을 찾아서 데이터를 넣어 html을 렌더링해서 반환

## API
- API: JSON 데이터 포맷으로 클라이언트에게 데이터를 전달하는 방식
- Vue, React 클라이언트 혹은 서버끼리 통신할 경우 API 사용

1) src > main > java > hello.hellospring.controller의 HelloController(class)에 코드 추가
- @ResponseBody를 사용하면 viewResolver를 사용하지 않고 HTTP의 BODY에 문자내용을 직접 반환
2) 웹 브라우저에서 localhost:8080/hello-string?name=spring!!! 접속
3) 페이지 소스 보기로 확인시 html 태그가 없고 문자열 그대로 반환
4) src > main > java > hello.hellospring.controller의 HelloController(class)에 코드 추가
5) 웹 브라우저에서 localhost:8080/hello-api?name=spring!!! 접속
6) 페이지 소스 보기로 확인시 html 태그가 없고 json 방식으로 반환
- XML 방식은 무겁고 JSON은 가벼운 편. 주로 JSON을 사용하는 추세
- @RequestBody 사용시 viewResolver 대신 HttpMessageConverter가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter
- byte 처리 등 여러 HttpMessageConverter가 기본으로 등록되어 있음

+) cmd + M으로 getter/setter 설정

(API 동작 방식)
1) 웹 브라우저에서 url로 접속
2) 스프링 부트의 내장 톰켓 서버에서 url을 받아서 url 파싱
3) url 파싱을 통해 얻은 주소가 Controller에 있는지 탐색
4) @ResponseBody가 붙어있고 객체가 없는(기본 문자) 경우 HttpMessageConverter(StringConverter)를 사용해서 HTTP의 BODY에 문자 내용을 직접 반환
4) @ResponseBody가 붙어있고 객체가 있는 경우 HttpMessageConverter(JsonConverter)를 사용해서 JSON 형태로 반환

+) 클라이언트의 HTTP Accept 헤더와 서버의 컨트롤러 반환 타입 정보를 조합해서 HttpMessageConverter가 선택됨
