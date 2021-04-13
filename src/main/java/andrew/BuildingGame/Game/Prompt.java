package andrew.BuildingGame.Game;

import org.bukkit.entity.Player;

public class Prompt {
  String promptString;
  Player promptGiver;

  public Prompt(Player promptGiver, String promptString) {
    this.promptString = promptString;
    this.promptGiver = promptGiver;
  }

  public String getPromptString() {
    return promptString;
  }

  public Player getPromptGiver() {
    return promptGiver;
  }
}
