package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGPrompt;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

public class Game {
  // Static Game Data
  GameSettings settings;
  GameVars vars;
  Player host;
  World world;
  List<Player> participants;
  Location[][] plotGrid;

  // Dynamic Game Data
  Timer timer;
  TeamManager teamManager;
  HashMap<Location, BuildingPlot> plotsData;
  HashMap<Player, PlayerData> participantsData;
  HashMap<Player, Player> playerChain;
  HashMap<Player, String> currPromptStrings;
  GameStateManager.GameState gameState;
  int roundNumber;  // Determines index in PlayerData path array to teleport players
  int[] tourIndices; // plotGrid indices we are currently in, empty uninitialized

  public Game(GameSettings settings, GameVars vars) {
    // Static Data Initialization
    this.settings = settings;
    this.vars = vars;
    this.host = vars.getHost();
    this.world = vars.getWorld();
    this.participants = vars.getParticipants();
    plotGrid = GameInit.initPlotGrid(settings, vars);

    // Dynamic Data Initialization
    timer = new Timer(settings, vars);
    teamManager = new TeamManager(participants);
    plotsData = new HashMap<>();
    participantsData = new HashMap<>();
    playerChain = new HashMap<>();
    BuildArea buildArea = new BuildArea(settings, vars);
    GameInit.generateBuildCells(buildArea, plotGrid);
    plotsData = GameInit.initPlotsData(plotGrid);

    HashMap<Player, List<Location>> playerPaths = GameInit.generatePlayerPaths(vars, plotGrid);
    playerChain = GameInit.createPlayerChain(participants);
    teamManager.resetPlayerTeams();
    for (Player p : participants) {
      GameInit.resetPlayerState(p);
      participantsData.put(p, new PlayerData(p, playerPaths.get(p)));
    }
    this.gameState = GameStateManager.GameState.INIT;
    roundNumber = 0;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public void startGame() {
    timer.startNextTimer(plotsData, participantsData, gameState, null);
  }

  public void startTimer(int numSeconds) {
    timer.startNextTimer(plotsData, participantsData, gameState, numSeconds);
  }

  public void advanceGamePhase() {
    if (gameState != GameStateManager.GameState.INIT && gameState != GameStateManager.GameState.INITPROMPT) {
      roundNumber++;
    }
    boolean isFinalRound = roundNumber >= vars.getNumRounds();

    // Update participant and plot data and start next timer based on them
    currPromptStrings = BGPrompt.getPrompts();
    for (Player p: participants) {
      PlayerData data = participantsData.get(p);
      BuildingPlot plot = plotsData.get(data.getLocation());
      updatePlotData(data, plot, isFinalRound);
      if (!isFinalRound) { updateParticipantData(data); }
    }

    gameState = GameStateManager.getNextState(gameState, isFinalRound);
    timer.startNextTimer(plotsData, participantsData, gameState, null);

    if (!isFinalRound && gameState != GameStateManager.GameState.INIT && gameState != GameStateManager.GameState.INITPROMPT) {
      for (Player p: participants) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
        Location teleportLocation = participantsData.get(p).path.get(roundNumber);
        p.teleport(Util.offsetTeleport(settings, teleportLocation));
      }
    }
    if (isFinalRound) { teamManager.clearPlayerTeams(); }
    BGPrompt.clearPrompts();
  }

  private void updateParticipantData(PlayerData pd) {
    pd.setLocation(pd.getLocationAtIndex(roundNumber));
  }

  private void updatePlotData(PlayerData pd, BuildingPlot bp, boolean isFinalRound) {
    // TODO: Enable assigning self own prompt
    if (gameState == GameStateManager.GameState.INITPROMPT) {
      Player playerGivingPrompt = pd.getPlayer();
      Player playerReceivingPrompt = playerChain.get(playerGivingPrompt);
      PlayerData pdOfReceivingPlayer = participantsData.get(playerReceivingPrompt);
      BuildingPlot receivingPlayerPlot = plotsData.get(pdOfReceivingPlayer.getLocationAtIndex(0));
      Prompt initPrompt = new Prompt(playerGivingPrompt, currPromptStrings.get(playerGivingPrompt));
      receivingPlayerPlot.setGivenPrompt(initPrompt);
    } else if (gameState == GameStateManager.GameState.BUILD) {
      bp.setBuilder(pd.getPlayer());
    } else if (gameState == GameStateManager.GameState.GUESS) {
      // TODO: Combine this with INITPROMPT but set the guess to only be for GameState.GUESS
      Player currPlayer = pd.getPlayer();
      Prompt guess = new Prompt(currPlayer, currPromptStrings.get(currPlayer));
      plotsData.get(pd.getLocation()).setGuessedPrompt(guess);

      if (!isFinalRound) {
        Player playerReceivingPrompt = playerChain.get(currPlayer);
        PlayerData pdOfReceivingPlayer = participantsData.get(playerReceivingPrompt);
        BuildingPlot receivingPlayerPlot = plotsData.get(pdOfReceivingPlayer.getLocationAtIndex(roundNumber));
        Prompt initPrompt = new Prompt(currPlayer, currPromptStrings.get(currPlayer));
        receivingPlayerPlot.setGivenPrompt(initPrompt);
        // TODO: set guessed prompt as the prompt for the next location, should replicate code above
      }
    }
  }

  public void advanceTourPhase() {
    // Instantiate indices and terminate if complete
    if (tourIndices == null) { tourIndices = new int[]{0, 0}; }
    if (tourIndices[0] >= vars.getNumPlayers()) { return; }

    // Execute teleportation
    String promptStringColour = ChatColor.BOLD + "" + ChatColor.YELLOW;
    ChatColor builderNameColour = ChatColor.GREEN;
    ChatColor promptNameColour = ChatColor.RED;
    ChatColor guesserNameColour = ChatColor.DARK_GREEN;
    ChatColor guessStringColour = ChatColor.GOLD;
    ChatColor regularColour = ChatColor.WHITE;

    Location tpLocation = plotGrid[tourIndices[1]][tourIndices[0]];
    BuildingPlot plotData = plotsData.get(tpLocation);
    Prompt givenPrompt = plotData.getGivenPrompt();
    Prompt guessedPrompt = plotData.getGuessedPrompt();
    String separator = ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 80);
    String buildTitle = "\"" + promptStringColour + givenPrompt.getPromptString() + regularColour + "\"" + " built by "
            + builderNameColour + plotData.getBuilder().getName() + "\n";
    String promptGiver = regularColour + "Prompt given by " + promptNameColour +
            givenPrompt.getPromptGiver().getName() + "\n";
    String guessString = guesserNameColour + guessedPrompt.getPromptGiver().getName() + "'s" + regularColour +
            " guess: " + guessStringColour + guessedPrompt.getPromptString() + "\n";

    for (Player p : participants) {
      p.teleport(Util.offsetTeleport(settings, tpLocation));
      p.sendMessage(separator);
      p.sendMessage(buildTitle);
      p.sendMessage(promptGiver);
      p.sendMessage(guessString);
      p.sendMessage(separator);
    }

    // Move to the next cell if possible, otherwise reset
    tourIndices[1] = (tourIndices[1] + 1) % vars.getNumBuildRounds();
    // If tourIndices == 0, we are entering a new strip
    if (tourIndices[1] == 0) {
      tourIndices[0]++;
    }
  }
}
