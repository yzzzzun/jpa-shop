# JPAShop project

Spring boot, JPA를 활용 간단한 쇼핑웹 구현

## 학습목표

- JPA기본 강의 이론을 활용
- 예제를 통해 핵심 내용 정리

## 요구사항

- [x] 회원 기능
    - [x] 회원 가입
    - [x] 회원 목록 조회
- [x] 상품 기능
    - [x] 상품 등록
    - [x] 상품 수정
    - [x] 상품 조회
- [x] 주문기능
    - [x] 상품 주문
    - [x] 주문 내역 조회, 검색
    - [x] 주문 취소

- [ ] 상품은 재고 관리해야 한다.
- [ ] 상품 종류 : 도서, 음반, 영화
- [ ] 상품을 카테고리로 구분
- [ ] 상품 주문시 배송정보 입력 가능


- jpa에 집중하기위해 주요 기능에만 집중한다.
    - 로그인 및 권한관리 안함
    - 도서만 활용
    - 카테고리, 배송정보 사용 안함

## Entity 구성

![img.png](./images/EntityDiagram.png)

## Application 아키텍처

### 계층형 구조

- Controller, Web : 웹 계층
- service : 비즈니스 로직, 트랜잭션 처리
- repository : JPA를 직접 사용하는 계층
- domain : 모든 계층 사용

## 정리

- Test와 코드는 별도의 DB를 설정하고 테스트하도록 한다. Test의 경우 메모리 db를 사용해 테스트!

- @Repository, @Service 는 컴포넌트 스캔의 대상이다.

- @PersistenceContext 어노테이션으로 EntityManager를 주입해야 한다.

  - @Autowired를 사용해도 가능하다. 이는 spring boot를 사용하기 때문

- @Transactional의 기본 설정은 readOnly=false로 설정되어 있다.

  - 읽기 전용의 경우 readOnly = true 로 해주면 db리소스를 읽기전용으로 최적화 해준다.

  - 해당 서비스가 읽기 전용이 많은경우, 쓰기가 많은경우를 따져보고 Class 레벨에 Transactional 을 설정한 후, 메서드에 별도의 Transactional을 설정한다.

    > 읽기 전용이 많은 경우 클래스에 @Transactional(readOnly = true) 설정 후 변경과 관련된 메서드에 @Transactional 을 별도로 준다.

- 도메인, 엔티티에 비즈니스 로직을 구현하는걸 도메인 모델 패턴이라고 한다.
  - 테스트에 장점이 있다 - 단위테스트 작성시 편리함
  - Service layer는 Stub을 활용해 테스트, 인수테스트를 참고하자.
- 서비스 레이어에 비즈니스 로직을 구현하는걸 트랜잭션 스크립트 패턴이라 한다.
- Springboot + Spring Data Jpa + QueryDSL 실무에서 생산성을 극대화 할 수 있다.

## 변경감지(DirtyChecking)와 Merge

엔티티를 임의로 만들어내더라도 기존 식별자를 가지고있으면 준영속 엔티티로 볼 수 있다.

준영속 엔티티를 수정하는 방법은 두가지가 있다.

- 변경감지 기능 사용
- 병합(merge) 사용

준영속 엔티티의 문제는 JPA에서 관리를 하지 않는다는 점이다. 변경이 되어도 변경이 저장되지 않는다..

**변경감지 기능 사용**

JPA에서 BestPractice로 제안하는 방법은 DirtyChecking을 사용하는 방법이다.

```
Item findItem = itemRepository.findOne(itemId);
findItem.setPrice(bookParam.getPrice());
findItem.setName(bookParam.getName());
findItem.setStockQuantity(bookParam.getStockQuantity());
```

repository를 통해 찾은 엔티티는 영속상태로 DirtyChecking 대상이다.

**병합(merge) 사용**

```
entitymanager.merge(entityObject);
```

merge를 실행하면 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.(없으면 DB에서 조회)

조회한 영속 엔티티에 파라미터로 넘긴 엔티티의 값을 채워 넣는다.

그후 영속상태인 엔티티를 반환해준다.

**주의**

변경감지를 사용하면 원하는 속성만 변경이 가능, 병합은 모든 값을 다 변경한다. 만약 교체값에 null이 있으면 null로 변경이된다.

## API 개발 기본

Entity를 Controller layer에 노출했을 때 문제점

- Presentation 계층을 위한 로직이 추가됨, validation, json 등..
- 엔티티의 모든값이 노출됨
- 변경시 API 스펙이 변경됨
- 다양한 API를 커버하기 어려워짐. 유지보수성이 낮아짐
- 컬랙션을 바로반환하면 확장이 어려움

## API개발 고급 - 지연로딩과 조회 성능 최적화

### Entity 직접호출(그냥 테스트.. 절대 사용하지말것)

문제발생 지점 -> 양방향 연관관계의 Json파싱 작업시 무한루프 발생

@JsonIgnore로 양방향을 끊어주면 해결될까? -> 아니다.

LazyLoading 설정되어있는 필드들은 Hibernate에서 Proxy객체를 담아두기때문에 파싱에서 오류가 발생한다.

Hibernate5Module을 사용해서 해결하던가 LazyLoading 걸려있는 필드를 호출하면 되는데 딱봐도 이상하다..

그럼 Eager로 변경..? 불필요한 데이터를 가져와 성능문제가 발생할 수 있다. 또한 성능 튜닝이 어려워진다.

**결론. : DTO 를 사용해서 반환하도록하자. 항상 지연로딩을 설정하고 성능 최적화가 필요하면 fetch join을 사용하자.**

 Ex) Order -> 2건의 Order결과 -> LazyLoading으로 Member, Delivery 접근

1+ Member(N) + Delivery(N) = 5건의 쿼리가 발생한다.

N+1 문제

연관관계 매핑시 발생하는 이슈로 쿼리 한번으로 데이터를 N개 가져왔는데 N개의 연관관계만큼 추가로 쿼리가 발생하는 문제

fetch join 을 사용해서 해결한다. 쿼리 한방으로 데이터를 모두 가져온다.

join결과를 DTO를 바로 반환

```
return em.createQuery("select o from Order o"
			+ " join fetch o.member m"
			+ " join fetch o.delivery d", Order.class).getResultList();
```

```
em.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address ) from Order o"
				+ " join o.member m"
				+ " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
```

둘의 차이는 select를 모두하냐, 딱 맞게 조회하냐의 차이가 있다. trade-off가 발생한다.

1번은 재사용성이 높고, 2번은 네트워크 리소스를 적게 먹는다.

2번의경우 화면과 연관성이 깊다. api spec이 변경되면 repository 까지 변경됨.. 

2번 사용시 별도의 패키지를 분리해서 쓰자. Repository 는 순수한 엔티티를 조회하는데 사용

쿼리 방식 선택 권장순서

1. 엔티티를 DTO 로 변환하는 방법을 선택
2. 필요시 페치조인으로 성능 최적화 -> 대부분 해결가능
3. 그래도 안되면 DTO로 직접조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해 SQL을 직접 사용
