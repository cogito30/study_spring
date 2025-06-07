# 06 회원 관리 예제 - 웹 MVC 개발

## 회원 웹 기능 - 홈 화면 추가
1) src > main > java > hello.hellospring.controller에 HomeController(class) 생성
2) src > main > resources > templates에 home.html 생성
3) 웹브라우저에서 localhost:8080로 접속

(html 우선 순위)
1) 컨테이너에 등록된 html 파일 탐색
2) 없다면 static에서 확인(기본: index.html)

## 회원 웹 기능 - 등록
1) src > main > java > hello.hellospring.controller의 MemberController(class)에 코드 추가
2) src > main > resources > templates에 members(directory) 생성
3) src > main > resources > templates > members에 createMemberForm.html 생성
4) src > main > java > hello.hellospring.controller에 MemberForm(class) 생성
5) src > main > java > hello.hellospring.controller의 MemberController(class)에 코드 추가

## 회원 웹 기능 - 조회
1) src > main > java > hello.hellospring.controller의 MemberController에 코드 추가
2) src > main > resources > templates > members에 memberList.html 생성
3) 웹 페이지 접속 후 회원 등록(spring1, spring2)
4) 웹 페이지에서 회원 목록 확인
- `<td th:text="${member.name}"></td>`에서 id, name은 private이므로 getter/setter에 접근하여 출력