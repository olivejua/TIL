
## CloudFront의 역할

CloudFront는 다양한 국가, 지역에서 요청하는 정적파일을 빠르게 찾아올수 있도록 각각의 엣지로케이션에 파일을 캐싱해두고 사용자가 요청하는 곳과 가장 가까운 엣지로케이션에서 가져와 네트워크 비용을 줄이고 응답을 빠르게 해주는 AWS 에서 제공해주는 서비스이다. 그리고 이러한 방식의 서비스를 `CDN(Content Delivery Network)`라고 한다. 

컨텐츠라고 한다면 HTTP 로 전송할 수 있는 모든 데이터인데, 주로 가공될일이 많지 않은 정적파일(이미지나 파일, 동영상 등)을 대부분 사용한다. 만약 잘 바뀌는 실시간 데이터라던지 동적 API 응답이라면 캐싱이 오히려 불필요한 과정을 거치게 되어 효과를 보지 못한다. 

AWS는 각국의 엣지로케이션을 위치해두고 있다. ([AWS Cloud Front 엣지 네트워크](https://aws.amazon.com/ko/cloudfront/features/?whats-new-cloudfront.sort-by=item.additionalFields.postDateTime&whats-new-cloudfront.sort-order=desc#Global_Edge_Network))

따라서 요청을 한다면 이런시나리오를 거치게된다. 
1. 한국의 서울 사용자가 image.jpg 를 요청
2. 서울 엣지로케이션에 캐싱된 파일이 있다면 사용자에게 응답
3. 서울 엣지로케이션에 캐싱된 파일이 없다면 원본 서버에서 가져옴
4. 서울 엣지로케이션에 캐싱하고 사용자에게 응답

<img src="https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/images/how-you-configure-cf.png" alt="AWS Cloud Front 이미지"/>
*(출처: Amazon CloudFront란?)*  


여기서 또하나 `Regional Edge Cache` 라는 개념도 있는데 중앙 캐싱 계층이라고 한다. 
사용자가 가까운 엣지로케이션에 요청했을 때 캐싱되어있는 파일이 없거나 만료된 경우 원본서버에 파일을 가져와야한다. 하지만 서버가 멀리 있는 경우의 네트워크 비용을 줄이기 위해 엣지로케이션의 한단계 위에 있는 캐싱계층인 `Regional Edge Cache`에서 컨텐츠를 가져온다.  
보통은 하나의 `Regional Edge Cache`에 10~20개의 엣지로케이션이 공유하고 있고, 엣지 로케이션에서 캐시가 만료되면 Regional Edge Cahce에서 가져올 수 있어야하기 때문에 TTL이 엣지 로케이션보다 `Regional Edge Cache`이 비교적 더 길다. 엣지로케이션은 트래픽에 따라 빨리 삭제될 수도 있다. 

예를 들면 한국의 사용자가 `image.jpg`를 요청했는데 서울의 엣지로케이션이 지워졌을 때 도쿄의 `Regional Edge Cache`에 남아 있다면, 원본 서버까지 가지 않고 그곳에서 가져오게 된다. 


### Cloud Front가 효과를 보려면...

만약 서버가 한국에 있고, 컨텐츠를 요청이 발생하는 곳이 모두 한국이라면 별 효과를 보지 못할 수 있다. 왜냐면 캐싱을 해놓는다는 것은 이것을 일정 주기로 캐시 무효화를 시키고 가져오고를 반복하게 될테고, 조회할 때 또한 캐시가 존재하는지 확인해서 없을 경우 한국 서버에 요청을 할텐데 이미 엣지로케이션에 요청하는 거리와 원본서버에 요청하는 거리가 별 차이가 없을 경우 불필요한 단계를 거치게 되어 그럴 필요가 없다는 거다. 

또한가지는 트래픽이 많이 발생하지 않는다면 이역시도 효과를 크게 보지 못할 수 있다. 캐시의 효과를 보지못할 가능성이 크다. 

하지만 이 반대상황인 경우 효과를 본다. 글로벌 사용자 대상이거나 자주 요청이 되는 경우 Cloud Front를 사용할 이유가 충분하자. 이 경우 Cloud Front 사용을 고려하자. 



---
#### 참고 
- https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/Introduction.html
- https://aws.amazon.com/ko/cloudfront/features/?whats-new-cloudfront.sort-by=item.additionalFields.postDateTime&whats-new-cloudfront.sort-order=desc#Global_Edge_Network