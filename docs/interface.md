# What is an Interface?
#Programming/Java

우리가 배웠듯이, 오브젝트는 외부에 노출되는 메서드를 통해 외부세상과 상호작용하는 것을 정의한다.
메서드는 외부 세계와 오브젝트의 인터페이스를 정형화한다. 예를 들면 텔레비전 앞에 버튼 같은 거다.
당신과 플라스틱 케이스의 외면앞에 전기선 사이의 역할을 하는 것이 **인터페이스**다.


공통 폼을 보면 인터페이스는 바디를 가지고 있지 않은 관련된 메서드들의 집합을 말한다. 인터페이스로써 정의되어 있다면, 다음과 같을 것이다.

```
interface Bicycle {

    //  wheel revolutions per minute
    void changeCadence(int newValue);

    void changeGear(int newValue);

    void speedUp(int increment);

    void applyBrakes(int decrement);
}
```


이 인터페이스를 구현하기 위해서, 클래스의 이름이 바뀔것이다. 그리고 `implements` 키워드를 선언한다.

```
class ACMEBicycle implements Bicycle {

    int cadence = 0;
    int speed = 0;
    int gear = 1;

   // The compiler will now require that methods
   // changeCadence, changeGear, speedUp, and applyBrakes
   // all be implemented. Compilation will fail if those
   // methods are missing from this class.

    void changeCadence(int newValue) {
         cadence = newValue;
    }

    void changeGear(int newValue) {
         gear = newValue;
    }

    void speedUp(int increment) {
         speed = speed + increment;   
    }

    void applyBrakes(int decrement) {
         speed = speed - decrement;
    }

    void printStates() {
         System.out.println("cadence:" +
             cadence + " speed:" + 
             speed + " gear:" + gear);
    }
}
```


인터페이스를 구현하는 것은 제공하기로 약속한 행동에 대해서 좀더 형식화되어 있는 클래스를 허용하는 것이다. 인터페이스는 클래스와 외부 세계 사이의 약정서를 형식화하는 것이다. 그리고 이 약정서는 빌드타임에 컴파일러에게 강요된다. 이 클래스가 이 인터페이스를 구현하겠다고 선언한다면, 이 인터페이스에 있는 모든 메서드들은 클래스가 성공적으로 컴파일 되기 전에


여기서 질문)
`interface`와 `추상 클래스`. 어떨때 사용해야할까?

`RacingCar`를 구현한다고 치면, 이 오브젝트의 정체성은 `Car`니까 이건 상속이라고 본다.
근데 만약 이 `Racing Car` 는 `Radio` 기능이 있어요. 근데 부모클래스인 `Car` 라는 개념은 모든 자동차에 라디오가 있지 않을 수 있는거죠. 그럼 이 `Radio` 기능은 **interface**로 구현을 한다 라고 생각한다.

근데 이 `Radio` 기능이 전부 똑같다면, interface는 형체가 없다. 그럼 결국 코드 중복인데 이런 고민이 결국 생긴다.



