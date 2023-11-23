# 🐱 톰캣 구현하기 🐱

## 🔍 진행 방식

- 미션은 **기능 요구 사항, 미션 진행 요구 사항** 두 가지로 구성되어 있다.
- 두 개의 요구 사항을 만족하기 위해 노력한다. 특히 기능을 구현하기 전에 기능 목록을 만든다.
- 기능 요구 사항에 기재되지 않은 내용은 스스로 판단하여 구현한다.

## 🚀 `step1`: 미션 설명

간단한 HTTP 서버를 만들어보자.

저장소에서 소스코드를 받아와서 메인 클래스를 실행하면 HTTP 서버가 실행된다.

웹브라우저로 로컬 서버(http://localhost:8080)에 접속하면 Hello world!가 보인다.

정상 동작을 확인했으면 새로운 기능을 추가해보자.

## ⚙️ 기능 요구 사항

### 0.기본 서버 제작

- 1~3 번의 사항에 외부 라이브러리가 아닌 순수 자바를 사용한다.
  서버가 시작하는 단계 : Tomcat Start
1. Java socket 객체를 생성한다. java.net 의 패키지를 사용한다.
2. 생성한 소켓 객체를 담는 therad 객체를 생성하고 demon 설정 후 스레드 start() 를 호출한다.
3. System.in.read() 메서드를 호출하여 시스템 입력을 받습니다.


### 1. GET /index.html 응답하기

인덱스 페이지(http://localhost:8080/index.html)에 접근할 수 있도록 만들자.

Http11ProcessorTest 테스트 클래스의 모든 테스트를 통과하면 된다.

브라우저에서 요청한 HTTP Request Header는 다음과 같다.

```text
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
- [x] process 의 socket 의 inputstream 의 buffer 를 읽어와 String 으로 변환한다.
- [x] request resource 경로를 파악하여 응답값을 생성한다.
- [x] 잘못된 경로 일시 404 를 반환한다.
- [x] 오류 메시지인 svg 이미지를 gzip 후 반환한다.

### 2. CSS 지원하기

인덱스 페이지에 접속하니까 화면이 이상하게 보인다.

개발자 도구를 열어서 에러 메시지를 체크해보니 브라우저가 CSS를 못 찾고 있다.

사용자가 페이지를 열었을 때 CSS 파일도 호출하도록 기능을 추가하자.


- 리다리렉션을 구현하지 않는다. :  많은 웹 페이지들이 최초요청 200 으로 응답받아 css, html 을 전부 처리한다.

- 파싱을 하면서 DOM Tree를 만들게 되는데, 파싱 도중에 link태그를 만나면 서버로 CSS파일 요청을 보낸다.
  다운받은 CSS를 파싱해서 CSSOM Tree를 만든다.

- [x] /css/styles.css 로 들어온 요청의 응답하는 css를 반환한다.

```text
GET /css/styles.css HTTP/1.1
Host: localhost:8080
Accept: text/css,*/*;q=0.1
Connection: keep-alive
```

### 3. Query String 파싱

http://localhost:8080/login?account=gugu&password=password으로 접속하면 로그인 페이지(login.html)를 보여주도록 만들자.

그리고 로그인 페이지에 접속했을 때 Query String을 파싱해서 아이디, 비밀번호가 일치하면 회원을 조회한 결과가 나오도록 만들자.

- [x] index.html 에서 요청하는 js,asset 를 반환환다.
- [x] login.html 의 최초 진입시 html 의 반환한다.
- [x] 회원 가입 페이지, 로그인 페이지로 이동한다.
- [x] 쿼리스트링의 사용자 입력정보를 받아 DB 와 비교한다. 

- 유저 관련 기능
- [x] 유저의 id 를 생성한다.
- [x] 유저를 저장한다.
- [x] 유저를 조회하고 없으면 문자열을 반환한다.


# 🐱 톰캣 구현하기 2단계 - 로그인 구현하기

## 🚀 미션 설명

서블릿을 도입해서 동적 페이지를 만들 수 있게 되었다.

이제 로그인과 회원가입 기능을 추가해보자.

로그인에 필요한 쿠키와 세션도 같이 구현해보자.

## ⚙️ 기능 요구 사항

### 1. HTTP Status Code 302

로그인 여부에 따라 다른 페이지로 이동시켜보자.

/login 페이지에서 아이디는 gugu, 비밀번호는 password를 입력하자.

로그인에 성공하면 응답 헤더에 http status code를 302로 반환하고 /index.html로 리다이렉트 한다.
로그인에 실패하면 401.html로 리다이렉트한다.

- [x] header 에 location 추가
- [] 정적 파일을 반환하는 부분은 어디인가 ?
- spring 의 controller 
- resource

### 2. POST 방식으로 회원가입

http://localhost:8080/register으로 접속하면 회원가입 페이지(register.html)를 보여준다.

회원가입 페이지를 보여줄 때는 GET을 사용한다.

회원가입을 버튼을 누르면 HTTP method를 GET이 아닌 POST를 사용한다.

회원가입을 완료하면 index.html로 리다이렉트한다.

로그인 페이지도 버튼을 눌렀을 때 GET 방식에서 POST 방식으로 전송하도록 변경하자.

- [x] post 로 로그인.
- [x] index.html 로 리다이렉트

### 3. Cookie에 JSESSIONID 값 저장하기

- 외부 라이브러리인 : jakarta.servlet.http.HttpSession 을 이용한다.

로그인에 성공하면 쿠키와 세션을 활용해서 로그인 상태를 유지해야 한다.

HTTP 서버는 세션을 사용해서 서버에 로그인 여부를 저장한다.
세션을 구현하기 전에 먼저 쿠키를 구현해본다.

자바 진영에서 세션 아이디를 전달하는 이름으로 JSESSIONID를 사용한다.

서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가하고 JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46 형태로 값을 전달하면 클라이언트 요청 헤더의 Cookie 필드에 값이 추가된다.

서버로부터 쿠키 설정된 클라이언트의 HTTP Request Header 예시

```text
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
Cookie: yummy_cookie=choco; tasty_cookie=strawberry; JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46
```

Cookie 클래스를 추가하고 HTTP Request Header의 Cookie에 JSESSIONID가 없으면 HTTP Response Header에 Set-Cookie를 반환해주는 기능을 구현한다.

```text
HTTP/1.1 200 OK 
Set-Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46
Content-Length: 5571
Content-Type: text/html;charset=utf-8;
```

### 4. Session 구현하기

쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.
로그인에 성공하면 Session 객체의 값으로 User 객체를 저장해보자.

그리고 로그인된 상태에서 /login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.


## ✏️ 미션 진행 요구 사항

- 미션은 [jwp-dashboard-http-mission](https://github.com/speculatingwook/jwp-dashboard-http-mission) 저장소를 Fork & Clone해 시작한다.
- **기능을 구현하기 전 `README.md`에 구현할 기능 목록을 정리**해 추가한다.
