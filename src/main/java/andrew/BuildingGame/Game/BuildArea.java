package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BuildArea {
  GameSettings settings;
  GameVars vars;
  Player host;
  World world;
  int numPlayers;

  public BuildArea(GameSettings settings, GameVars vars) {
    this.settings = settings;
    this.vars = vars;

    host = vars.getHost();
    world = vars.getWorld();
    numPlayers = vars.getNumPlayers();
  }

  public void generateBuildingCell(Location hostLocation, int rowNum) {
    int padding = settings.getBuildAreaPadding();
    int width = settings.getBuildAreaWidth();
    int length = settings.getBuildAreaLength();
    int height = settings.getBuildAreaHeight();
    int playerX = hostLocation.getBlockX();
    int playerY = hostLocation.getBlockY();
    int playerZ = hostLocation.getBlockZ();

    generateBasePlatform(rowNum, padding, width, length, playerX, playerY, playerZ);
    generateWalls(padding, width, length, height, playerX, playerY, playerZ);
  }

  private void generateBasePlatform(int rowNum, int padding, int width, int length, int playerX, int playerY, int playerZ) {
    for (int x = 0; x < width + (padding * 2); x++) {
      for (int z = 0; z < length + (padding * 2); z++) {
        Block block = world.getBlockAt(playerX + x, playerY - 1, playerZ + z);
        if (x < width + padding && x >= padding && z < length + padding && z >= padding) {
          block.setType(settings.getBaseBlockMaterial());
        } else {
//          block.setType(settings.getPaddingBlockMaterial());
          block.setType(vars.getBuildRoundMaterials().get(rowNum));
        }
      }
    }
  }

  private void generateWall(Location start, Location end) {
    for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
      for (int y = start.getBlockY(); y < end.getBlockY(); y++) {
        for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
          Block block = world.getBlockAt(x, y, z);
          block.setType(settings.getWallsBlockMaterial());
        }
      }
    }
  }

  private void generateWalls(int padding, int width, int length, int height, int playerX, int playerY, int playerZ) {
    generateWall(
            new Location(world, playerX - 1, playerY, playerZ - 1),
            new Location(world, playerX - 1, playerY + height, playerZ + length + padding * 2)
    );
    generateWall(
            new Location(world, playerX - 1, playerY, playerZ + length + padding * 2),
            new Location(world, playerX + width + padding * 2, playerY + height, playerZ + length + padding * 2)
    );
    generateWall(
            new Location(world, playerX + width + padding * 2, playerY, playerZ - 1),
            new Location(world, playerX + width + padding * 2, playerY + height, playerZ + length + padding * 2)
    );
    generateWall(
            new Location(world, playerX - 1, playerY, playerZ - 1),
            new Location(world, playerX + width + padding * 2, playerY + height, playerZ - 1)
    );
  }
}
