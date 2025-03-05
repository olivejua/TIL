#spring #aop #proxy

Spring AOP는 프록시 기반으로 동작을 한다. 그러므로 스프링에서 프록시가 어떻게 동작하는지 알아보자.

## Proxy Mechanism

먼저 Spring 에서는 두가지 방식을 채택하고 있는데 JDK Dynamic Proxy와 CGLIB이다. 

### JDK Dynamic Proxy와 CGLIB

Sample Code
```java
public interface Pojo {  
    void foo();  
}
```

```java
public class SimplePojo implements Pojo {  
  
    public void foo() {  
        this.bar();  
    }  
  
    public void bar() {  
        System.out.println("SimplePojo.bar");  
    }  
}
```

#### JDK Dynamic Proxy
> Java에 내장되어있는 reflect 패키지인 java.lang.reflect.Proxy를 사용한 프록시.  
>   
> - 대상: 인터페이스를 1개 이상 상속받은 클래스는 JDK Dynamic Proxy로 생성함.  
>   
> 별도의 외부 라이브러리 없이 java에서 제공되는 기능으로 동작할 수 있다는 것이 장점임. 하지만 인터페이스가 필수임.  

```java
import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
  
public class ServiceInvocationHandler implements InvocationHandler {  
    private final Object target;  
  
    public ServiceInvocationHandler(Object target) {  
        this.target = target;  
    }  
  
    @Override  
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
        System.out.println("Before ServiceInvocationHandler.invoke");  
  
        Object result = method.invoke(target, args);  
  
        System.out.println("After ServiceInvocationHandler.invoke");  
  
        return result;  
    }  
}
```

```java
@Service  
public class V2Service {  
  
    // JDK Dynamic Proxy  
    public void methodA() {  
        System.out.println("V2Service.methodA");  
  
        Pojo pojo = new SimplePojo();  
  
        Pojo proxyInstance = (Pojo) Proxy.newProxyInstance(  
                Pojo.class.getClassLoader(),  
                new Class[]{Pojo.class},  
                new ServiceInvocationHandler(pojo));  
  
        proxyInstance.foo();  
    }  
  
    public static void main(String[] args) {  
        V2Service service = new V2Service();  
  
        service.methodA();  
    }  
}
```


#### CGLIB
> common 오픈소스 라이브러리 클래스임 (`spring-core` 에 리패키징된 CGLIB 라이브러리 )  
> 
> - 대상: 인터페이스를 상속받지 않은 클래스는 모두 CGLIB 프록시로 생성함.  
> 바이트코드 조작하여 메서드 호출전에 Advice 적용하여 원하는 로직을 삽입하는 형태로 런타임에 서브클래스가 생성되어 동작함.  
>   
> `java.lang.reflect.Proxy` 생성하기 위해서는 인터페이스가 필수로 존재해야하는데 Spring 에서 빈 생성 조건에는 인터페이스를 구현하고있지 않은 클래스도 대상임. Spring Bean 생성은 프록시 기반이므로, 따라서 인터페이스가 없는 클래스도 프록시를 적용할 수 있도록 CGLIB을 사용함.

```java
import org.springframework.cglib.proxy.MethodInterceptor;  
import org.springframework.cglib.proxy.MethodProxy;  
  
import java.lang.reflect.Method;  
  
public class CglibProxyHandler implements MethodInterceptor {  
    private final Object target;  
  
    public CglibProxyHandler(Object target) {  
        this.target = target;  
    }  
  
    @Override  
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {  
        System.out.println("Before CglibProxyHandler.intercept");  
  
        Object result = proxy.invoke(target, args);  
  
        System.out.println("After CglibProxyHandler.intercept");  
  
        return result;  
    }  
}
```

```java
@Service  
public class V2Service {  
  
    // CGLIB  
    public void methodB() {  
        SimplePojo simplePojo = new SimplePojo();  
  
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(SimplePojo.class);  
        enhancer.setCallback(new CglibProxyHandler(simplePojo));  
  
        SimplePojo proxyInstance = (SimplePojo) enhancer.create();  
        proxyInstance.foo();  
    }  
  
    public static void main(String[] args) {  
        V2Service service = new V2Service();  
  
        service.methodA();    
    }  
}

```


### 정리
> 인터페이스가 있으면 JDK Dynamic Proxy, 없으면 CGLIB을 사용함. 
> CGLIB은 바이트코드 조작을 해야하기 때문에 무겁고 느리다는 단점이 있어 Spring에서는 JDK Dynamic Proxy 방식을 우선 사용함.

> [!quote]
> Spring AOP defaults to using standard JDK dynamic proxies for AOP proxies. This enables any interface (or set of interfaces) to be proxied.
> 
> 출처: [Spring Docs - AOP Proxies](https://docs.spring.io/spring-framework/reference/core/aop/introduction-proxies.html)
  


(아직 작성중...) 
