package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BuildingPlot {
  Location plotLocation;
  Player builder;
  Prompt givenPrompt;
  Prompt guessedPrompt;

  public BuildingPlot(Location plotLocation) {
    this.plotLocation = plotLocation;
  }

  public void setBuilder(Player builder) {
    this.builder = builder;
  }

  public void setGivenPrompt(Prompt givenPrompt) {
    this.givenPrompt = givenPrompt;
  }

  public void setGuessedPrompt(Prompt guessedPrompt) {
    this.guessedPrompt = guessedPrompt;
  }

  public Location getPlotLocation() {
    return plotLocation;
  }

  public Player getBuilder() {
    return builder;
  }

  public Prompt getGivenPrompt() {
    return givenPrompt;
  }

  public Prompt getGuessedPrompt() {
    return guessedPrompt;
  }
}
