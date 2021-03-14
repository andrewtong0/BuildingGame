package andrew.BuildingGame.Game.Plot;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Plot {
  Location cellLocation;

  Player initialPromptGiver;

  String buildingPrompt;
  Player promptBuilder;
  String buildGuess;
  Player buildGuesser;

  Location nextCell;

  public Plot(Location cellLocation, Player promptBuilder) {
    this.cellLocation = cellLocation;
    this.promptBuilder = promptBuilder;
  }

  public Location getCellLocation() { return cellLocation; }
  public String getBuildingPrompt() { return buildingPrompt; }
  public Player getInitialPromptGiver() { return initialPromptGiver; }
  public Player getPromptBuilder() { return promptBuilder; }
  public String getBuildGuess() { return buildGuess; }
  public Player getBuildGuesser() { return buildGuesser; }
  public Location getNextCell() { return nextCell; }

  public void setBuildingPrompt(String buildingPrompt) { this.buildingPrompt = buildingPrompt; }
  public void setInitialPromptGiver(Player initialPromptGiver) { this.initialPromptGiver = initialPromptGiver; }
  public void setPromptBuilder(Player promptBuilder) { this.promptBuilder = promptBuilder; }
  public void setBuildGuess(String buildGuess) { this.buildGuess = buildGuess; }
  public void setBuildGuesser(Player buildGuesser) { this.buildGuesser = buildGuesser; }
  public void setNextCell(Location nextCell) { this.nextCell = nextCell; }
}
