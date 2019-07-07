스프링 부트로 REST API 구현
=======================

## 09. Event 생성 API 구현
### 스프링 부트 슬라이스 테스트
* @WebMvcTest
    - **MockMvc 빈**을 자동 설정 해준다. 따라서 빈을 그냥 가져와서 쓰면 된다.
    - 웹 관련 빈만 등록해주기 떄문에 슬라이싱 테스트라고도 부른다.

### MockMvc
* 스프링 MVC 테스트의 핵심 클래스이다.
* MockMvc는 ajax나 client(browser)에서 요청 내용을 controller에서 받아 처리하는 것과 같은 테스트를 진행할 수 있는 것이다.
* 결과값인 status코드로 테스트 통과 여부를 결정한다.
* 웹 서버를 띄우지 않고도 스프링 MVC(DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 사용된다.
    - 단위테스트보다는 느리다.
        + DispathcherServlet 등과 같이 생성해야 하는 요소(객체)가 더 많기 때문
        + 초기 구동되어야 하는 요소들이 있다.

## 10. Event 생성 API 구현: 201 응답 받기
* @RestController
    - @ResponseBody를 모든 메소드에 적용한 것과 동일하다.
* ResponseEntity를 사용하는 이유
    - 응답 코드, 헤더, 본문 모두 다루기 편한 API
* Location URI 만들기
    - HATEOS가 제공하는 LinkTo(), methodOn()사용
* 객체를 JSON으로 변환
    - ObjectMapper 사용
        +  스프링 부트를 사용할 때, Mapping jackson json이 의존성으로 들어가 있을 경우에 ObjectMapper가 자동으로 Bean으로 등록된다. 
        + 따라서, ObjectMapper를 자동주입(@Autowired)하여 사용할 수 있다.
        + ObjectMapper는 객체를 Json 문자열로 변환시켜 준다.
