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

@Getter
@Setter
@Entity
@Table(name = "traffic_maps")
@RequiredArgsConstructor
@SequenceGenerator(
  name = "map_seq",
  sequenceName = "MAP_SEQ"
)
public class Map {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_seq")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @Formula(value="FOLLOWUP_NUMBER")
  private Long followUpNumber;

  @Lob
  private byte[] file;

  private String filename;
}
