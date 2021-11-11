package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/** 슬라이스어노테이션이라고 한다.
 * 그냥 테스트 메소드에서 실행하면
 * 테이블 생성이 된다.
 * h2디비의 테스트디비의 범위?*/
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
}
