package ru.nsu.fit.trafficProjectServer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import ru.nsu.fit.trafficProjectServer.security.model.User;

@Getter
@Setter
@Entity
@Table(name = "traffic_maps")
@RequiredArgsConstructor
@SequenceGenerator(
  name = "map_seq",
  sequenceName = "MAP_SEQ",
  allocationSize = 1
)
public class Map {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_seq")
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getGrabbedByUser() {
    return grabbedByUser;
  }

  public void setGrabbedByUser(User grabbedByUser) {
    this.grabbedByUser = grabbedByUser;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Long getFollowUpNumber() {
    return followUpNumber;
  }

  public void setFollowUpNumber(Long followUpNumber) {
    this.followUpNumber = followUpNumber;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User grabbedByUser;

  @ManyToOne
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @Formula(value="FOLLOWUP_NUMBER")
  private Long followUpNumber;

  @Lob
  private byte[] file;

  private String filename;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getGrabbedByUser() {
    return grabbedByUser;
  }

  public void setGrabbedByUser(User grabbedByUser) {
    this.grabbedByUser = grabbedByUser;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Long getFollowUpNumber() {
    return followUpNumber;
  }

  public void setFollowUpNumber(Long followUpNumber) {
    this.followUpNumber = followUpNumber;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }
}
