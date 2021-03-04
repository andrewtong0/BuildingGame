package andrew.BuildingGame.Game;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class GameSettings {
  private int buildTimeMinutes;
  private int buildAreaWidth;
  private int buildAreaLength;
  private int buildAreaHeight;
  private int buildAreaPadding;
  private Material baseBlockMaterial;
  private Material paddingBlockMaterial;
  private Material wallsBlockMaterial;

  public GameSettings(int buildTimeMinutes, int buildAreaWidth, int buildAreaLength, int buildAreaHeight,
                           int buildAreaPadding, Material baseBlockMaterial, Material paddingBlockMaterial,
                           Material wallsBlockMaterial) {
    this.buildTimeMinutes = buildTimeMinutes;
    this.buildAreaWidth = buildAreaWidth;
    this.buildAreaLength = buildAreaLength;
    this.buildAreaHeight = buildAreaHeight;
    this.buildAreaPadding = buildAreaPadding;

    this.baseBlockMaterial = baseBlockMaterial;
    this.paddingBlockMaterial = paddingBlockMaterial;
    this.wallsBlockMaterial = wallsBlockMaterial;
  }

  public int getBuildTimeMinutes() { return buildTimeMinutes; }
  public int getBuildAreaWidth() { return buildAreaWidth; }
  public int getBuildAreaLength() {  return buildAreaLength; }
  public int getBuildAreaHeight() { return buildAreaHeight; }
  public int getBuildAreaPadding() { return buildAreaPadding; }

  public Material getBaseBlockMaterial() { return baseBlockMaterial; }
  public Material getPaddingBlockMaterial() { return paddingBlockMaterial; }
  public Material getWallsBlockMaterial() { return wallsBlockMaterial; }
}
