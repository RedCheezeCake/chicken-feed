# 개발 규칙
* 개발 과정에서 지킬 규칙들을 나열
* 포맷 :  
```
* \#{번호}. {제목}  
	* {내용}
	* 출처 : {출처 url}
```
* example

* \#1. do something.
	* do something not stay.
	* 출처 : http://stackoverflow.com/...

### Naming Rules
* \#1. 패키지 이름은 복수? 단수?
	* Use *the plural* for packages with **homogeneous contents** and  
	*the singular* for packages with **heterogeneous contents**.
	* 출처 : https://softwareengineering.stackexchange.com/questions/75919/should-package-names-be-singular-or-plural
* \#2. 패키지 이름은 모두 소문자
	* 패키지 이름은 모두 소문자로 구성
	* 복수 개의 단어로 구성된 패키지는 ~~_로 구분하거나~~ 다 붙혀서 씀 
	* 출처 : https://google.github.io/styleguide/javaguide.html#s5.2.1-package-names
* \#3. 클래스 네이밍
	* 클래스 이름은 명사(구)로 지정 (ex. `Character`, `ImmutableList`)
	* 가끔 형용사을 사용할 수 있음 (ex. `Readable`)
	* 테스트 클래스는 대상 클래스에 `Test` 를 붙임 (ex. `Hash` -> `HashTest`) 
	* 출처 : https://google.github.io/styleguide/javaguide.html#s5.2.2-class-names
* \#4. 메소드 네이밍
	* 메소드 이름은 동사(구)로 지정
	* 테스트 메소드는 대상 메소드에 `_`와 로직적 상태를 붙임 `<methodUnderTest>_<state>` (ex. `pop_emptyStack`) 
	* 출처 : https://google.github.io/styleguide/javaguide.html#s5.2.3-method-names
	
### Design Rules
* \#1. REST API 리소스 디자인
	* Document resource 
		* 객체 명이나 DB 레코드 명처럼 단일 컨셉
		* 단수형으로 디자인
		* 리소스의 타입은 응답 헤더에 `Content-Type`을 통해서 명시
		* ex. `http://api.soccer.restapi.org/leagues/seattle/teams/trebuchet`, `Content-Type: application/xml`
	* Collection resource 
		* 서버가 관리(server-managed)하는 리소스의 디렉토리 
		* 복수형으로 디자인
		* 클라이언트가 컬렉션에 새로운 리소스를 추가하려고해도, 실제 추가 여부는 컬렉션에 달려있음
		* ex. `http://api.soccer.restapi.org/leagues/seattle/teams` 
	* Store resource 는 클라이언트가 관리(client-managed)하는 리소스 저장소
		* 클라이언트가 삽입, 수정, 삭제하는 리소스
		* Store 의 URL 는 생성되지 않지만, 각 Stored Resource 에 해당하는 URL 이 있음
		* 복수형으로 디자인 
		* ex. `PUT /users/1234/favorites/alonso` (`favorites` : Store, `alonso` : resource)
	* Controller resource
		* 파라미터 값으로 실행 가능한 함수 역할을 하여 값을 반환하는 리소스 (input, output)
		* URL 의 마지막에 동사로 디자인
		* ex. `POST /alerts/245743/resend` 
	* 출처 : 
		* https://stackoverflow.com/questions/27121749/confusion-between-noun-vs-verb-in-rest-urls/27122233#27122233
		* https://spoqa.github.io/2013/06/11/more-restful-interface.html
		
