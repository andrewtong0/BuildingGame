package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGPrompt;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
      participantsData.put(p, new PlayerData(p, playerPaths.get(p)));
    }
    this.gameState = GameStateManager.GameState.INIT;
    roundNumber = 0;
  }

  public void startGame() {
    timer.startNextTimer(plotsData, participantsData, gameState);
  }

  public void advancePhase() {
    if (gameState != GameStateManager.GameState.INIT && gameState != GameStateManager.GameState.INITPROMPT) {
      roundNumber++;
    }
    // TODO: This might be > if off by one
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
    timer.startNextTimer(plotsData, participantsData, gameState);

    if (!isFinalRound && gameState != GameStateManager.GameState.INIT && gameState != GameStateManager.GameState.INITPROMPT) {
      for (Player p: participants) {
        p.teleport(participantsData.get(p).path.get(roundNumber));
      }
    }
    currPromptStrings = new HashMap<>();
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
}
