# Boxing / Unboxing
## Autoboxing
primitive type과 그에 맞는 wrapper class 타입 사이에 Java Compiler의 의해 만들어지는  `자동 전환` 이다.


autoboxing의 예제를 보면,
```java
Character ch = ‘a’
```


Generic에도 들어감.
```java
List<Integer> li = new ArrayList<>();
for (int i = 1; i < 50; i += 2)
    li.add(i);
```
Integer 타입의 List인데 int를 넣어도 들어간다. 왜 Java Compiler가 컴파일에러를 내지 않았을까?
이유는 컴파일러가 예제의 변수인 `i`를 Integer로 Autoboxing 후, List의 변수인 `li`에 추가하고 있기 때문이다. Compiler는 런타임에 이전코드를 다음과 같이 변환한다.

```
List<Integer> li = new ArrayList<>();
for (int i = 1; i < 50; i += 2)
    li.add(Integer.valueOf(i));
```

**primitive value를 wrapper class의 object 로 변환하는 과정을 autoboxing이라 부른다.**

Java Compiler는 다음과 같은 상황일 때 autoboxing을 한다.
* wrapper class의 object를 파라미터로 받는 메서드에 primitive value가 넘겨졌을때
* wrapper class 타입의 변수에 primitive value가 할당되었을 때


자, 다음과 같은 상황을 보자.
```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i: li)
        if (i % 2 == 0)
            sum += i;
        return sum;
}
```


이 예제에서 `sum`의 결과 값은 얼마일까? 답은 0이다.

**왜냐하면 wapper class 인 Integer object는 remainder(%) 연산자와 unary plus (+=) 연산자가 적용되지 않는다.**
_그렇다면 왜 Java Compiler는 어째서, 어떠한 에러도 내지 않는걸까?_
이유는 런타임에 Integer에서 int로 전환하는 method인 `intValue` 가 주입되기 때문이다. 다음과 같다.

```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i : li)
        if (i.intValue() % 2 == 0)
            sum += i.intValue();
        return sum;
}
```


**wrapper class타입의 object를 거기에 해당하는 primitive type으로 변환하는 과정을 unboxing이라 부른다.**
Java Compiler는 다음과 같은 상황일 때 unboxing을 한다.
* primitive type을 파라미터로 받는 메서드에 wrapper class 타입의 값이 넘겨졌을때
* primitive type의 변수에 wrapper class의 값이 할당되었을 때


## Wrapper Class
wrapper class 타입의 object를 생성했다면, 이 object 안에 하나의 필드가 포함됐을텐데, 이 필드 안에는 primitive data type이 저장될수 있다.
다르게 말하면, 우리는 primitive value를 wrapper class object로 감쌀 수 있다.

### Wrapper Class가 필요한 이유
1. 우리가 method에 전달될 인수를 수정하고 싶을 때 Object가 필요하다. 왜냐면 primitive type은 값 자체가 넘겨지기 때문에 함수가 종료되면 이 값도 사라진다.)
2. Java.util 패키지에 있는 클래스들 (살펴보니, 컬렉션이나 Timezone 등이 있음) 오직 object만 다룰수 있기 때문에 wrapper class를 사용한다.
3. Collection Framework 안에 있는 자료구조(ArrayList, Vector 등) 은 오직 object만 저장할 수 있다. (primitive type 안됨)
4. object 는 멀티스레드 환경에서 동기화 를 지원하는데 필요하다.


## 왜 Autoboxing / Unboxing을 사용하는 걸까?
* Autoboxing과 unboxing은 개발자가 보다 읽기 쉬운 클린코드를 쓰게 해준다.
* Autoboxing, unboxing의 기술은 primitive type과 wrapper class object 사이에 교차적으로 편리하게 사용할 수 있도록 함으로써 굳이 타입캐스팅을 수행하는 로직을 작성하지 않아도 된다.


## Primitive Wrapper Classes를 모두 불변의 객체로 만들었을까?
Wrapper class (Integer, Byte, Long, Float, Double, Character, Boolean, and Short)를 보면 모두 `private final (primitive type) value;` 로 **한번 생성되면 변경될 수 없다.**

왜 wrapper class를 불변으로 만들었을까?
* 불변으로 만들었기 때문에 상태(field)는 자동으로 동기화된다.
* 불변으로 만들었기 때문에 wrapper class는 일관성이 있음.
* 특정 타입의 하나의 인스턴스로 여러개의 어플리케이션을 캐싱할 수 있도록 해준다????

```java
class GFG {

    public static void main(String[] args)
    {

        // Getting an integer value
        Integer i = new Integer(12);

        // Printing the same integer value
        System.out.println(i);

        // Calling method 2
        modify(i);

        // Now printing the value stored in above integer
        System.out.println(i);
    }

    // Method 2
    // To modify integer value
    private static void modify(Integer i) { i = i + 1; }
}
```


modify 후 출력되는 `i`의 결과값은 13일까? 결과는 처음 할당했던 값인 12다.
```java
i = i + 1;
```


이 연산의 절차를 보자.
1. 먼저 `i`를 int value로 unbox한다.
2. `i`에 1을 더한다.
3. 더한 결과를 다른 Integer object로 box한다.
4. 결과값 Integer object를 `i`에 할당한다.

---
#### [공부출처]
* [Autoboxing and Unboxing (The Java™ Tutorials > Learning the Java Language > Numbers and Strings)](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)
* [Autoboxing and Unboxing in Java - GeeksforGeeks](https://www.geeksforgeeks.org/autoboxing-unboxing-java/)
* [Wrapper Classes in Java - GeeksforGeeks](https://www.geeksforgeeks.org/wrapper-classes-java/)