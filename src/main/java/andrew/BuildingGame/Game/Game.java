package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGPrompt;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
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

    playerChain = GameInit.createPlayerChain(participants);
    HashMap<Player, Player> reversePlayerChain = Util.reversePlayerChain(playerChain);
    HashMap<Player, List<Location>> playerPaths = GameInit.generatePlayerPaths(vars, plotGrid, reversePlayerChain);
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

  public GameVars getVars() { return vars; }

  public void startGame() {
    GameInit.teleportPlayersToSpawn(vars);
    Util.sendCustomJsonMessage(participants, JsonStrings.generateIntroText());
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
        p.setGameMode(GameMode.CREATIVE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
        Location teleportLocation = participantsData.get(p).path.get(roundNumber);
        p.teleport(Util.offsetTeleport(settings, teleportLocation));
      }
    }

    if (gameState == GameStateManager.GameState.INITPROMPT)  { Util.sendCustomJsonMessage(participants, JsonStrings.generateInitialPromptText()); }
    if (gameState == GameStateManager.GameState.BUILD) { Util.sendMessageToPlayers(participants, ChatColor.GOLD + "Time to build!"); }
    if (gameState == GameStateManager.GameState.GUESS) { Util.sendCustomJsonMessage(participants, JsonStrings.generateGuessText()); }

    if (isFinalRound) {
      teamManager.clearPlayerTeams();
      Util.sendCustomJsonMessage(participants, JsonStrings.generateBuildingPhaseCompleteText());
      for (Player p : participants) {
        p.setInvulnerable(false);
      }
      saveAllPlotData();
    }
    BGPrompt.clearPrompts();
  }

  private void updateParticipantData(PlayerData pd) {
    pd.setLocation(pd.getLocationAtIndex(roundNumber));
  }

  private void updatePlotData(PlayerData pd, BuildingPlot bp, boolean isFinalRound) {
    // TODO: Enable assigning self own prompt
    if (gameState == GameStateManager.GameState.INITPROMPT) {
      Player playerGivingPrompt = pd.getPlayer();
      Player playerReceivingPrompt;
      if (settings.getBuildOwnFirstPrompt()) {
        playerReceivingPrompt = playerGivingPrompt;
      } else {
        playerReceivingPrompt = playerChain.get(playerGivingPrompt);
      }
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

  private void saveAllPlotData() {
    for (int i = 0; i < plotGrid.length; i++) {
      for (int j = 0; j < plotGrid[0].length; j++) {
        BuildingPlot bp = plotsData.get(plotGrid[i][j]);
        SaveBuildingPlotData saveBpd = new SaveBuildingPlotData();
        saveBpd.savePlotData(bp);
      }
    }
  }

  public void advanceTourPhase() {
    // Instantiate indices and terminate if complete
    if (tourIndices == null) { tourIndices = new int[]{0, 0}; }
    if (tourIndices[0] >= vars.getNumPlayers()) { return; }

    // Execute teleportation
    Location tpLocation = plotGrid[tourIndices[1]][tourIndices[0]];
    for (Player p : participants) { p.teleport(Util.offsetTeleport(settings, tpLocation)); }

    // Generate and send messages
    BuildingPlot plotData = plotsData.get(tpLocation);
    Prompt givenPrompt = plotData.getGivenPrompt();
    Prompt guessedPrompt = plotData.getGuessedPrompt();
    int currPhase = tourIndices[1] + 1;
    int totalPhases = vars.getNumBuildRounds();
    String builderPrompt = givenPrompt.getPromptString();
    String builder = plotData.getBuilder().getName();
    String prompter = givenPrompt.getPromptGiver().getName();
    String guesser = guessedPrompt.getPromptGiver().getName();
    String guess = guessedPrompt.getPromptString();

    if (tourIndices[1] == 0) {
      timer.printNewBuildTextWithDelay(
              currPhase, totalPhases, builderPrompt, builder, prompter, guesser, guess, participants
      );
    } else {
      Util.sendCustomJsonMessage(participants,JsonStrings.generateViewingPhaseText(
              currPhase, totalPhases, builderPrompt, builder, prompter, guesser, guess
      ));
    }

    // Move to the next cell if possible, otherwise reset
    tourIndices[1] = (tourIndices[1] + 1) % vars.getNumBuildRounds();
    // If tourIndices == 0, we are entering a new strip
    if (tourIndices[1] == 0) {
      tourIndices[0]++;
    }
  }
}
