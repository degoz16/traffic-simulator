package ru.nsu.fit.trafficProjectServer.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.trafficProjectServer.model.Map;

public interface MapRepository extends JpaRepository<Map, Long> {
  Map findByRoomIdAndFollowUpNumber(Long room_id, Long followUpNumber);
}
