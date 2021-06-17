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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map getGlobalMap() {
    return globalMap;
  }

  public void setGlobalMap(Map globalMap) {
    this.globalMap = globalMap;
  }

  public User getAdminUser() {
    return adminUser;
  }

  public void setAdminUser(User adminUser) {
    this.adminUser = adminUser;
  }

  public List<Map> getMaps() {
    return maps;
  }

  public void setMaps(List<Map> maps) {
    this.maps = maps;
  }

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map getGlobalMap() {
    return globalMap;
  }

  public void setGlobalMap(Map globalMap) {
    this.globalMap = globalMap;
  }

  public User getAdminUser() {
    return adminUser;
  }

  public void setAdminUser(User adminUser) {
    this.adminUser = adminUser;
  }

  public List<Map> getMaps() {
    return maps;
  }

  public void setMaps(List<Map> maps) {
    this.maps = maps;
  }
}
