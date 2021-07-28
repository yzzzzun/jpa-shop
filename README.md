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



