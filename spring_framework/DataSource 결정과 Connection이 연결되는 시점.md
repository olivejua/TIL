#spring #datasource #db-connection


- datasource가 결정되고, Connection이 적용되는 시점
- 트랜잭션이 걸려있을 때와 걸려있지 않은 때의 차이
- LazyConnectionDatasourceProxy를 걸면 안걸때와 무슨 차이가 있는 건가


## Datasource가 결정되는 시점

### 패키지 기준 적용

> 패키지 단위로 적용이 된다는 것은 쿼리를 실행할 때마다 새로운 Connection을 사용한다는 뜻이다. 쿼리 실행을 마치면 Connection이 닫히고 반납한다.

#### 1. `@MapperScan` Mapper가 속한 패키지 기준으로 사용되는 경우 

1. (datasource 설정) MyBatis 설정은 기본적으로 Datasource 빈생성과 이 빈을 주입받아 SqlSessionFactory를 생성한다.
2. (애플리케이션 실행 시) 애플리케이션이 실행되는 시점에 어느 패키지에서 어떤 SqlSessionFactory를 사용할지 결정이 된다.
3. (런타임에 Mapper 쿼리 실행 시) Mapper에서의 쿼리를 실행할 때 SqlSession을 주입해야하는데 이 때 이미 패키지단위로 연결해놓은 `SqlSessionFactory`에서 `SqlSession(Datasource가 바인딩된)`을 가져와 연결을 해준다. 


### 트랜잭션 기준 적용

> 트랜잭션 단위로 적용이 된다는 것은 해당 트랜잭션을 실행하는 동안은 하나의 세션을 유지한 채 쿼리를 실행한다는 것을 의미한다. 기본 트랜잭션 Datasource 설정을 한다는 가정하에 Spring 에서는 기본적으로 트랜잭션당 하나의 Connection만 연결 가능하다. 

`A_DB`와 `B_DB`가 별도로 나누어져있고, 각각 `aMapper`, `bMapper`에서 접근한다고 가정하자.  `transactionManagerA` -> `aDatasource`, `transactionManagerB` -> `bDatasource`에 대한 트랜잭션을 각각 관리한다고 하자. 
#### 1. `@Transactional("transactionManagerA")`

```java
public class FooService {
	...
	
	@Transactional("transactionManagerA") // A_DB datasource 접근가능
	public void process() {
		aMapper.insert(); // A_DB 사용
		bMapper.insert(); // 다른 트랜잭션 사용 불가!! (예외 발생)
	}
	
	...
}

```

### 동적 Datasource 라우팅도 가능하다

요청이 들어올 때 동적으로 Datasource를 적용할 수도 있다. AbstractRoutingDatasource를 상속받아 구현하면 된다. 
datasource는 모두 등록해놓았다고 가정하자. 

```java
public class DynamicRoutingDatasource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContextHolder.getCurrentDb();
	}
}

public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dynamicDataSource (@Qualifier("aDataSource") DataSource aDataSource, @Qualifier("bDataSource" DataSource bDataSource)) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("a", aDataSource);
		targetDataSources.put("b", bDataSource);
		
		DynamicRoutingDatasource dynamicRoutingDatasource = new DynamicRoutingDataSource();
		dynamicRoutingDatasource.setTargetDataSources(targetDataSources);
		dynamicRoutingDatasource.setDefaultTargetDataSource(firstDataSources);
		
		return dynamicRoutingDatasource;
	}

}
```

사용하려면 애노테이션을 서비스 로직에 추가하여 AOP로 어떤 Datasource를 사용할지 주입할 수도 있다. 

## 동적 Routing DataSource 적용 + 여러 Datasource를 접근할 때  

### 트랜잭션이 걸려있지 않은 경우 

보통 트랜잭션이 걸려 있지 않은 경우 패키지 스캔으로 적용을 하게 되고, 이 경우 실행하고자 하는 Mapper에는 이미 어떤 DataSource를 사용할지 정해져있기 때문에 명확하다. 

```java
@RequiredArgsConstructor
@Service
public class AService {
	private final AMapper aMapper;
	private final BService bService;
	
	@UseDataSource("a") // A_DB 사용
	public void execute() {
		aMapper.insert();
		
		bService.execute();
		
		aMapper.update();
	}
}

@RequiredArgsConstructor
@Service
public class BService {
	private final BMapper bMapper;
	
	@UseDataSource("b") // B_DB 사용
	public void execute() {
		bMapper.insert();
	}
}
```


위와 같은 로직을 실행한다고 가정하자. aService.execute()를 실행했을 때 문제가 발생하지 않을까? 

답은 에러가 발생한다. 
1. `AService.execute()` 실행 -> `DynamicDataSourceContextHolder.setDataSourceKey("a")`로 데이터 소스가 설정됨
	1. `A_DB`로 데이터 소스 설정됨
	2. `aMapper.insert()` 실행 -> `A_DB`에서 실행
2. `bService.execute()` 호출
	1. `@UseDataSource("b")` 적용 -> `DynamicDataSourceContextHolder.setDataSourceKey("b")`로 데이터 소스가 설정됨
	2. `DynamicDataSourceContextHolder`값이 `B_DB` 로 데이터 소스가 변경됨
	3. `bMapper.insert()` 실행 -> `B_DB`에서 실행
3. `aMapper.update()` 실행
	1. 여전히 `DynamicDataSourceContextHolder`값이 `B_DB` 로 데이터소스가 남아있음.
	2. aMapper.update()가 `B_DB`에서 실행됨 (`A_DB`에서 실행해야함. **에러!!**)

설정시 ThreadLocal에서 DataSource를 저장하고 있기 때문에 데이터소스가 AOP에 의해 업데이트되면서 기존 로직으로 돌아왔을 때 이전에 업데이트 되었던 데이터소스가 그대로 남아있는 이슈이다. 

### 해결방법

#### 1) 트랜잭션을 분리하자

트랜잭션을 분리해도 ThreadLocal은 공유한다. 하지만 트랜잭션은 시작전에 기존 상태를 스냅샷을 저장하고 트랜잭션이 종료될 때 기존 상태로 복구해놓기 때문에 이 방법으로도 해결이 가능하다. 

##### Spring 트랜잭션이 ThreadLocal을 복구하는 과정
1. `AService.execute()`가 실행될 때 A_DB를 사용 (ThreadLocal = "a")
2. `bService.execute()`가 실행되면서, Spring이 트랜잭션을 새로 시작
	1. ThreadLocal = "b"로 변경됨
3. `bService.execute()`가 끝나면, Spring이 트랜잭션을 커밋하고 원래 ThreadLocal로 복구
	1. 즉, "b"를 제거하고 "a"로 복원
4. `AService.execute()`가 계속 실행되므로, `aMapper.update()`는 `A_DB`에서 실행

##### 트랜잭션 사용시 ThreadLocal 상태 복구
`TransactionSynchronizationManager`라는 클래스를 사용하여 트랜잭션 상태를 관리한다. 
즉, Spring이 `TransactionSynchronizationManager`을 이용하여 ThreadLocal을 트랜잭션이 끝날 때 원래 상태로 복구해준다.
```
// 트랜잭션이 시작될 때
TransactionSynchronizationManager.bindResource(dataSource, transactionStatus);

// 트랜잭션이 종료될 때
TransactionSynchronizationManager.unbindResource(dataSource);
```


이 말 뜻은 같은 Transaction 내에서 호출을 한다면 데이터 소스 또한 공유하게 되어 기존 현상을 여전히 남아있다는 뜻이기 때문에 이점 주의하자.

#### 2) AOP에서 비즈니스 로직 실행 후 실행전 DataSource로 복구해주자

```java
@Aspect
@Component
public class DataSourceAspect {

    @Around("@annotation(useDataSource)")
    public Object switchDataSource(ProceedingJoinPoint joinPoint, UseDataSource useDataSource) throws Throwable {
        String previousDataSource = DynamicDataSourceContextHolder.getDataSourceKey(); // 기존 데이터소스 저장
        try {
            DynamicDataSourceContextHolder.setDataSourceKey(useDataSource.value()); // 새로운 데이터소스 적용
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.setDataSourceKey(previousDataSource); // 기존 데이터소스로 복구
        }
    }
}
```




