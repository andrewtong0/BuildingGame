package andrew.BuildingGame.Game;

import org.bukkit.ChatColor;

public class GameStateManager {
  public enum GameState {
    INIT,
    INITPROMPT,
    BUILD,
    GUESS,
    ENDGAME
  }

  public static GameState getNextState(GameState currState, boolean finalRound) {
    return switch (currState) {
      case INIT -> GameState.INITPROMPT;
      case INITPROMPT -> GameState.BUILD;
      case BUILD -> GameState.GUESS;
      case GUESS -> finalRound ? GameState.ENDGAME : GameState.BUILD;
      default -> null;
    };
  }

  public static String getActionBarMsgForState(GameState currState, BuildingPlot bp) {
    return switch (currState) {
      case INIT -> "";
      case INITPROMPT -> "";
      case BUILD -> {
        assert bp != null;
        yield ChatColor.GOLD + "" + ChatColor.BOLD + "Your prompt is: " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD +
                "" + bp.getGivenPrompt().getPromptString();
      }
      case GUESS -> "";
      default -> "";
    };
  }
}
