package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameVars {
  Player host;
  World world;
  Location origin;
  List<Player> participants;
  List<Material> buildRoundMaterials;
  int numPlayers;
  int numRounds;
  int numBuildRounds;
  Location spawnTeleport;

  public GameVars(Player host, World world, List<Player> participants) {
    this.host = host;
    this.world = world;
    this.participants = participants;

    origin = host.getLocation();
    numPlayers = participants.size();
    numRounds = calculateNumRounds();

    buildRoundMaterials = selectBuildRoundMaterials();

    // TODO: Allow for dynamic spawnTeleport location setting
    spawnTeleport = new Location(world, 112, 64, -27);
  }

  public Player getHost() {
    return host;
  }
  public World getWorld() {
    return world;
  }
  public Location getOrigin() {
    return origin;
  }
  public List<Player> getParticipants() {
    return participants;
  }
  public Location getSpawnTeleport() { return spawnTeleport; }
  public int getNumPlayers() {
    return numPlayers;
  }
  public int getNumRounds() {
    return numRounds;
  }
  public int getNumBuildRounds() {
    if (numPlayers > 4) {
      return 3;
    } else {
      return 2;
    }
  }
  private int calculateNumRounds() {
    if (numPlayers > 4) {
      return 6;
    } else {
      return 4;
    }
  }
  private List<Material> selectBuildRoundMaterials() {
    HashSet<Material> selectedMaterials = new HashSet<>();
    Material[] terracottas = new Material[]{
            Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.CYAN_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA, Material.LIME_TERRACOTTA, Material.MAGENTA_TERRACOTTA,
            Material.ORANGE_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.RED_TERRACOTTA,
            Material.WHITE_TERRACOTTA, Material.YELLOW_TERRACOTTA
    };

    while (selectedMaterials.size() < 3) {
      int selectedTerracotta = ThreadLocalRandom.current().nextInt(0, terracottas.length);
      selectedMaterials.add(terracottas[selectedTerracotta]);
    }

    return new ArrayList<>(selectedMaterials);
  }

  public List<Material> getBuildRoundMaterials() {
    return buildRoundMaterials;
  }
}
