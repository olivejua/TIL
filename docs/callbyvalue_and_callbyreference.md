call by value / call by reference
함수 파라미터로써 두가지 방법으로 주입될 수 있는데 `Call by Value` or `Call by Reference`이다.

여기서 알게된 사실
#### [Actual Parameter]
* 요청 받는 함수에게 전달 되는 **변수**
* actual parameter는 데이터타입이 아니라 변수만 언급한다.

#### [Formal Parameter]
* 함수가 선언됐을 때, 값들을 받는 함수에 의해 결정되는 값? 즉, 호출받는 함수 안에서 사용되는 **값**들.
* Formal Parameter는 데이터타입이 필수다.

함수를 호출할 때, _변수의 값을 전달하느냐, 변수의 주소값을 전달하느냐._

## Call by Value
Actual Parameter로써 값을 넘기면 Java는 이 값을 Formal Parameter로 복사를 해서 사용한다.
따라서 호출하는 쪽에서 준 Actual Parameter 값에 아무런 영향을 미치지 않는다.


## Call by Reference
Java에서 모든 non-primitives 는 항상 references다. 그래서 메서드에 파라미터로 object를 넘기게 될때 조금 까다롭다.
Java는 역시 Actual Parameter로 넘겨진 값을 Formal Parameter로 복사를 하긴 한다. 그러나 같은 데이터를 가리키는 주소값을 복사한다.
그 말은, 함수로 넘어온 파라미터에 다른 object 주소값을 할당하면, 처음 넘겨준 object에는 아무런 영향을 받지 않는다.
우리가 다른 주소값이나 다른 오브젝트로 바꿔버리면 변경사항이 다시 반영이 되지 않는 다는 얘기다.

우리가 새로운 주소 또는 오브젝트를 할당하지 않는다면 변경사항이 반영될것이다.


---
#### [공부 출처]
* [Difference between Call by Value and Call by Reference - GeeksforGeeks](https://www.geeksforgeeks.org/difference-between-call-by-value-and-call-by-reference/)
* [Java is Strictly Pass by Value! - GeeksforGeeks](https://www.geeksforgeeks.org/g-fact-31-java-is-strictly-pass-by-value/)
