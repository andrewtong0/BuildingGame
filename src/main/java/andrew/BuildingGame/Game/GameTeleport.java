package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameTeleport {
  Location initPosition;
  List<Player> participants;
  List<Player> playerOrder;
  HashMap<Player, Player> playerChain;
  HashMap<Player, List<Location>> playerPaths;
  GameSettings settings;
  int numRounds;

  public GameTeleport(Location initPosition, List<Player> participants, HashMap<Player, Player> playerChain,
                      GameSettings settings, int numRounds) {
    this.initPosition = initPosition;
    this.participants = participants;
    this.playerChain = playerChain;
    this.settings = settings;
    this.numRounds = numRounds;

    playerPaths = new HashMap<>();

    generatePlayerOrder();
    generatePlayerPaths();
  }

  private void generatePlayerOrder() {
    playerOrder = new ArrayList<>();
    Player firstPlayer = participants.get(0);
    playerOrder.add(firstPlayer);

    Player nextPlayer = playerChain.get(firstPlayer);
    while(nextPlayer != firstPlayer) {
      playerOrder.add(nextPlayer);
      nextPlayer = playerChain.get(nextPlayer);
    }
  }

  private void generatePlayerPaths() {
    initPlayerPaths();

    for (int i = 0; i < numRounds; i++) {
      Location firstPosition = null;
      Player prevPlayer = null;
      for (int j = 0; j < playerPaths.size(); j++) {
        Location playerLocation = new Location(
                initPosition.getWorld(),
                initPosition.getBlockX() + settings.getBuildAreaXOffset() * i + 2,
                initPosition.getBlockY(),
                initPosition.getBlockZ() + settings.getBuildAreaZOffset() * j + 2
        );
        if (firstPosition == null) { firstPosition = playerLocation; }

        int playerIndex = (i + j) % participants.size();
        Player player = playerOrder.get(playerIndex);
        playerPaths.get(player).add(playerLocation);
        if (prevPlayer != null) { playerPaths.get(prevPlayer).add(playerLocation); }
        prevPlayer = player;
      }
      playerPaths.get(prevPlayer).add(firstPosition);
    }
  }

  private void initPlayerPaths() {
    for (Player p : participants) {  playerPaths.put(p, new ArrayList<>()); }
  }

  public HashMap<Player, List<Location>> getPlayerPaths() { return playerPaths; }
}
