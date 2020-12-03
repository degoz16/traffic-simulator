package ru.nsu.fit.traffic.jsonParse;

import java.util.Map;
import ru.nsu.fit.traffic.model.trafficsign.MainRoadSign;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignType;
import ru.nsu.fit.traffic.model.trafficsign.SpeedLimitSign;

// FIXME: 25.11.2020 ASK DENIS MEGINSKIY HOW TO MAKE IT.
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
