package andrew.BuildingGame.Game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.List;

public class Game {
  Boolean devMode;
  List<Player> participants;
  HashMap<String, Integer> settings;

  public Game(Boolean devMode, List<Player> participants, HashMap<String, Integer> settings) {
    this.devMode = devMode;
    this.participants = participants;
    this.settings= settings;
  }

  public void Start() {

  }
}
