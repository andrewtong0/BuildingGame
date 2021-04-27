package andrew.BuildingGame.Game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;

public class TeamManager {
  List<Player> participants;
  ScoreboardManager manager;
  Scoreboard board;

  public TeamManager(List<Player> participants) {
    this.participants = participants;

    manager = Bukkit.getScoreboardManager();
    assert manager != null;
    board = manager.getMainScoreboard();
  }

  public void clearPlayerTeams() {
    for (Team team : board.getTeams()) { team.unregister(); }
  }

  private void createPlayerTeams(List<Player> participants) {
    for (Player p : participants) {
      Team team = board.registerNewTeam(Integer.toString(p.getEntityId()));
      team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
      team.addEntry(p.getName());
    }
  }

  public void resetPlayerTeams() {
    clearPlayerTeams();
    createPlayerTeams(participants);
  }
}
