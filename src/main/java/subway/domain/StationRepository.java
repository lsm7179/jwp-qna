package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository는 왜 Repository일까?                   entity, id
 * 기본적으로 빈으로 등록된다.
 * */
public interface StationRepository extends JpaRepository<Station, Long>{

    Station findByName(String name); //쿼리메소드
}
