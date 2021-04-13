package andrew.BuildingGame.Game;

import andrew.BuildingGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

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

  public Timer(GameSettings settings, GameVars vars) {
    this.settings = settings;
    this.vars = vars;
    this.scheduler = Bukkit.getServer().getScheduler();
    timerBar = Util.createBossBarTimer(settings.getBuildTimeSeconds());
    Util.addPlayersToBossBar(vars.getParticipants(), timerBar);
    this.currState = GameStateManager.GameState.INIT;
  }

  public void startNextTimer(HashMap<Location, BuildingPlot> plotsData, HashMap<Player, PlayerData> participantsData,
                             GameStateManager.GameState newState) {
    stopTimer();
    setGameState(newState);
    startTimer(plotsData, participantsData);
  }

  private void setGameState(GameStateManager.GameState newState) {
    this.currState = newState;
  }

  private void startTimer(HashMap<Location, BuildingPlot> plotsData, HashMap<Player, PlayerData> participantsData) {
    clearOldTimer();
    this.plotsData = plotsData;
    this.participantsData = participantsData;
    currTime = settings.getBuildTimeSeconds();
    ticksLeftInSecond = 20;
    runTimer();
  }

  private void clearOldTimer() {
    if (timerBar == null) return;
    timerBar.removeAll();
  }

  private void stopTimer() {
    scheduler.cancelTasks(Main.main);
  }

  private void runTimer() {
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
        tickSecond();
        ticksLeftInSecond = 20;
      }

      ticksLeftInSecond--;
      runTimer();
    }, 1L);
  }

  private void tickSecond() {
    if (currTime > 0) { currTime -= 1; }
    if (timerBar != null) { Util.updateBossBarTimer(timerBar, currTime, settings.getBuildTimeSeconds()); }
  }
}
