package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGPrompt;
import andrew.BuildingGame.Game.Plot.LocationPrompts;
import andrew.BuildingGame.Game.Plot.Plot;
import andrew.BuildingGame.Main;
import andrew.BuildingGame.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
  Boolean devMode;
  List<Player> participants;
  Player host;
  GameSettings settings;
  World world;
  ScoreboardManager manager;
  Scoreboard board;
  GameTeleport gameTeleport;
  List<Team> playerTeams;
  HashMap<Player, Player> playerChain;
  HashMap<Player, Location> currPlayerLocations;
  HashMap<Player, List<String>> playerPrompts;
  HashMap<Player, List<String>> playerGuesses;
  HashMap<Player, List<Location>> playerPaths;
  HashMap<Location, Plot> cellInformation;
  HashMap<Location, LocationPrompts> locationPrompts;
  BukkitScheduler scheduler;
  BossBar timerBar;
  int currTime;
  int numTicks;
  int gamePhase;  // TODO: Seems redundant to phase
  int buildingRoundNumber;
  GamePhase phase;

  // Tour variables
  List<Plot> startLocations;
  Plot nextCell;
  int stripIndex;
  boolean cellHasNext;

  enum GamePhase {
    UNSTARTED,
    INITPROMPTS,
    BUILDING,
    GUESSBUILD,
    FINALGUESS,
    GUESSWHO,
    ENDGAME,
    ERROR
  }

  public Game(Boolean devMode) {
    this.devMode = devMode;
  }

  public void start(List<Player> participants, Player host, GameSettings settings) {
    this.participants = participants;
    this.host = host;
    this.settings = settings;
    world = host.getWorld();
    gamePhase = 0;
    playerPrompts = new HashMap<>();
    playerGuesses = new HashMap<>();
    locationPrompts = new HashMap<>();
    currPlayerLocations = new HashMap<>();
    startLocations = new ArrayList<>();
    stripIndex = 0;
    cellHasNext = true;
    nextCell = null;

//    if (!verifyGamePrerequisites()) { return; }

    stopTimer();

    GenerateBuildArea buildAreaGenerator = new GenerateBuildArea(participants.size(), host, settings);
    buildAreaGenerator.generate();

    createAndManagePlayerTeams();
    createPlayerChain();
    initializeTeleportOrder();
    phase = GamePhase.UNSTARTED;
  }

  public void stop() {}

  public void stopTimer() {
    if (scheduler == null) {
      scheduler = Bukkit.getServer().getScheduler();
    } else {
      scheduler.cancelTasks((Plugin) Main.main);
    }
  }

  public void startTimer() {
    currTime = settings.getBuildTimeSeconds();
    numTicks = 20;
    tick();
  }

  public void tick() {
    scheduler.scheduleSyncDelayedTask((Plugin) Main.main, new Runnable() {
      public void run() {
        for (Player p : participants) {
          String actionBarMsg;
          switch (phase) {
            case INITPROMPTS -> {
              Util.sendActionBarMessage(p, ChatColor.GOLD + "Enter a prompt with /bgprompt");
            }
            case BUILDING -> {
              if (playerPrompts.get(p).size() > buildingRoundNumber) {
                actionBarMsg = playerPrompts.get(p).get(buildingRoundNumber);
                Util.sendActionBarMessage(p, ChatColor.GOLD + "" + ChatColor.BOLD +
                        "Your prompt is: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + actionBarMsg);
              }
            }
            case GUESSBUILD -> {
              Util.sendActionBarMessage(p, ChatColor.GOLD + "Guess what this build is! Enter your guess with " +
                      "/bgprompt");
            }
            case GUESSWHO -> {
              Util.sendActionBarMessage(p, ChatColor.GOLD + "Guess who you've been following this game! " +
                      "Enter your guess with /bgprompt");
            }
          }
        }

        if (numTicks == 0) {
          tickSecond();
          numTicks = 20;
        }

        numTicks--;
        Game.this.tick();
      }
    }, 1L);
  }

  public void tickSecond() {
    // BossBar timer update
    if (currTime > 0) { currTime -= 1; }
    if (timerBar != null) { Util.updateBossBarTimer(timerBar, currTime, settings.getBuildTimeSeconds()); }
  }

  private boolean verifyGamePrerequisites() {
    if (participants.size() < 4) {
      Bukkit.broadcastMessage(ChatColor.RED + "Requires at least 4 players to play.");
      return false;
    }
    return true;
  }

  // Creates cycle including all players randomly to distribute prompts
  private void createPlayerChain() {
    Random rand = new Random();
    playerChain = new HashMap<>();

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
  }

  // Based on the player chain, assign each player a prompt
  private void distributePrompts(boolean assignSelf) {
    HashMap<Player, String> prompts = BGPrompt.getPrompts();
    for (Player p : participants) {
      String playerPrompt;
      Player prompter;
      if (assignSelf) {
        prompter = p;
      } else {
        prompter = playerChain.get(p);
      }
      playerPrompt = prompts.get(prompter);

      Location currLocation = currPlayerLocations.get(p);
      if (locationPrompts.containsKey(currLocation)) {
        locationPrompts.get(currLocation).setPromptOrGuess(playerPrompt, prompter);
      }

      if (playerPrompts.containsKey(p)) { playerPrompts.get(p).add(playerPrompt); }
      else {
        List<String> playerPromptList = new ArrayList<>();
        playerPromptList.add(playerPrompt);
        playerPrompts.put(p, playerPromptList);
      }
    }
    BGPrompt.clearPrompts();
  }

  private void initializeTeleportOrder() {
    gameTeleport = new GameTeleport(host.getLocation(), participants, playerChain, settings,
            Util.calculateNumBuildRounds(participants.size()));
    playerPaths = gameTeleport.getPlayerPaths();
    cellInformation = gameTeleport.getCellListings();

    for (Player p : participants) {
      for (Location l : playerPaths.get(p)) {
        if (!locationPrompts.containsKey(l)) {
          LocationPrompts lp = new LocationPrompts(l);
          locationPrompts.put(l, lp);
        }
      }
    }
  }

  private void initializeTimerBar() {
    timerBar = Util.createBossBarTimer(settings.getBuildTimeSeconds());
    Util.addPlayersToBossBar(participants, timerBar);
  }

  private void clearOldTimer() {
    if (timerBar == null) return;
    timerBar.removeAll();
  }

  private void createScoreBoardAndManager() {
    manager = Bukkit.getScoreboardManager();
    assert manager != null;
    board = manager.getMainScoreboard();
  }

  private void createAndManagePlayerTeams() {
    playerTeams = new ArrayList<>();
    createScoreBoardAndManager();

    for (Team team : board.getTeams()) { team.unregister(); }

    for (Player p : participants) {
      Team team = board.registerNewTeam("Team " + p.getDisplayName());
      team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
      team.addEntry(p.getName());
      playerTeams.add(team);
    }
  }

  // TODO: Maybe make an announcement in chat (e.g. Round 3/5)
  public void nextPhase() {
    if (phase == GamePhase.ENDGAME) {
      stopTimer();
      populateCellInformation();
      return;
    }
    if (phase != GamePhase.UNSTARTED) { teleportNextPhase(); }
    switch (phase) {
      case INITPROMPTS, GUESSBUILD -> {
        endPromptPhase();
        startBuildPhase();
      }
      case FINALGUESS, BUILDING -> {
        endBuildPhase();
        startPromptPhase();
      }
    }
    phase = determineNextPhase();
    stopTimer();
    startTimer();
  }

  private GamePhase determineNextPhase() {
    if (gamePhase >= Util.calculateNumRounds(participants.size())) {
      return GamePhase.ENDGAME;
    }

    switch (phase) {
      case UNSTARTED:
        return GamePhase.INITPROMPTS;
      case INITPROMPTS:
      case GUESSBUILD:
        for (Player p : participants) {
          locationPrompts.get(currPlayerLocations.get(p)).setBuilder(p);
        }
        return GamePhase.BUILDING;
      case BUILDING:
        return GamePhase.GUESSBUILD;
      case FINALGUESS:
      case GUESSWHO:
        return GamePhase.ENDGAME;
      default:
        return GamePhase.ERROR;
    }
  }

  private void startBuildPhase() {
    // TODO: Add 3 second countdown with Minecraft title feature
    initializeTimerBar();
  }

  private void endBuildPhase() {
    clearOldTimer();
    buildingRoundNumber++;
  }

  private void startPromptPhase() {

  }

  private void endPromptPhase() {
    if (phase == GamePhase.INITPROMPTS && settings.getBuildOwnFirstPrompt()) { distributePrompts(true);
    } else { distributePrompts(false); }
  }

  private void teleportNextPhase() {
    for (Player p : participants) {
      if (gamePhase >= playerPaths.get(p).size()) { continue; }
      p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
      Location playerNextLocation = playerPaths.get(p).get(gamePhase);
      currPlayerLocations.put(p, playerNextLocation);
      p.teleport(playerNextLocation);
    }
    gamePhase++;
  }

  private void populateCellInformation() {
    for (Player p : participants) {
      for (Location location : playerPaths.get(p)) {
        Plot cell = cellInformation.get(location);
        LocationPrompts prompts = locationPrompts.get(location);
        cell.setBuildGuess(prompts.getGuess());
        cell.setBuildGuesser(prompts.getGuesser());
        cell.setBuildingPrompt(prompts.getPrompt());
        cell.setInitialPromptGiver(prompts.getPrompter());

        Location nextLocation = new Location(location.getWorld(), location.getBlockX() + settings.getBuildAreaXOffset(), location.getBlockY(), location.getBlockZ());
        if (cellInformation.containsKey(nextLocation)) { cell.setNextCell(nextLocation); }
      }
    }

    for (Player p : participants) {
      startLocations.add(cellInformation.get(playerPaths.get(p).get(0)));
    }
  }

  public void tourBuilds() {
    if (nextCell == null && stripIndex < startLocations.size()) {
      nextCell = startLocations.get(stripIndex);
    }
    Location nextBuildLocation = nextCell.getCellLocation();
    Bukkit.broadcastMessage(ChatColor.WHITE + nextCell.getBuildingPrompt() + " built by: " + nextCell.getPromptBuilder().getName());
    Bukkit.broadcastMessage(ChatColor.WHITE + nextCell.getBuildGuess() + " guessed by: " + nextCell.getBuildGuesser().getName());
    for (Player p : participants) {
      p.teleport(nextBuildLocation);
    }

    if (nextCell.getNextCell() == null) {
      nextCell = null;
      stripIndex++;
    } else {
      nextCell = cellInformation.get(nextCell.getNextCell());
    }
  }
}
