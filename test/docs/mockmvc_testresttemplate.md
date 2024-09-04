
> [!note]
> - https://stackoverflow.com/questions/25901985/difference-between-mockmvc-and-resttemplate-in-integration-tests
> - https://spring.io/blog/2012/11/12/spring-framework-3-2-rc1-spring-mvc-test-framework



MockMvc와 TestRestTemplate은 모두 통합테스트를 위한 도구이다. 
두개의 주요차이점은 서블릿 컨테이너를 실제로 띄우냐아니냐의 차이이다. 즉, 요청을 서버 안에서 던지냐, 서버 밖에서 던지냐의 차이인것 같다. 서버 안에서 던진다면 내가 서버에서도 특정범위를 테스트할 수 있을것이고, 밖에서 던진다면 어쩔 수 없이 모든 것을 거쳐야해서 실제 운영환경과 가깝게 테스트하게 될 것이다.


그리고 테스트 범위를 어디까지 두고싶은지에 따라 테스트할 수 있도록 알아보자.

## MockMvc

톰캣서버에서는 Connector와 Engine으로 구성되어있다. 여기서 Connector는 Server Socket을 열어놓고 Listening 상태로 있다가 클라이언트의 요청을 받아 Connection을 맺고, 요청/응답 객체를 생성 및 스레드를 할당을 하여 Engine으로 전달한다. Engine 내에서는 Host -> Context -> Servlet -> 처리할 비즈니스로직 (Controller ...) 으로 볼 수 있다.

MockMvc에서는 실제 서블릿 컨테이너를 띄우지 않고, TestContext 위에 DispatcherServlet을 주입시켜서 Web Layer를 테스트할 수 있다.

즉, MockMvc는 시작점이 클라이언트가 Connection 맺는것부터가 아니라 DispatcherServlet부터다.

### Controller 단위테스트 / Spring MVC 테스트

아래 내용을 테스트하고 싶다면 MockMvc를 사용하는 것으로 만족할 수 있다.
- Request 보냈을 때 Controller API method에 정의한 파라미터 객체와 매핑/변환/추출/유효성 검사가 잘되는지
- 요청에 대한 실패 예외 케이스가 원하는 형태로 응답 잘 되는지
- 정상 응답케이스가 원하는 형태로 잘 오는지
- 인증된 사용자가 잘 받아지는지

```java
@RunWith(SpringJUnit4ClassRunner.class) 
@WebAppConfiguration 
@ContextConfiguration("servlet-context.xml") 
public class SampleTests { 
	@Autowired private WebApplicationContext wac; 
	private MockMvc mockMvc; 
	
	@Before 
	public void setup() { 
		this.mockMvc = webAppContextSetup(this.wac).build(); 
	} 
	
	@Test 
	public void getFoo() throws Exception {
		this.mockMvc.perform(get("/foo").accept("application/json")) 
					.andExpect(status().isOk()) 
					.andExpect(content().mimeType("application/json")) 
					.andExpect(jsonPath("$.name").value("Lee")); 
	} 
}
```


기존 컨트롤러 단위 테스트와 마찬가지로 Spring MVC 테스트는 서블릿 컨테이너 없이 spring-test의 Mock Request, Response를 기반으로 구축한다. 주요 차이점은 실제 Spring MVC 구성이 TestContext 프레임워크를 통해 로드되고, 요청이 실제로 DispatcherServlet 및 런타임에 사용되는 모든 동일한 Spring MVC 인프라를 호출할 수 있다.

그리고 Web Layer 만 집중할 수 있도록 비즈니스 로직이나 데이터베이스 연결설정은 Mock을 주입함으로써 구성할 수 있다. 

```java 
@Configuration 
public class MyConfig { 
	@Bean 
	public FooService fooService() { 
		return Mockito.mock(FooService.class); 
	} 
}
```



## TestRestTemplate

Client-side REST Test를 한다. RestTemplate을 사용하는 코드가 있는 경우 이를 Mocking하거나 실제 구동중인 서버를 대상으로 테스트할 수 있다.

실제 RestTemplate을 사용하지만 실제 요청에 대한 기대값을 검증하기 위해 Stub 응답을 반환하는 사용자 정의 ClientHttpRequestFactory로 구성하는 대체제(?)를 제공한다.

```java
RestTemplate restTemplate = new RestTemplate(); 
MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate); 

mockServer.expect(requestTo("/greeting")) 
		.andRespond(withSuccess("Hello world", "text/plain")); 
		// use RestTemplate ... 

mockServer.verify();
```


기대하는 요청값을 정의하고, 반환할 Stub응답을 정의한다. 그러면 위 예제와 같이 Verify 메서드를 사용하여 모든 요청이 실행되었는지 확인할 수 있다.