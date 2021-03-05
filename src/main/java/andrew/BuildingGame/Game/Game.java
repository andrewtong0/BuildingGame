package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGPrompt;
import andrew.BuildingGame.Main;
import andrew.BuildingGame.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
  Boolean devMode;
  List<Player> participants;
  Player host;
  GameSettings settings;
  World world;
  ScoreboardManager manager;
  Scoreboard board;
  List<Team> playerTeams;
  HashMap<Player, Player> playerChain;
  HashMap<Player, String> playerPrompts;
  HashMap<Player, List<Location>> playerPaths;
  BukkitScheduler scheduler;
  BossBar timerBar;
  int currTime;
  int numTicks;
  int gamePhase;

  public Game(Boolean devMode) {
    this.devMode = devMode;
  }

  public void start(List<Player> participants, Player host, GameSettings settings) {
    this.participants = participants;
    this.host = host;
    this.settings= settings;
    world = host.getWorld();
    gamePhase = 0;

    if (scheduler == null) {
      scheduler = Bukkit.getServer().getScheduler();
    } else {
      scheduler.cancelTasks((Plugin) Main.main);
    }

//    if (!verifyGamePrerequisites()) { return; }

//    GenerateBuildArea buildAreaGenerator = new GenerateBuildArea(participants.size(), host, settings);
//    buildAreaGenerator.generate();

    createAndManagePlayerTeams();
    createPlayerChain();
    initializeTeleportOrder();
    distributePrompts();

    clearOldTimer();
    initializeTimer();

    timer();
  }

  public void stop() {}

  public void timer() {
    currTime = settings.getBuildTimeSeconds();
    numTicks = 20;
    tick();
  }

  public void tick() {
    scheduler.scheduleSyncDelayedTask((Plugin) Main.main, new Runnable() {
      public void run() {
        for (Player p : participants) {
          String playerPrompt = playerPrompts.get(p);
          Util.sendActionBarMessage(p, ChatColor.YELLOW + "" + ChatColor.BOLD +
                  "Your prompt is: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + playerPrompt);
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
    Util.updateBossBarTimer(timerBar, currTime, settings.getBuildTimeSeconds());
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
  private void distributePrompts() {
    Random rand = new Random();
    HashMap<Player, List<String>> prompts = BGPrompt.getPrompts();
    playerPrompts = new HashMap<>();
    for (Player p : participants) {
      List<String> promptsForPlayer = prompts.get(playerChain.get(p));
      String playerPrompt = promptsForPlayer.get(rand.nextInt(promptsForPlayer.size()));
      playerPrompts.put(p, playerPrompt);
    }
  }

  private void initializeTeleportOrder() {
    GameTeleport gameTeleport = new GameTeleport(host.getLocation(), participants, playerChain, settings,
            Util.calculateNumBuildRounds(participants.size()));
    playerPaths = gameTeleport.getPlayerPaths();
  }

  private void initializeTimer() {
    timerBar = Util.createBossBarTimer(settings.getBuildTimeSeconds());
    Util.addPlayersToBossBar(participants, timerBar);
  }

  private void clearOldTimer() {
    if (timerBar == null) return;
    timerBar.removeAll();
    timerBar = null;
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
  public void teleportNextPhase() {
    if (gamePhase >= Util.calculateNumRounds(participants.size())) { return; }
    for (Player p : participants) { p.teleport(playerPaths.get(p).get(gamePhase)); }
    gamePhase++;
  }
}
