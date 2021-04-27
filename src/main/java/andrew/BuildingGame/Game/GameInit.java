package andrew.BuildingGame.Game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameInit {
  public static void resetPlayerState(Player p) {
    // Change to creative
    p.setGameMode(GameMode.CREATIVE);
  }

  public static Location[][] initPlotGrid(GameSettings settings, GameVars vars) {
    int numPlayers = vars.getNumPlayers();
    int numBuildRounds = vars.getNumBuildRounds();
    Location[][] plotGrid = new Location[numBuildRounds][numPlayers];
    Location origin = vars.getOrigin();

    for (int row = 0; row < numBuildRounds; row++) {
      int rowOffset = row * settings.getBuildAreaZOffset();
      for (int col = 0; col < numPlayers; col++) {
        int colOffset = col * settings.getBuildAreaXOffset();
        plotGrid[row][col] = new Location(
                vars.getWorld(),
                origin.getBlockX() + colOffset,
                origin.getBlockY(),
                origin.getBlockZ() + rowOffset
        );
      }
    }
    return plotGrid;
  }

  public static HashMap<Player, Player> createPlayerChain(List<Player> participants) {
    Random rand = new Random();
    HashMap<Player, Player> playerChain = new HashMap<>();

    // Store reference to complete cyclic chain
    List<Player> participantsClone = new ArrayList<>(participants);
    Player firstPlayer = participantsClone.get(0);
    participantsClone.remove(0);

    Player player;
    Player prevPlayer = firstPlayer;

    // Iteratively generate chain
    while (participantsClone.size() > 0) {
      int playerIndex = rand.nextInt(participantsClone.size());
      player = participantsClone.get(playerIndex);
      participantsClone.remove(player);
      playerChain.put(prevPlayer, player);
      prevPlayer = player;
    }

    playerChain.put(prevPlayer, firstPlayer);

    return playerChain;
  }

  public static HashMap<Player, List<Location>> generatePlayerPaths(GameVars vars, Location[][] plotGrid,
                                                                    HashMap<Player, Player> playerChain) {
    HashMap<Player, List<Location>> playerPaths = new HashMap<>();
    List<Player> playerChainList = new ArrayList<>();
    Player nextPlayer = vars.getHost();

    for (int i = 0; i < vars.getNumPlayers(); i++) {
      playerChainList.add(nextPlayer);
      nextPlayer = playerChain.get(nextPlayer);
    }

    for (int i = 0; i < vars.getNumPlayers(); i++) {
      List<Location> path = new ArrayList<>();
      // Only advance a row if it is a build row
      boolean isGuessRound = false;
      int col = i;
      int row = 0;
      for (int j = 0; j < vars.getNumRounds(); j++) {
        path.add(plotGrid[row][col]);

        if (isGuessRound) { row++; }
        isGuessRound = !isGuessRound;
        col = (col + 1) % vars.getNumPlayers();
      }
      playerPaths.put(playerChainList.get(i), path);
    }
    return playerPaths;
  }

  public static HashMap<Location, BuildingPlot> initPlotsData(Location[][] plotGrid) {
    HashMap<Location, BuildingPlot> plotsData = new HashMap<>();

    for (int row = 0; row < plotGrid.length; row++) {
      for (int col = 0; col < plotGrid[0].length; col++) {
        Location curr = plotGrid[row][col];
        plotsData.put(curr, new BuildingPlot(curr));
      }
    }
    return plotsData;
  }

  public static void generateBuildCells(BuildArea buildArea, Location[][] plotGrid) {
    for (int row = 0; row < plotGrid.length; row++) {
      for (int col = 0; col < plotGrid[0].length; col++) {
        Location curr = plotGrid[row][col];
        buildArea.generateBuildingCell(curr);
      }
    }
  }
}
