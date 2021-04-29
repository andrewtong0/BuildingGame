package andrew.BuildingGame.Game;

import andrew.BuildingGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Timer {
  GameSettings settings;
  GameVars vars;
  BukkitScheduler scheduler;
  BossBar timerBar;

  GameStateManager.GameState currState;
  HashMap<Location, BuildingPlot> plotsData;
  HashMap<Player, PlayerData> participantsData;
  int ticksLeftInSecond;
  int currTime;
  int messageDelay;

  public Timer(GameSettings settings, GameVars vars) {
    this.settings = settings;
    this.vars = vars;
    this.scheduler = Bukkit.getServer().getScheduler();
    this.currState = GameStateManager.GameState.INIT;
  }

  public void startNextTimer(HashMap<Location, BuildingPlot> plotsData, HashMap<Player, PlayerData> participantsData,
                             GameStateManager.GameState newState, Integer timerDurationSeconds) {
    stopTimer();
    setGameState(newState);
    startTimer(plotsData, participantsData, timerDurationSeconds);
  }

  private void setGameState(GameStateManager.GameState newState) {
    this.currState = newState;
  }

  private void startTimer(HashMap<Location, BuildingPlot> plotsData, HashMap<Player, PlayerData> participantsData,
                          Integer timerDurationSeconds) {
    clearOldTimer();
    this.plotsData = plotsData;
    this.participantsData = participantsData;
    if (timerDurationSeconds != null) {
      timerBar = Util.createBossBarTimer(timerDurationSeconds);
      currTime = timerDurationSeconds;
      ticksLeftInSecond = 20;
      Util.addPlayersToBossBar(vars.getParticipants(), timerBar);
      runTimer(timerDurationSeconds);
    } else {
      runTimer(0);
    }
  }

  public void clearOldTimer() {
    if (timerBar == null) return;
    timerBar.removeAll();
  }

  private void stopTimer() {
    scheduler.cancelTasks(Main.main);
  }

  private void runTimer(int timerMaxTime) {
    scheduler.scheduleSyncDelayedTask(Main.main, () -> {
      for (Player p : vars.getParticipants()) {
        String actionBarMsg;
        if (participantsData.containsKey(p)) {
          Location playerLoc = participantsData.get(p).getLocation();
          BuildingPlot plotData = plotsData.get(playerLoc);
          actionBarMsg = GameStateManager.getActionBarMsgForState(currState, plotData);
        } else {
          actionBarMsg = GameStateManager.getActionBarMsgForState(currState, null);
        }
        Util.sendActionBarMessage(p, actionBarMsg);
      }

      if (ticksLeftInSecond == 0) {
        tickSecond(timerMaxTime);
        ticksLeftInSecond = 20;
      }

      ticksLeftInSecond--;
      runTimer(timerMaxTime);
    }, 1L);
  }

  private void tickSecond(int timerMaxTime) {
    if (currTime > 0) { currTime -= 1; }
    if (timerBar != null) { Util.updateBossBarTimer(timerBar, currTime, timerMaxTime); }
  }

  public void printNewBuildTextWithDelay(int currPhase, int totalPhases, String builderPrompt, String builder,
                                         String prompter, String guesser, String guess, List<Player> participants) {
    messageDelay = 5;
    ticksLeftInSecond = 20;
    stopTimer();
    clearOldTimer();

    Util.sendCustomJsonMessage(participants, JsonStrings.generateNewBuildRoundText(builderPrompt, prompter));

    tickNewBuildText(currPhase, totalPhases, builderPrompt, builder, prompter, guesser, guess, participants);
  }

  private void tickNewBuildText(int currPhase, int totalPhases, String builderPrompt, String builder,
                                String prompter, String guesser, String guess, List<Player> participants) {
    scheduler.scheduleSyncDelayedTask(Main.main, () -> {
      if (ticksLeftInSecond <= 0) {
        messageDelay--;
        ticksLeftInSecond = 20;
      }

      if (messageDelay == 0) {
        Util.sendCustomJsonMessage(participants,JsonStrings.generateViewingPhaseText(
                currPhase, totalPhases, builderPrompt, builder, prompter, guesser, guess
        ));
        messageDelay = -1;
      }

      ticksLeftInSecond--;
      tickNewBuildText(currPhase, totalPhases, builderPrompt, builder, prompter, guesser, guess, participants);
    }, 1L);
  }
}
