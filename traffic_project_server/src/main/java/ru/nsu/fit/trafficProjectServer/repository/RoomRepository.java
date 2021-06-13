package ru.nsu.fit.trafficProjectServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.trafficProjectServer.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
