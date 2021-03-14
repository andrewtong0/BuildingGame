package andrew.BuildingGame.Game.Plot;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationPrompts {
  private String prompt;
  private String guess;
  private Location location;
  private Player prompter;
  private Player guesser;
  private Player builder;

  public LocationPrompts(Location location) {
    this.location = location;
  }

  public String getPrompt() {
    return prompt;
  }

  public String getGuess() {
    return guess;
  }

  public Player getPrompter() {
    return prompter;
  }

  public Player getGuesser() {
    return guesser;
  }

  public Player getBuilder() {
    return builder;
  }

  public void setBuilder(Player builder) {
    this.builder = builder;
  }

  public Location getLocation() {
    return location;
  }

  public void setPromptOrGuess(String entry, Player p) {
    if (prompt == null) {
      prompt = entry;
      prompter = p;
    } else if (guess == null) {
      guess = entry;
      guesser = p;
    }
  }
}
