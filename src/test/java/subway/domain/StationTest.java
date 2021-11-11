package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/** 슬라이스어노테이션이라고 한다.
 * 그냥 테스트 메소드에서 실행하면
 * 테이블 생성이 된다.
 * h2디비의 테스트디비의 범위?
 * 영속성 컨텍스트는 hashMap의 형태로 되어 있다고 생각하는게 편하
 *
 *
 * - 1차 캐시
 * - 동일성 보장 -> 조회를 했더니 저장한 객체와 같은 형상
 * - 트랜잭션을 지원하는 쓰기 지연
 * - 변경 감지
 * - 지연 로딩
 *
 * 커밋되는순간 디비에 쿼리를 날린다.
 * 디비에 생성전략과 관련이 있다.
 * 아이디가 들어가있으면 merge로 하게된다.
 *
 * */
@DataJpaTest // 이 어노테이션안에 트랜잭션어노테이션이 붙어져 있다.
public class StationTest {

    @Autowired
    private StationRepository stations;//왜 s 를 붙일까?

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");

        assertThat(station1.getId()).isEqualTo(station2.getId());
        assertThat(station1.getName()).isEqualTo(station2.getName());
        assertThat(station1).isSameAs(station2);
    }

    @Test
    void findById() {//아하 캐시는 id기반이다.
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();

        assertThat(station1.getId()).isEqualTo(station2.getId());
        assertThat(station1.getName()).isEqualTo(station2.getName());
        assertThat(station1).isSameAs(station2);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }


}
