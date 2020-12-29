package ru.nsu.fit.traffic.json.parse;

import java.util.Map;
import ru.nsu.fit.traffic.model.map.MainRoadSign;
import ru.nsu.fit.traffic.model.map.RoadSign;
import ru.nsu.fit.traffic.model.map.SignType;
import ru.nsu.fit.traffic.model.map.SpeedLimitSign;

class RoadSignCreator {

  RoadSign createSign(Map<String, String> settings) {
    RoadSign sign = null;
    switch (SignType.valueOf(settings.get("type"))) {
      case MAIN_ROAD:
        sign = new MainRoadSign();
        sign.setSettings(settings);
        break;
      case SPEED_LIMIT:
        sign = new SpeedLimitSign();
        sign.setSettings(settings);
        break;
    }
    if (sign == null) {
      throw new IllegalArgumentException("Wrong settings type");
    }
    return sign;
  }
}
