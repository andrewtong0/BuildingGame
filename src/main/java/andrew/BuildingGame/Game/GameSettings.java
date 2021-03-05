package andrew.BuildingGame.Game;

import org.bukkit.Material;

public class GameSettings {
  private int buildTimeSeconds;
  private int buildAreaWidth;
  private int buildAreaLength;
  private int buildAreaHeight;
  private int buildAreaPadding;
  private int buildAreaXOffset;
  private int buildAreaZOffset;

  private Material baseBlockMaterial;
  private Material paddingBlockMaterial;
  private Material wallsBlockMaterial;

  public GameSettings(int buildTimeSeconds, int buildAreaWidth, int buildAreaLength, int buildAreaHeight,
                           int buildAreaPadding, Material baseBlockMaterial, Material paddingBlockMaterial,
                           Material wallsBlockMaterial) {
    this.buildTimeSeconds = buildTimeSeconds;
    this.buildAreaWidth = buildAreaWidth;
    this.buildAreaLength = buildAreaLength;
    this.buildAreaHeight = buildAreaHeight;
    this.buildAreaPadding = buildAreaPadding;
    buildAreaXOffset = this.buildAreaPadding * 2 + this.buildAreaWidth + 3;
    buildAreaZOffset = this.buildAreaPadding * 2 + this.buildAreaLength + 3;

    this.baseBlockMaterial = baseBlockMaterial;
    this.paddingBlockMaterial = paddingBlockMaterial;
    this.wallsBlockMaterial = wallsBlockMaterial;
  }

  public int getBuildTimeSeconds() { return buildTimeSeconds; }
  public int getBuildAreaWidth() { return buildAreaWidth; }
  public int getBuildAreaLength() {  return buildAreaLength; }
  public int getBuildAreaHeight() { return buildAreaHeight; }
  public int getBuildAreaPadding() { return buildAreaPadding; }
  public int getBuildAreaXOffset() { return buildAreaXOffset; }
  public int getBuildAreaZOffset() { return buildAreaZOffset; }

  public Material getBaseBlockMaterial() { return baseBlockMaterial; }
  public Material getPaddingBlockMaterial() { return paddingBlockMaterial; }
  public Material getWallsBlockMaterial() { return wallsBlockMaterial; }
}
