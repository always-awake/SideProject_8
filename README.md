스프링 부트로 REST API 구현
=======================

## Event 생성 API 구현
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
* 슬라이스 테스트이기 때문에 웹용 Bean들만 컨테이너에 등록해준다. 때문에 Repository를 Bean으로 등록해주지 않는다.

## Event 생성 API 구현
### 10. 201 응답 받기
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

### 11. EventRespository 구현
* 스프링 데이터 JPA
    - JpaRepository를 상속받아 만들기
* Enum을 JPA 맵핑 시 주의할 점
    - @Enumerated(EnumType.STRING)
    - 기본 값인 EnumType.ORDINAL은 아후에 Enum내 값의 위치가 변경된다면, 아예 다른 데이터가 되기 때문이다.
* @MockBean
    - Mockito를 사용해서 mock 객체를 만들고 Bean으로 등록해 준다.
    - (주의) 기존 빈을 테스트용 빈이 대체 한다.
        + JpaRepository를 상속한 EventRepository는 MockBean에 의해 대체 됨
        + JpaRepository를 상속한 EventRepository를 사용할 수 없을 경우(슬라이스 테스트인 경우)에 테스트용 Bean을 만들지만, 그렇지 않은 상황에서는 JpaRepository를 상속한 EventRepository를 사용하기 위해 @MockBean 애노테이션이 있는 코드를 제거해준다. 

### 12. 입력값 제한하기
* 입력값 제한
    - id 또는 입력 받은 데이터로 계산해야 하는 값들은 입력을 받지 않아야 한다.
    - jackson json이 제공하는 다양한 애노테이션을 사용하는 방법도 있지만, 입력값 제한 외에 유효성, lombok, JPA 등과 관련된 애노테이션 등 도메인 클래스에 굉장히 많은 애노테이션이 추가되어야 한다. -> 따라서 EventDto 이용
    - 입력 값을 받는 DTO(EventDto)를 생성하여 입력값을 제한한다.
        + 단점: 중복 발생
        + 도메인 클래스에서 입력값 제한, 유효성 검사 등의 많은 애노케이션 추가를 방지할 수 있다. 
* **통합 테스트**로 전환
    - @WebMvcTest 를 없애주고, 다음의 애노테이션들을 추가해준다.
        + @SpringBootTest: MockMvc용으로 Mocking한 Dispatcherservlet을 생성해준다.
        + @AutoConfigureMockMvc: MockMvc 객체를 이용하기 위함
    - Repository @MockBean 코드를 제거해준다. (더이상 Repository Mock 객체가 필요하지 않기 때문)
* 통합 테스트(SpringBootTest)의 장점
    - test 코드 생성과 관리가 쉬워진다.( Mocking 해주어야 할 객체들의 수가 없어짐)
    - 모든 Bean들이 스프링 컨테이너에 등록된다.
        + @SpringBootApplication 애노테이션이 있는 곳을 찾아서, 그 곳부터 모든 Bean들을 등록해준다.
    - 애플리케이션을 실제 실행했을 때와 가장 유사한 형태로 테스트가 가능하다.


### 13. 입력값 이외에 에러 발생
* 입력을 원하지 않은 프로퍼티의 데이터가 들어올 경우 에러를 발생시켜 준다.
* ObjectMapper 커스터마이징을 통해 구현 가능 (ObjectMapper를 다양하게 커스터마이징 할 수 있다.)
    - application.properties에 **spring.jackson.deserialization.fail-on-unknown-properties=true
** 입력

### 14. Bad Request 처리하기
* @Valid와 BindingResult (또는 Errors)
    - 컨트롤러가 요청 내용을 엔티티에 바인딩을 할 때, 엔티티 내 애노테이션을 참고해서 검증을 수행한다.
    - BindingResult는 항상 @Valid 바로 다음 인자로 사용해야 한다. (스프링 MVC)
        + 검증을 수행한 결과를 @Valid 애노테이션을 사용한 객체 오른쪽에 있는 객체(BindingResult 타입 또는 Errors 타입)에 넣어준다.
    - @NotNull, @NotEmpty, @Min, @Max 등을 사용해서 입력값을 바인딩할 때 에러를 확인할 수 있다. 
* 도메인 Validator 만들기
    - @Valid와 BindingResult로는 Bad Request로 처리할 수 없을 경우 도메인 Validator을 이용해 더욱 섬세하게 처리해준다.
    - Validator 인터페이스 사용하기 (인터페이스를 사용하지 않고 만들어도 상괍없음)
