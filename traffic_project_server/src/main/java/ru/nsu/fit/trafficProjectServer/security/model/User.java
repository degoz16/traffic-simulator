package ru.nsu.fit.trafficProjectServer.security.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nsu.fit.trafficProjectServer.model.Map;
import ru.nsu.fit.trafficProjectServer.model.Room;

@EqualsAndHashCode(of = {"username", "id"})
@Entity
@Getter
@Setter
@Table(name="traffic_users", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @OneToMany
  private Set<Map> grabbedMaps = new HashSet<>();

  @OneToMany
  private Set<Room> createdRooms  = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Map> getGrabbedMaps() {
    return grabbedMaps;
  }

  public void setGrabbedMaps(Set<Map> grabbedMaps) {
    this.grabbedMaps = grabbedMaps;
  }

  public Set<Room> getCreatedRooms() {
    return createdRooms;
  }

  public void setCreatedRooms(Set<Room> createdRooms) {
    this.createdRooms = createdRooms;
  }

  public void addMap(Map map) {
    grabbedMaps.add(map);
  }

  public void addRoom(Room room) {
    createdRooms.add(room);
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Map> getGrabbedMaps() {
    return grabbedMaps;
  }

  public void setGrabbedMaps(Set<Map> grabbedMaps) {
    this.grabbedMaps = grabbedMaps;
  }

  public Set<Room> getCreatedRooms() {
    return createdRooms;
  }

  public void setCreatedRooms(Set<Room> createdRooms) {
    this.createdRooms = createdRooms;
  }
}
