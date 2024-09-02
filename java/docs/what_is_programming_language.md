# What is Programming Language?

우리 모두 알다시피, 사람과 의사소통하기 위해서는, 특정언어가 필요하다. 유사하게도 컴퓨터와 의사소통하기 위해서 프로그래머는 `프로그래밍 언어`라고 불리는 언어가 필요하다.

프로그래밍 언어를 이해하기 전에, **언어**란 무엇인지 알아보자.


## What is Language?
언어는 우리가 아이디어를 공유하거나 서로의 의견을 나눌때 사용하는 의소소통의 수단으로 사용된다.
예를 들면, A라는 사람이 B라는 사람에게 가르쳐야 할 때, **A와 B 모두 이해할 수 있는 언어**가 필요하다.


## What is a Programming Language?
프로그래밍 언어는 _프로그래머가 컴퓨터와 의사소통하기 위해 사용하는 컴퓨터 언어_ 이다.
이 프로그래밍 언어는 **특정 임무를 수행하기 위해 특정 언어로 쓰여진 명령어 집합**이다.


# 프로그래밍 언어의 종류
## 1. 저급 언어 (Low-level programming language)
저급 언어는 **기계 의존적(machine-dependent)** 인 프로그래밍 언어이다.
_프로세서는 low-level 프로그램을 컴파일러나 인터프리터의 도움 없이 바로 실행한다._
따라서, 저급언어로 쓰여진 프로그램은 실행속도가 매우 빠르다.

저급언어는 두가지 유형으로 나뉜다.

### i. 기계어 (Machine Language)
기계어는 _저급언어 (low-level programming language)_의 호칭 이외에 **machine code 또는 object code**라고도 불린다.
기계어는 일반적으로 2진수 또는 16진수의 형태로 보여지기 때문에 읽기에는 쉽다. (Me - 읽기도 어려울거같은데…)

컴퓨터가 바로 기계어 프로그램을 이해할 수 있기 때문에 프로그램을 변환하기 위한 번역을 할 필요가 없다.
이 과정을 거치지 않기 때문에 `high level programming language` 보다 빠르다는 이점을 가지고 있다.


### ii. 어셈블리어 (Assembly Language)
어셈블리어는 특정 프로세서용으로 설계된 _저급언어(low-level programming language)_이다.
이 언어는 **상징적이고 인간이 이해할 수 있는 형태**의 명령어 집합으로 나타난다.

어셈블리어로 쓰여진 프로그램은 어셈블리어를 기계어로 변환하기 위해 어셈블러(assembler)를 사용한다.

어셈블리어의 이점은 프로그램을 실행하는데 적은 메모리와 적은 실행시간을 필요로 한다는 점이다.

* [어셈블리어 나무위키](https://namu.wiki/w/%EC%96%B4%EC%85%88%EB%B8%94%EB%A6%AC%EC%96%B4)

## 2. 고급 언어 (High-level programming language)
고급언어는 **사용자 친화적인 소프트웨어 프로그램과 웹사이트**의 개발용으로 설계된 언어이다.
이 프로그래밍 언어는 프로그램을 기계어로 번역시켜줄 컴파일러 또는 인터프리터가 필요하다.

고급언어에는 Python, Java, JavaScript, PHP, C#, C++, Objective C, Perl, Pascal, LISP, FORTRAN, 그리고 Swift 프로그래밍 언어가 포함된다.

고급 언어는 세가지 유형으로 나누어진다.

### i. 절차 지향 프로그래밍 언어 (Procedural Oriented Programming Language)
절차 지향 프로그래밍 언어는 구조화된 프로그래밍에서 파생되었으며, 프로시저 호출 개념 기반으로 이루어진다.
이 언어는 프로그램을 루틴(routines)과 함수(functions)라는 두가지 절차로 나눈다.

절차 지향 프로그래밍 언어는 소프트웨어 프로그래머가 IDE나 AdobeDreamweaver 또는 Microsoft Visual Studio 같은 프로그래밍 에디터를 사용해 수행할 수 있는 프로그램을 만드는데 사용된다.

절차 지향 언어의 이점은 프로그래머가 쉽게 프로그램 흐름을 추적하고, 코드를 프로그램의 다른 부분에 재사용할 수 있다는 것이다.

### ii. 객체 지향 프로그래밍 언어 (Object-Oriented Programming Language)
객체지향 프로그래밍 언어는 객체 기반으로 이루어진다. 이 언어는 프로그램을 객체(object)라고 불리는 작은 부분들로 나눈다.
이 언어는 프로그램에서 상속, 다형성, 추상화 같은 현실세계의 개념들을 구현함으로서 재사용성 있고, 효율적이고, 사용하기 쉬운 프로그램을 만드는데 사용된다.

객체지향 프로그래밍 언어의 주요 이점은 OOP 가 디버깅 뿐만 아니라 실행, 유지보수, 변경하는데 쉽고 빠르게 할 수 있는 점이다.


### iii. 자연어 (Natural Language)
자연어는 영어, 러시아어, 독일어, 일본어같은 인간 언어의 일부이다. 기계가 이 언어를 이해, 조작, 해석하는데 사용된다.
또한 개발자가 번역, 자동 요약, NER(Named Entity Recognition), 관계 추출 및 주제 세분화와 같은 작업을 수행하는 데 사용한다.

자연어의 주요 이점은 유저가 어느 주제의 질문을 하던 몇 초만에 바로 응답할 수 있는 것이다.


## 중급 언어 (Middle-level programming language)
중급언어는 저급 언어(low-level programming language)와 고급 언어(high-level programming language) 사이에 놓여있다.
이 언어는 중간 언어(Intermediate programming language) 와 수도 언어 (pseudo-language)로 알려져있다.

중간언어의 이점은 고급 언어의 프로그래밍(사용자 친화적인 언어)의 특징과 기계어와 인간어와 밀접하게 관련이 있다는 것이다. (무슨 말….이지?)


#### 공부 출처
* [Programming Language |  What is Programming Language - Javatpoint](https://www.javatpoint.com/programming-language)