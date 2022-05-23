# SOLID Principles
## 원칙이 나왔던 배경과 왜 우리는 이 원칙들을 고려해야하는지
Robert C. Martin 에 의해 소개되었다.

SOLID가 무엇이고, 어떻게 더 나은코드를 쓸 수 있게 도와주느냐?
이 원칙들을 설계함으로써 유지보수성이 좋아지고, 쉽게 이해할 수 있게 해주며, 유연한 소프트웨어를 만들게 해준다. 동시에 애플리케이션의 규모가 커질 때, 복잡성을 줄일 수 있고,  그 뒤로 앓게 될 골칫거리들을 줄일 수 있다.


1. **S**ingle Responsibility
2. **O**pen / Closed
3. **L**iskov Substitution
4. **I**nterface Segregation
5. **D**ependency Inversion


### Single Responsibility
**클래스는 오직 하나의 책임만 갖는다. 게다가 이 클래스를 변경할 이유는 오직 하나여야한다.**

이 원칙을 지켰을 때 이점
1. Testing: 하나의 역할만 가졌을 때 훨씬 적은 테스트 케이스를 가지게된다.
2. Lower coupling: 단일 클래스의 기능이 적을수록 종속성이 줄어든다.
3. Organization: 더 작게, 잘 조직화된 클래스들은 모놀리식 클래스들보다 검색하기 더 쉽다.


### Open for Extension, Closed for Modification
클래스들은 확장에는 열려있고, 변경에는 닫혀있어야한다. 그렇게 함으로써, 기존코드를 수정하고, 애플리케이션 내의 잠재적인 새로운 버그를 일으키는 것을 방지한다.

물론, 이 원칙의 한가지 예외는 기존코드에서 버그를 수정할 때다.


### Liskov Substitution
**클래스 A가 클래스 B의 하위타입일 경우, 프로그램 동작을 방해하지 않고, 클래스 B를 A로 대체할 수 있어야한다.**


### Interface Segregation
**큰 인터페이스보다 작게 분리된 여러개의 인터페이스가 낫다.**

### Dependency Inversion
의존의 역전은 소프트웨어 모듈간의 분리를 나타낸다.



설계하다가 의문이었던 점.
Service 계층을 인터페이스와 그를 상속받는 클래스로 나누었는데, 사실 인터페이스의 목적중 하나가 구현체를 변경해도 애플리케이션이 동작하는데 영향이 없기위함도 있는데, 구현체가 여러개가 발생할일이 없을 때도, 인터페이스를 분리해야할까?


포인트 동작도 인터페이스로 분리하면 어떨까?

- - - -
**[공부 출처]**
* https://www.baeldung.com/solid-principles
* 