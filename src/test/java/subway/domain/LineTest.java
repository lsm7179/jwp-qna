package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineTest {
    @Autowired
    private LineRepository lines;

    @Autowired StationRepository stations;

    @Test
    void saveWithLine() {
        Station expected = new Station("잠실역");
        //expected.setLine(new Line("2호선")); 에러가 났다 Line이 영속상태가 아니란 것이다.
        expected.setLine(lines.save(new Line("2호선"))); //이렇게 하면 된다.
        Station actual = stations.save(expected);
        stations.flush(); //  transaction commit
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));//영속 상태여야 한다.
        stations.flush(); // transaction commit
    }

    @Test
    void removeLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(null);
        stations.flush(); // transaction commit
    }

    @Test
    void findById() {
        Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
        lines.flush(); // transaction commit
    }


}
