package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line") // 외래키를 알려줘야한다.! ** mappedBy 를 알려줘야 중요하다!
    private List<Station> stations = new ArrayList<>(); // (2)

    protected Line() {

    }

    public Line(String name) {
        this.name = name;
    }

    public void addStation(Station station) {
        stations.add(station);
        station.setLine(this);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
