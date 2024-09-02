# JVM


> [!tip]
> 왜라는 질문을 하고 답변을 스스로 해보기



### 누가 언제 메모리에 올리고 구조는 어떻게 생겨먹었지?
Java 프로그램이 실행되면 **JVM**이 클래스들을 메모리에 로드하고 메모리 구조를 관리한다.
JVM에서 #클래스로더 가 `Runtime Data 영역`에 배치한다.

![[Pasted image 20240711173952.png]]

#### 클래스로더
순서대로 실행됨
1) Bootstrap Class Loader : Java 런타임에 핵심 클래스들을 로드 (java.lang, java.util 등)
    1) Java8 이하: lib 디렉토리에 있음
    2) Java9 이상: rt.jar가 없어짐. JRE lib에 모듈 경로에 있음. jmods, modules
    * 부트스트랩 클래스로더 경로: `System.getProperty("sun.boot.class.path");` 실행보면 부트스트랩 클래스로더가 실행되는 경로가 보임
1) Extension Class Loader : jre/lib/ext 디렉토리의 확장 클래스들을 로드 (이 경로에 확장 라이브러리들을 추가하거나 Oracle이나 기타 타사가 제공하는 추가적인 Java 라이브러리.. )
    1) Java8 이하: lib/ext 디렉토리에 잇음 (예, 암호해독을 위한 클래스들, Cipher, SecretKeySpec, SSL/TLS 사용한 보안소켓 통신 등)
    2) Java9 이상 :
    * 익스텐션 클래스로더 경로: `System.getProperty("java.ext.dirs");` 실행보면 익스텐션 클래스로더 실행되는 경로가 보임
2) Application Class Loader : 애플리케이션 클래스패스를 기준으로 클래스파일 로드


#### Runtime Data Area
##### 1. Method Area
> 클래스 데이터, 메서드 데이터, 정적변수, 상수 풀이 저장됨
> 모든 스레드가 공유하는 영역이다.

##### 2. Heap Area
> 모든 객체와 배열이 저장된다. 객체는 Method Area 올라온 클래스들만 객체 생성이 가능하다.
> 가비지 컬렉터에 의해 관리된다.

##### 3. Stack Area
> 각 스레드마다 존재하며, 메서드 호출시마다 임시로 생성되었다가 소멸되는 데이터 저장 영역이다.
> 메서드 매개변수, 리턴값, 지역변수, 연산 중 발생하는 임시데이터 등이 있다.

##### 4. Native Method Stack
> 스레드마다 존재하며, 네이티브 메서드를 호출할 때 사용된다.
> 네이티브 메서드의 로컬변수, 호출 스택 프레임을 저장한다.

##### 5. PC Register
> Thread가 어떤 명령을 실행할지를 기록하는 영역으로, 현재 실행중인 JVM 명령의 주소를 가리킨다.


#### 바이트코드란?
확장자가 `.class`이다. 애플리케이션 개발자가 작성한 java 코드를 JVM이 이해할 수 있는 언어로 변환된 중간 코드이다.
JVM은 변환된 바이트코드를 각각의 운영체제에 맞게 기계어로 다시 컴파일하여 실행한다. 따라서 바이트코드는 가상머신이 설치되어 있다면 어느 환경에서든 실행가능하다.


#### 처음 메모리에 올릴 때부터 Runtime data Area에 구성영역은 정해져있는건가? 각 영역의 메모리 크기는 어떻게 할당되는거지?

1) Heap Area : 동적할당 영역 (최대 크기 있음)
    - 보통 시스템 메모리의 일정비율로 설정된다. 예를 들어 최대 힙크기 `-Xmx`는 시스템 메모리의 절반에서 최대 3/4 정도로 설정하는 것이 일반적이다.
    - 설정 명령어 : `java -Xms512m -Xmx1024m MyApp`
    - 필요에 따라 크기가 증가할 수 있고 최대 크기에 도달하면 OutOfMemoryError가 발생한다.
2) Metaspace : 동적할당 영역
    - Java8부터 PermGen을 대체한것으로 클래스의 메타데이터를 저장한다. 최대 크기설정 옵션은 `-XX:MaxMetaspaceSize`이다.
    - 클래스의 메타데이터: 클래스이름, 메서드 이름, 필드이름, 타입등
3) Stack Size: 고정크기 영역
    - 각 스레드마다 설정할 수 있으며, 기본값은 JVM 구현에 따라 다를 수 있다. 설정 옵션은 `-Xss`이다.
    - 스레드가 생성되면 스택 프레임이 추가되면, 메서드가 반환되면 해당 프레임이 제거된다.
    - 설정 명령어: `java -Xxx1m MyApp`
    - Stack Size는 고정 크기지만 JVM 옵션을 통해 조정할 수 있다. 각 스레드는 독립적인 스택을 가지며, 스택크기를 초과하면 StackOverflowError가 발생한다.


#### 클래스로더가 .class로 클래스코드를 로드하는데 왜 별도로 metaspace가 필요한거지?
- Method Area와 Metaspace의 차이점
    - Method Area : JVM 사양에 정의된 논리적 메모리영역으로, 클래스의 바이트코드와 정적데이터가 저장된다.
    - Metaspace : Java8부터 도입된 메모리영역으로 클래스를 로드할 때 생성되는 클래스의 메타데이터(클래스명, 메서드명, 필드명 및 타입 등)을 저장되며, Native 메모리를 사용하여 동적으로 확장된다.
- 왜 필요할까?
    - 동적 확장성
        - PermGen 영역은 크기가 고정되어있고, JVM 시작시 설정한 크기를 초과할 수 없다.
        - 하지만 metaspace는 네이티브 메모리를 사용하여 필요에 따라 크기를 동적으로 조정할 수 있다. 이를 통해 클래스 로딩시 메모리 부족 문제를 해결할 수 있다.
    - 메모리 관리의 효율성
        - metaspace는 JVM의 메모리 관리 및 가비지 컬렉션의 복잡성을 줄인다.
        - 클래스메타데이터가 네이티브 메모리에 저장되므로, 힙영역과 별도로 관리되어 가비지 컬렉션의 부담을 덜어줄 수 있다.


#### 스택 크기를 넘어섰을 때 발생할 수 있는 문제
- 여러 스레드가 동시 생성되어 더이상 새로운 스레드를 생성할 수 없을 때
    - OutOfMemoryError
    - 시스템의 물리적 메모리가 부족하거나, 운영체제의 스레드 생성 한도를 초과한 경우
- 하나의 스레드에서 할당된 스택크기를 초과할 때
    - StackOverflowError
    - 스레드의 스택크기를 초과했을 때
    - 메서드의 깊은 재귀 호출이나 너무 많은 메서드 호출로 인해 스택 프레임이 스택크기를 초과할 때


#### 상수풀(Constant Pool)이란?
> 클래스 파일의 바이트코드 내에 상수와 심볼릭참조[^1]들을 저장하는 테이블이다.
> JVM의 효율적인 메모리 관리와 실행성능을 유지하는데 중요한 역할을 하며 컴파일시 생성된 상수와 참조를 런타임에 효과적으로 사용하기 위한 구조를 제공한다.
> 상수풀은 두가지 주요영역으로 나눈다.

1. 클래스 상수 풀 (Class Constant Pool)
    - 각 클래스 파일에 포함된 데이터 구조로, 컴파일 시 생성된다.
    - 포함 정보
        - 리터럴 상수: 숫자, 문자열, 불리언 등
        - 심볼릭 참조: 클래스, 필드, 메서드 등
    - 클래스 상수 풀은 `.class` 파일 내에 존재하며, JVM이 클래스를 로드할 때 이 데이터를 사용합니다.
2. 런타임 상수 풀 (Runtime Constant Pool)
    - JVM이 클래스를 로드하고 메서드 영역에 저장할 때 클래스의 상수 풀의 데이터를 사용하여 생성된다.
    - 런타임 상수풀은 모든 상수와 심볼릭 참조를 포함하며, JVM이 실행중에 이 데이터를 사용하여 클래스와 메서드 호출을 관리한다.
    - 포함 정보
        - `CONSTANT_Class`: 클래스나 인터페이스의 심볼릭 참조.
        - `CONSTANT_Fieldref`: 필드의 심볼릭 참조.
        - `CONSTANT_Methodref`: 메서드의 심볼릭 참조.
        - `CONSTANT_InterfaceMethodref`: 인터페이스 메서드의 심볼릭 참조.
        - `CONSTANT_String`: 문자열 상수.
        - `CONSTANT_Integer`: 정수 상수.
        - `CONSTANT_Float`: 부동 소수점 상수.
        - `CONSTANT_Long`: 장정 상수.
        - `CONSTANT_Double`: 배정 소수점 상수.
        - `CONSTANT_NameAndType`: 필드 또는 메서드의 이름과 타입.
        - `CONSTANT_Utf8`: UTF-8 인코딩된 문자열.

예)
```java
public class Example {
	public static final String CONSTANT = "Hello, World!";
	
	public int add(int a, int b) {
		return a + b;
	}
}
```

상수풀에 포함될 항목
1. 클래스명 "Example" -> constant_utf8
2. Example 클래스 참조 -> constnat_class
3. 상수명 "CONSTANT" -> contant_utf8
4. CONSTANT의 저장 문자열 "Hello, World!" : constant_string
5. 상수 "CONATNT" 필드 참조 -> constant_fieldref
6. 메서드명 "add" -> constant_utf8
7. add 메서드 이름과 타입 `(I I)I` -> constant_nameandtype
8. add 메서드 참조 -> constant_methodref


#### Java 프로그램이 실행되면 Runtime Data Area에 있는 메모리들만 사용하는건가?
주로 Runtime Data Area에 의해 관리된다. 그러나 이외에도 추가적인 메모리 메모리 사용을 한다.
1. JNI (Java Native Interface) 메모리
    - JNI를 통해 네이티브 라이브러리(C/C++)와 상호작용할 때 사용할 수 있다.  Socket 연결 등이 있음
2. 메모리 맵 파일
    1. 파일 I/O를 효율적으로 수행하기 위해 메모리 맵 파일을 사용할 수 있다.
    2. `java.nio` 패키지의 `MappedByteBuffer` 클래스 등을 통해 파일을 메모리에 매핑함
3. Off-Heap 메모리
    - 직접적인 힙 영역 외부의 메모리를 사용하는 경우
    - `java.nio`패키지의 `ByteBuffer` 클래스 등을 통해 사용한다.
    - 주로 대용량 데이터를 효율적으로 처리하거나 가비지컬렉션의 영향을 줄이기 위해 사용된다.
1. JVM 메타데이터 메모리
    - JVM이 자체적으로 사용하는 메타데이터, 컴파일러 캐시, 클래스 메타데이터 등이 저장된다.
    - JIT 컴파일러의 코드 캐시 등이 포함됨
2. Native 메모리
    1. JVM 자체가 운영체제로부터 할당받은 메모리
    2. JVM이 실행되는동안 사용되는 다양한 내부 데이터 구조와 운영체제와의 상호작용을 위한 메모리



### 클래스 링크
1. 클래스 로딩
    1.  `클래스로더`가 클래스를 메모리에 로드한다.
        1. 컴파일러가 컴파일한 바이트코드를 메서드 영역에 올림. 여기에는 상수나 정적 필드, 정적 메서드 등 정적 데이터도 포함됨.
        2. 그과정에서 바이트코드에 등록된 클래스들의 상수풀을 참조하여 런타임 상수풀을 생성하여 등록함.
2. 클래스 링크
    1. 검증
        1. JVM 사양이 준수한지, 바이트 코드가 유효한지, 클래스 구조가 올바른지, 상속계층이 유효한지 검증
    2. 준비
        1. 정적필드를 기본값으로 초기화(인스턴스필드 초기화 X)
        2. 정적변수들이 메모리에 할당되고, 기본값(0, null, false)으로 초기화된다. 이단계에서는 실제로 코드가 실행되지 않는다.
    3. 해결
        1. 심볼릭 참조를 직접 참조로 변환한다. 메서드 호출, 필드접근, 클래스 상속 등의 심볼릭 이름을 실제 메모리 주소로 변환 (이 과정은 런타임에 필요할 때 수행할 수 있음)
    4. 초기화
        1. 정적 필드를 초기화함 (메서드호출형태라면 해당 메서드를 호출해 반환값을 초기화한다)


---
예)
```java
public class Example { 
	static int staticField; 
	int instanceField; 
	
	static { 
		staticField = 10; 
		System.out.println("Static block executed"); 
	} 
	
	public static void main(String[] args) {
		Example example = new Example(); 
		System.out.println("Instance field value: " + example.instanceField); 
	}
}
```
1. 검증:
    1. 클래스 파일 구조 검증: `Example.class` 파일이 올바르게 형식화되었는지 확인
    2. 바이트코드 검증: `main` 메서드와 정적 초기화 블록의 바이트코드가 올바른지 확인
    3. 상속 계층 검증: `Example` 클래스가 유효한 상속 계층을 가지고 있는지 확인
2. 준비
    1. 정적필드 기본값으로 초기화: `Example` 클래스의 정적필드인 `staticField`가 기본값인 `0`으로 초기화
        1. 이시점에는 `staticField`는 `0`임
    2. 메서드 영역에 저장: 클래스의 메타데이터와 기본값이 메서드 영역에 저장됨
3. 해결
    1. 심볼릭 참조: `staticField`, `instanceField`, `main` 메서드, `Example` 생성자 등의 심볼릭 참조를 실제 메모리 주소로 변환한다.

##### 어느시점에 심볼릭 참조로 주입되었다가 실제메모리 주소로 어디서 변환하는거지?
클래스를 컴파일 하면 클래스 파일에 클래스 내 정보들을 심볼릭참조로 포함하고 있다.
그럼 애플리케이션이 실행되고 클래스가 로드되는 시점에 이 클래스 정보를 Metadata에 저장했다가
클래스 링크의 해결단계에서 이 클래스의 정보, 메서드, 필드 정보가 실제 메모리 주소로 변환된다.

---

### 객체를 생성하라는 명령을 읽었을 때의 메모리 변화는? (new Example())

```java
public class Example {
	static int staticField = AUtil.calcaulate();
	int instanceField = AUtil.calcaulte();

	public static void main(String[] args) {
		Example example = new Example();
	}
}

class AUtil {
	public static int calculate() {
		return 10;
	}
}
```

(위 예제 기준으로 단계별 변화)
1. `Example` 클래스와 `AUtil`클래스를 메서드 영역에 로드한다. (메타데이터는 Native메서드 영역에 저장되는거 아닌가?)
2. 클래스링크과정에서 초기화할 때 `staticField` 초기화
3. main 메서드 실행하면서 main메서드의 스택프레임이 생성됨 (이때 로컬변수 example이 스택프레임이 저장됨)
4. 다음코드중 `Example example = new Example();`가 실행됨
    1. 힙 영역에 `Example` 객체가 생성됨
    2. 객체의 필드가 instanceField가 초기화됨. 이때 AUtil.calculate()가 실행되어 반환값이 저장됨
5. `example` 변수는 힙영역에 생성된 Example 객체의 참조를 스택영역의 main 메서드 스택프레임의 exmaple 지역변수에 저장

```
Method Area:
+--------------------------------------+
| Example class metadata and bytecode  |
| - staticField = 10                   |
| AUtil class metadata and bytecode    |
+--------------------------------------+

Heap:
+-------------------------+
| Example object          |
| - instanceField = 10    |
+-------------------------+

Stack:
+--------------------------+
| main() stack frame       |
| - example (reference)    | ---> Heap영역의 Example Object 메모리 주소
+--------------------------+
```


#### 객체 내 필드의 데이터가 크다면 메모리를 어떻게 할당해서 관리하지?

1. 힙 영역에서 메모리 할당
    - 객체가 생성되면 힙영역에 메모리 블록 내에 저장됨
    - 객체의 크기는 클래스에 정의된 필드의 타입과 개수에 따라 달라짐
        - 예를 들어 int 타입필드는 4바이트, long 타입 필드는 8바이트를 차지한다.
2. 필드의 메모리 레이아웃
    - 객체의 필드값은 연속된 메모리 블록 내에 저장
    - JVM 구현에 따라 달라질 수 있지만, 일반적으로 필드의 타입과 선언순서에 따라 메모리 내에서 정렬됨

**메모리 관리**
- 힙 영역을 가비지 컬렉션을 통해 자동으로 관리한다. (사용하지 않는 객체를 주기적으로 식별&제거하여 메모리 회수)
- 영역기반 할당함
    - 힙 영역을 여러 영역으로 나누어 메모리를 관리한다. (Old Heap, Young Heap)
- 메모리 풀
    - JVM은 특정 타입의 객체나 데이터 구조를 효율적으로 관리하기 위해 메모리 풀을 사용할 수 있다. 예를 들어 상수풀은 문자열 리터럴을 공유하여 메모리를 절약한다.


#### 값타입과 참조타입
- 값타입: 메모리에 값 자체를 저장함 (static이면 메소드영역, 인스턴스 필드면 힙영역, 지역변수면 스택영역)
- 참조타입: Heap 메모리에 저장되고 그 메모리 주소값을 저장함

##### 값타입은 상수풀을 이용할 수 있고 참조타입은 상수풀을 이용할 수 없는건가?
> 원시타입과 문자열리터럴은 상수 풀을 활용하지만 래퍼클래스를 포함한 일반 참조타입은 기본적으로 상수풀을 사용하지 않는다. 그러나 특정 조건에서는 래퍼클래스도 상수풀과 유사한 방식으로 동작할 수 있다.

- 래퍼 클래스
    - 래퍼클래스는 원시타입을 객체로 다루기 위해 존재하며, 상수풀과는 별도로 객체 풀링 매커니즘을 사용한다.
    - Java는 `Integer`, `Boolean`, `Byte`, `Character`, `Short` 등의 래퍼클래스에 대해 특정 범위 내의 객체는 재사용하도록 설계되어있다.
    - Integer 클래스 : -128 ~ 127까지의 값을 캐싱한다. (Byte, Short 모두 마찬가지) Character는 127까지 캐싱함, Boolean은 true, false모두 캐싱해놓음.

```java
public class WrapperClassExample { 
	public static void main(String[] args) { 
		Integer int1 = Integer.valueOf(100); 
		Integer int2 = Integer.valueOf(100); 
		Integer int3 = new Integer(100); 
		Integer int4 = 100; // 자동 박싱, Integer.valueOf(100) 호출 
		
		Integer int5 = Integer.valueOf(200); 
		Integer int6 = Integer.valueOf(200); 
		
		System.out.println(int1 == int2); // true 
		System.out.println(int1 == int3); // false 
		System.out.println(int1 == int4); // true 
		System.out.println(int5 == int6); // false 
	} 
}
```

##### 성능차이가 있나? 장단점이 무엇인가
> 원시타입과 래퍼클래서에서 중요한 차이점이 있다. 당연히 원시 타입이 메모리 사용과 성능면에서 더 효율적이다.

```java
/**
 * Primitive Time: 2,564,041  
 * Wrapper Time: 16,354,500 
 */  

public class PerformanceExample {  
    public static void main(String[] args) {  
        int sumPrimitive = 0;  
        long startTimePrimitive = System.nanoTime();  
        for (int i = 0; i < 1000000; i++) {  
            sumPrimitive += i;  
        }  
        long endTimePrimitive = System.nanoTime();  
        System.out.println("Primitive Time: " + (endTimePrimitive - startTimePrimitive));  
  
        Integer sumWrapper = 0;  
        long startTimeWrapper = System.nanoTime();  
        for (int i = 0; i < 1000000; i++) {  
            sumWrapper += i;  
        }  
        long endTimeWrapper = System.nanoTime();  
        System.out.println("Wrapper Time: " + (endTimeWrapper - startTimeWrapper));  
    }  
}

```

**원시타입**
- 특징
    - 메모리 사용: 더 적은 메모리를 사용한다.
    - 성능: 빠릅니다. 박싱/언방식이 필요없기 때문에 계산이 효율적이다.
    - 기본값이 있으며, null을 가질 수 없다.
- 장점: 성능이 빠르고 효율적이다. 메모리 사용량이 적다.
- 단점: null값을 가질 수 없다. 컬렉션 프레임워크에서 사용할 수 없다.
- 사용시기: 성능과 메모리 사용이 중요한 경우 / 값이 항상 존재하고, null이 필요없는 경우
  **래퍼클래스**
- 특징
    - 메모리 사용: 더 많은 메모리를 사용한다. 객체로 래핑되어 있어 추가적인 메모리 오버헤드가 있.
    - 성능: 상대적으로 느리다. 박싱/언방식이 필요하고, 메모리할당이 더 복잡하다.
    - null값을 가질 수 있으며, 기본값은 null이다. 이는 컬렉션에서 유용할 수 있다.
- 장점
    - null 값을 가질 수 있어 값이 없음을 명시할 수 있다.
    - 컬렉션을 사용할 수 있으며 유틸리티 메서드를 사용할 수 있다.
- 단점
    - 박싱/언박싱으로 성능이 저하될 수 있다. 추가적인 메모리 오버헤드가 있다.
- 사용시기:
    - 컬렉션 프레임워크와 함께 사용될 때
    - null 값을 사용해야하는 경우 (값을 명시해야될 때)
    - 유틸리티 메서드를 사용할 때

###### 정리
> 원시타입과 래퍼클래스는 장단점이 있으니 상황에 맞게 선택해서 사용해야한다.
> 성능과 메모리가 중요한 경우에는 원시타입을, 컬렉션사용과 null 처리가 필요한 경우에는 래퍼클래스를 사용하는 것이 좋다.


##### 문자열 리터럴을 상수풀에 어떤방식으로 저장하고 사용하지?
예를 들어
```java
public class Example {
	private String instanceField = "abc";

	public static void main(String[] args) {
		String str1 = "Hello, World!";  
		String str2 = "Hello, World!";  
		String str3 = new String("Hello, World!");  
  
		System.out.println("str1 == str2: " + (str1 == str2)); // true  
		System.out.println("str1 == str3: " + (str1 == str3)); // false  
		System.out.println("str1.equals(str3): " + str1.equals(str3)); // true
		
		Example example = new Example();
	}
}

```
1. 컴파일 타임
    1. "Hello, World!" 와 "abc"가 문자열 리터럴을 클래스 파일의 상수 풀에 저장한다.
    2. 각 클래스 파일에는 해당 클래스에서 사용되는 모든 리터럴 값이 상수 풀에 저장된다.
2. 클래스 로딩
    1. JVM이 `Example` 클래스를 로드할 때 상수풀에 저장된 문자열 리터럴 `Hello, World!`를 메서드 영역의 상수풀에 로드한다.
3. 문자열 리터럴 사용
    1. `String str1 = "Hello, World!";` 문장이 실행될 때, JVM은 상수풀에서 "Hello, World!"를 검색하고, 해당 문자열 객체의 참조를 `str1`에 할당한다. (동일한 문자열이면 문자열 재사용할 수 있는 이점이 있음)
    2. new 키워드로 문자열을 생성하면 힙 영역에 생성한다. 상수풀에 있는 문자열과 다른 객체이다.
4. 참고) 런타임시에 받아들인 문자열을 java 자체적으로 상수풀에 넣어놓고 사용하지는 않는다. 애플리케이션 내부 코드로 str.intern()함수를 사용해서 잘 사용할 법한 문자열을 넣어놓을 수는 있다.


#### 배열같은 경우 메모리 관리를 어떻게 관리하지?
```java
public class Example {
	private int[] array;

	public Example(int size) {
		this.array = new int[size];
	}

	public static void main(String[] args) {
		Example example = new Example(1_000_000);
		
	}
}

```

1. JVM은 힙영역에서 Example 객체와 array 배열을 위한 메모리를 할당함
2. array 배열은 100만개의 `int`값을 저장할 수 있는 메모리 공간을 차지함


### 일반클래스와 상속구현체는 무슨차이가 있는 것인가? 그리고 상속받은클래스를 특정 타입에 들어가도록 할 때는 어떻게 인식하는거지?

- 힙영역에는 객체인스턴스를 저장하고, 메서드 영역은 클래스 수준의 메타데이터를 저장한다.

##### 메모리상의 클래스 구조
메서드 영역
- 클래스 로딩시 클래스의 메타데이터(클래스 이름, 부모클래스 정보, 인터페이스, 메서드, 필드정보 등)가 저장됨
- 메서드 영역은 모든 스레드가 공유하는 영역으로, 클래스와 상속관계가 포함됨

힙영역
- 객체 인스턴스가 생성될 때 해당 객체의 모든 인스턴스 변수는 힙영역에 저장됨
- 상속된 클래스의 객체는 부모 클래스의 인스턴스 변수도 함께 저장됨

예시
```java
public class Parent {
	int parentField;
	
	public void parentMethod() {
		System.out.println("Parent method");
	}
}

public class Child extends Parent {
	int childField;
	
	public void childMethod() {
		System.out.println("Child method");
	}
	
	@Override
	public void parentMethod() {
		System.out.println("Child method");
	}
	
	public static void main(String[] args) {
		Child child = new Child();
		child.parentField = 10; // 부모 클래스의 필드
		child.childField = 20;  // 자식 클래스의 필드
		child.parentMethod();   // 오버라이드된 메서드
		child.childMethod();    // 자식 클래스의 필드
	}
}
```


1. 메서드 영역
    - `Parent` 클래스와 `Child`클래스의 메타데이터가 저장됨
    - `Child`클래스는 `Parent`클래스의 메타데이터를 참조하여 상속 관계를 유지함
2. 힙 영역
    - `Child`객체가 생성되면, `Parent`클래스의 필드와 `Child`클래스의 필드가 모두 힙영역에 저장됨
    - `Child`객체는 `Parent` 객체의 필드를 포함하여 인스턴스 필드를 가짐

```
Method Area:
+----------------------------------------+
| Parent class metadata                  |
| - int parentField                      |
| - void parentMethod()                  |
| Child class metadata                   |
| - int childField                       |
| - void childMethod()                   |
| - void parentMethod() [Overridden]     |
+----------------------------------------+

Heap:
+----------------------------------------+
| Child object                          |
| - int parentField = 10                |
| - int childField = 20                 |
+----------------------------------------+

```

##### 상속관계(부모클래스 상속, 인터페이스 구현)의 저장을 어떤식으로 하는가?
- 상속관계와 인터페이스 구현정보
    - 상속관계: `super_class` 항목에 부모 클래스의 이름으로 저장
    - 인터페이스 구현: `interfaces`배열에 구현된 인터페이스의 이름들이 저장

예시코드)
```java
public class Parent {
	//some fields are methods
}

public interface InterfaceA {
	//some methods
}

public interface InterfaceB {
	//some methods
}

public class Child extends Parent implements InterfaceA, InterfaceB {
	//some fields are methods
}
```

###### 클래스 파일 구조
> Child 클래스의 메타데이터
> - super_class: Parent 클래스
> - interfaces: InterfaceA와 InterfaceB의 이름들

###### 클래스 파일의 메타데이터 시각화
```
Class File Structure for Child.class:
+-----------------------------------------+
| Magic Number                            |
| Version Information                     |
| Constant Pool                           |
| Access Flags                            |
| This Class                              |
| Super Class (Parent)                    |
| Interfaces (count: 2)                   |
|  - InterfaceA                           |
|  - InterfaceB                           |
| Fields                                  |
| Methods                                 |
| Attributes                              |
+-----------------------------------------+
```
```
Class: Child
  Super class: Parent
  Interfaces:
    0: InterfaceA
    1: InterfaceB

```

##### 결론
- **상속 관계**: 부모 클래스의 이름은 클래스 파일의 `super_class` 항목에 저장됩니다.
- **인터페이스 구현**: 구현된 인터페이스의 이름들은 클래스 파일의 `interfaces` 배열에 저장됩니다.
- **클래스 파일 구조**: 클래스 파일에는 다양한 메타데이터가 포함되어 있으며, 상수 풀을 통해 클래스와 인터페이스 이름을 참조합니다.

### import 위치정보는 어디에 포함되나? 그리고 이름만 저장하면 어느파일에 뭐라고 어떻게 인식할까?
> Java 컴파일러는 import 문을 통해 클래스 파일간의 의존성을 해결한다. import문은 컴파일 시점에 컴파일러가 다른 클래스의 위치를 찾는데 사용된다. 컴파일된 클래스 파일에는 Import문 자체가 포함되지 않지만, 상수 풀에 다른 클래스에 대한 참조가 포함된다.

```java
import java.util.List;
import java.util.ArrayList;

public class Example {
    private List<String> list;

    public Example() {
        list = new ArrayList<>();
    }

    public void addString(String str) {
        list.add(str);
    }

    public String getString(int index) {
        return list.get(index);
    }

    public static void main(String[] args) {
        Example example = new Example();
        example.addString("Hello");
        System.out.println(example.getString(0));
    }
}
```

(클래스 파일의 상수풀)
```
Constant pool:
   #1 = Methodref          #4.#21         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#22         // Example.list:Ljava/util/List;
   #3 = Class              #23            // Example
   #4 = Class              #24            // java/lang/Object
   #5 = Utf8               list
   #6 = Utf8               Ljava/util/List;
   #7 = Class              #25            // java/util/List
   #8 = Class              #26            // java/util/ArrayList
   ...
```

#### 클래스 로딩 과정에서의 참조처리
1. 컴파일 타임
    - import문을 통해 참조된 클래스(java.util.List, java.util.ArrayList)가 상수풀에 추가됨
2. 클래스 로딩
    - JVM이 `Example`클래스를 로드할 때, 상수 풀에 있는 클래스 참조를 메서드 영역의 런타임 상수풀로 이동시킴
3. 런타임 상수풀
    - 클래스 참조(`java/util/List`, `java/util/ArrayList`)는 런타임 상수풀에서 참조됨


### Jar의 실행과정

- jar 파일 내 메인함수가 여러개라면?
    - Jar가 배포될 때 하나의 메인 클래스가 지정이 되어있어야함. 메인 클래스는 `META-INF/MENIFEST.MF` 파일의 `Main-Class` 속성에 명시됨. jar 파일 내 여러 `main`메섣를 가진 클래스가 있을 경우, 기본적으로 `MENIFEST.MF` 파일에 지정된 메인 클래스를 실행함.



### 동적로딩이 왜 필요한가?

메모리 사용을 유연하게 할 수 있다는 점이다. 따라서 필요한 시점에만 메모리에 올렸다가 사용하지 않을 때는 해제할 수 있다.

- 런타임에 필요한 클래스만 로드함으로써 메모리 사용을 최적화할 수 있다.
    - 예를 들어 플러그인 시스템을 사용하는 애플리케이션에서 사용자가 필요한 플러그인만을 동적으로 로드하여 메모리사용을 최소화할 수 있다.
- 특정 기능이나 모듈이 실제로 필요할 때만 메모리에 로드하고, 사용하지 않을 때는 메모리에서 해제함으로써 메모리 사용을 최적화할 수 있다.
    - 대규모 데이터 처리를 위해 일시적으로 많은 객체를 생성하고, 처리가 끝나면 메모리에서 해제하는 방식으로 메모리 관리를 효과적으로 수행할 수 있다.


보완할 점
- 유연한 기능이지만 에러나 보안에서 예기치 못한 문제가 발생할 수 있다. 런타임에 문제가 발생하기 때문에 미리 알수 없다. 따라서 성능이 중요한 애플리케이션에서는 동적로딩사용을 신중히 고려해야한다.
- 동적 로딩의 빈도를 줄이고, 캐싱 등을 통해 성능을 최적화할 수 있다.
- 효율적인 메모리 사용을 위해 할당한 메모리 중 사용하지 않는 메모리는 해제해야함. 이게 컴파일이나 실행에 문제가 되지 않아 발견되지 않을 수 있지만 누적되면 CPU를 지연시키고, 프로그램 에러가 나는 경우가 있음.

메모리 누수가 되는 경우



### 실행엔진
> 바이트코드를 기계어로 변환하는 역할을 함. 소스는 사람이 이해하기 쉬운 고급언어로 구성되어 있기 때문에 변환한다. heap 메모리 영역에 생성된 객체를 GC가 찾아 메모리 해제하여 관리를 해주낟.
> 여기서 인터프리트 방식과 JIT 컴파일 방식을 사용한다. JIT 컴파일러는 인터프리터가 실행중인 코드를 분석하여 자주 실행되는 부분(핫스팟)을 기계어로 변호나하고, 이를 캐싱하여 다음에 실행할 때 바로 사용한다.

##### JIT 컴파일러가 캐싱을 판단하는 기준
- 메서드 호출 횟수
    - JVM에는 메서드 호출 횟수를 추적하는 카운터가 있으며, 이 카운터가 특정 임계치에 도달하면 JIT 컴파일러가 메서드를 컴파일한다.
- 루프 반복 횟수
    - 루프 내부의 코드가 많이 반복 실행되면 JIT 컴파일러는 루프를 포함하는 메서드를 기계어로 컴파일함
- 프로파일링 데이터
    - JVM은 실행중에 수집된 프로파일링 데이터를 사용하여 자주 사용되는 경로를 식별함
        - 메서드의 실행 경로, 조건문 분기, 인라인 가능한 메서드 등의 정보 포함

##### JIT 컴파일러가 성능을 향상시키기 위해 사용하는 최적화 기술
**1. 인라이닝**
- 메서드 호출을 제거하고, 호출된 메서드의 코드를 호출 지점에 직접 삽입
- 효과: 메서드 호출 오베헤드를 줄임
- 예시 :
```java
public int add(int a, int b) {
	return a + b;
}

public void example() {
	int sum = add(1, 2);
}
```
-->
```java
public void example() {
	int sum = 1 + 2;
}
```

**2. 루프 최적화**
- 루프 언롤링, 루프 전환을 포함
- 장점: 루프의 반복 횟수를 줄이고, 루프 내 코드의 실행속도를 향상시킴
- 예시
```java
for (int i = 0; i < 100; i++) {
	// 반복 작업
}
```
-->
```java
for (int i = 0; i < 100; i+=4) {
	// 반복 작업
}
```

**3. 탈출 분석**




분기 예측


중복코드 제거




```
JIT 컴파일러가 변환한 코드 저장 영역
- (코드 캐시)네이티브 메모리 영역

**정리**
> JVM이 시작할 때 한번 인터프리터가 전체 코드를 읽어 기계어로 변환하여 실행보고 여기서 자주 사용하는 메서드나 반복실행 루프, 자주 사용하는 경로를 추려서 JIT 컴파일러가 이를 캐싱해놓고, 실행 단계에서 작업 요청이 들어왔을 때 다시 바이트코드를 인터프리터가 한줄씩 기계어로 변환하여 실행하면서 JIT 컴파일러가 캐싱해놓은 기계어 코드들을 사용한다.

 
어차피 기계어로 변환하는데 바이트코드로 한번 컴파일 하는 이유
### 1. 플랫폼 독립성

Java 바이트코드는 JVM을 통해 실행됩니다. 각 JVM 구현은 특정 플랫폼(예: Windows, Linux, macOS)에 맞춰져 있습니다. 바이트코드는 플랫폼에 독립적이므로, 한 번 작성된 Java 프로그램은 어떤 플랫폼에서도 동일하게 실행될 수 있습니다.

### 2. 보안

바이트코드의 사용은 보안을 강화합니다. JVM은 바이트코드를 실행하기 전에 다양한 검증을 수행하여 악의적인 코드 실행을 방지합니다. 이 검증 과정은 클래스 로더와 바이트코드 검증기를 통해 이루어집니다.

### 3. 최적화

바이트코드는 중간 표현으로, JVM은 이를 실행하는 과정에서 다양한 최적화를 적용할 수 있습니다. JIT 컴파일러는 실행 중에 수집된 프로파일링 데이터를 활용하여 런타임 최적화를 수행할 수 있습니다.

### 4. 멀티언어 지원

JVM은 Java뿐만 아니라 Kotlin, Scala, Groovy 등 여러 언어를 지원합니다. 이러한 언어들은 JVM 바이트코드로 컴파일되므로, JVM이 이 바이트코드를 실행할 수 있습니다. 이는 JVM을 멀티언어 실행 환경으로 만들어줍니다.

### 5. 클래스 로딩과 동적 로딩

JVM의 클래스 로딩 메커니즘은 동적으로 클래스를 로드하고, 필요한 경우 이를 메모리에서 언로드할 수 있도록 합니다. 바이트코드는 이러한 클래스 로딩 메커니즘의 핵심입니다. 동적 로딩과 언로딩은 메모리 관리와 성능 최적화에 중요한 역할을 합니다.

### 6. 이식성 및 호환성

바이트코드는 Java 프로그램의 이식성과 호환성을 보장합니다. 이는 다양한 JVM 구현이 동일한 바이트코드를 실행할 수 있음을 의미합니다. 새로운 JVM 버전이 출시되더라도, 기존 바이트코드는 그대로 실행될 수 있습니다.

```

### 객체 할당 크기

객체가 힙영역에 할당될 때 메모리 크기는 객체의 구조에 따라 다르다.
**객체의 메모리크기를 결정하는 요소**
1. 객체 헤더
2. 인스턴스 변수
3. 패딩

**객체 헤더**
- Mark Word: 객체의 해시코드, GC 정보, 잠금 정보
- Class Pointer: 객체의 클래스 메타데이터
  64비트 JVM을 기준으로, 객체 헤더는 일반적으로 12바이트 또는 16바이트이다.

**인스턴스 변수**
- 각 객체에 정의된 인스턴스 변수에 해당하는 메모리 공간을 차지
- **boolean**: 1 바이트
- **byte**: 1 바이트
- **char**: 2 바이트
- **short**: 2 바이트
- **int**: 4 바이트
- **float**: 4 바이트
- **long**: 8 바이트
- **double**: 8 바이트
- **참조 타입**: 4 바이트 또는 8 바이트 (32비트 JVM에서는 4 바이트, 64비트 JVM에서는 8 바이트)

**패딩**
- JVM은 메모리 정렬을 위해 객체의 크기를 8바이트의 배수로 맞춤. 따라서 인스턴스 변수와 객체의 헤더의 크기를 합친후, 8바이트의 배수가 되도록 패딩을 추가함







### main함수의 실행과정을 정리하자면
JVM이 실행되며 아래와 같은 단계를 거친다.
1. 클래스 로더가 컴파일된 바이트코드를 Runtime Data Area의 메서드 영역에 옮긴다. (여기서 Bootstrap Class Loader가 자바의 핵심 클래스를 로드시키고(java.util, java.lang 등) -> Extension Class Loader가 자바의 확장클래스를 로드시키고(javax 등) -> Application Class Loader가 애플리케이션 내 클래스패스에 있는 바이트코드를 로드시킨다.)
2. 여기서 클래스의 Metadata도 metaspace에 옮긴다. (Java8 이전에는 메서드 영역에 저장되었지만 Java8버전부터는 Native 메모리 영역에 저장된다.)
3. 바이트 코드에 있는 클래스의 상수풀에서도 클래스정보, 메서드 정보, 문자열, 데이터타입 등의 상수풀을 런타임 상수풀에 저장한다. (런타임 상수풀은 메서드영역에 위치한다.)
4. 클래스링크 단계에서 바이트코드가 올바른지 검증한다. (버전별로 다른 로직이라던지 클래스 정보 등이 유효한지 등)
5. 클래스링크 단계에서 준비 과정을 실행한다. 여기서는 클래스의 정적 필드를 기본값으로 세팅된다. 정수타입은 0, boolean 타입은 false, 참조타입은 null로 세팅된다.
6. 클래스링크 단계에서 해결과정을 실행하게 되고, 클래스의 메타데이터가 심볼릭참조단계에서 직접참조로 변환된다. (실제 메모리 주소)
7. 실제 static 필드에 초기화 할당되는 값을 저장한다.
8. 실행엔진 구성요소 중 하나인 인터프리터가 한줄한줄씩 기계어로 실행한다. 여기서 발견되는 핫스팟 (반복되는 루프, 많이 호출되는 메서드, 자주 실행되는 경로)을 찾아 JIT 컴파일러가 미리 기계어로 변환하여 캐싱해놓는다. 이후 인터프리터가 실행 중에 캐싱되어있는 부분을 사용하여 실행속도를 높인다.
9. main 함수를 실행한다. 그럼 실행 스레드에 대한 Stack 영역이 생성되고 그안에 main함수에 대한 Stack Frame이 생성된다. main 함수의 인자 args와 내부 지역변수를 저장한다. 인터프리터가 한줄한줄 실행한다. 여기서 new 생성자를 만나면 해당 클래스의 객체를 생성한다. 저장되는 위치는 Heap 영역으로 메타데이터의 클래스 정보와 내부 필드도 이안에 같이 저장된다. 초기화되는 순서는 필드의 기본값->초기화블럭->생성자순이다. 여기에는 super class 와 구현 interface의 인스턴스 필드도 함께 포함된다.
10. 실행중에 더이상 참조하지 않는 메모리 주소가 있다면 GC가 이를 해제한다.










[^1]:  심볼릭참조(Symbolic Reference) : 특정 객체를 참조할 때 메모리주소를 참조하는 게 아니라 객체의 이름으로 참조한다.