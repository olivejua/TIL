# Operator new
java에 클래스를 정의할 때, 우리는 새로운 데이터 타입을 만들고 있는것이다.클래스를 객체의 설계도라고 표현할 수 있다.
클래스로부터 객체를 만들수 있다. 클래스의 객체를 만들기 위해서 두가지 스텝을 거쳐야 한다.

1. 변수 선언: 먼저, 클래스타입의 변수를 선언해야한다. 이 변수가 object를 정의하고 있지는 않다. 대신, object의 참조값을 가질 수 있다. 일반정인 선언문은 다음과 같다.

```
Syntax :
class-name var-name;

Example :
// declare reference to an object of class Box
Box mybox;
```


이 상태의 변수는 (위 예제의 경우 `mybox`) 현재 object의 참조값을 가지고 있지 않은 상태다.

2. 인스턴스화, 초기화: 그다음, 실재하는 object의 복사본을 이 객체에 할당해야한다. 이 때 new operator를 사용한다. **new operator는 동적으로 새로운 객체를 위한 메모리를 할당받음으로서 클래스를 인스턴스화하고, 이 메모리에 대한 주소값을 반환한다.**
   이 과정은 runtime에 일어난다. 그리고 나서 이 메모리에 대한 주소값이  변수에 저장된다. 즉, java에서는 모든 클래스의 객체들이 동적으로 할당된다.

new 연산자 뒤에는 클래스를 초기화하는 클래스의 생성자의 호출이 온다. 생성자는 클래스의 객체에 생성될때 발생하는 일들을 정의한다. (즉, 초기화하는데 필요한 일들을 한다)

```
Syntax :
var-name = new class-name();

Example :
// instantiation via new operator and 
// initialization via default constructor of class Box
mybox = new Box();
```


**Important points :**
1. 위 두가지 과정을 하나의 명령문으로 표현하자면 다음과 같다.
```
Box mybox = new Box();
```

2. new operator에 의해 반환된 참조값은 클래스 변수에 할당할 필요가 없다 ???
```
double height = new Box().height;
```

3. array 의 경우에도 new 연산자를 사용한다.
```
int arr[] = new int[5];
```

4. 이 지점에서 이런 의문점이 들것이다. `왜 primitive type은 new 연산자를 사용하지 않을까?` 그 답은 자바의 primitive type은 object의 구현체가 아니다. `normal` 변수에 가깝다..? 효율적으로 다루기 위함이다.

5. `클래스를 인스턴스화한다는것` 라는 의미는 `객체를 생성하는 것`과 같은 의미다. 그리고 객체를 생성할 때, 당신은 클래스의 인스턴스를 생성하는 것이고, 그러므로 클래스를 인스턴스화한다 라고 하는것이다.

