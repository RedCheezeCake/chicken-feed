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
