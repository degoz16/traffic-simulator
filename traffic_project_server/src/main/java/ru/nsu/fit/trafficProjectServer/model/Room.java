package ru.nsu.fit.trafficProjectServer.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import ru.nsu.fit.trafficProjectServer.security.model.User;

@Getter
@Setter
@Entity
@Table(name = "traffic_rooms")
@SequenceGenerator(
  name = "room_seq",
  sequenceName = "ROOM_SEQ",
  allocationSize = 1
)
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
  private Long id;

  private String name;

  @OneToOne
  private Map globalMap;

  @ManyToOne
  private User adminUser;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
  @OrderColumn(name = "FOLLOWUP_NUMBER")
  private List<Map> maps = new ArrayList<>();

  public void addMap(Map map) {
    maps.add(map);
  }

  public Room() {

  }
}
