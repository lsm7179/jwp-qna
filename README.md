#1단계 요구사항
JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

* DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
~~~ 
create table answer
  (
  id          bigint generated by default as identity,
  contents    clob,
  created_at  timestamp not null,
  deleted     boolean   not null,
  question_id bigint,
  updated_at  timestamp,
  writer_id   bigint,
  primary key (id)
  )  
~~~
@DataJpaTest를 사용하여 학습 테스트를 해 본다.


#2단계 - 연관 관계 매핑
요구 사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

아래의 DDL을 보고 유추한다.
~~~
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user
~~~

* [x] answer - question 연관관계를 매핑한다.
  * 일대다(1:N) 관계다
* [x] answer - user 연관관계를 매핑한다.
  * 다대일(N:1) 관계다
* [x] delete_history - user 연관관계를 매핑한다.
  * 다대일(N:1) 관계다
* [x] question - user 연관관계를 매핑한다.
  * 다대일(N:1) 관계다

#3단계 질문 삭제하기 리팩터링
## 기능 요구 사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* [ ] 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
* [ ] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
* [ ] 답변이 없는 경우 삭제가 가능하다.
* [ ] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
* [ ] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
* [ ] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
* [ ] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

##프로그래밍 요구 사항
* qna.service.QnaService의 deleteQuestion()는 앞의 질문 삭제 기능을 구현한 코드이다. 이 메서드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
* 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
* 리팩터링을 완료한 후에도 src/test/java 디렉터리의 qna.service.QnaServiceTest의 모든 테스트가 통과해야 한다.
  * 도메인 객체 중심으로 비즈니스로직을 이전하자.

##힌트1
* 객체의 상태 데이터를 꺼내지(get)말고 메시지를 보낸다.
* 규칙8: 일급 콜렉션을 쓴다.
  * Qustion의 List<Answer>를 일급 콜렉션으로 구현해본다.
* 규칙7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
  * 인스턴스 변수의 수를 줄이기 위해 도전한다.
##힌트2
* 테스트하기 쉬운 부분과 테스트하기 어려운 부분을 분리해 테스트 가능한 부분만 단위테스트한다.


## 공부해야되는 내용
* Optional 객체
* @LastModifiedDate 어노테이션