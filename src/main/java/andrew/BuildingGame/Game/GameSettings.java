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

  private boolean buildOwnFirstPrompt;

  public GameSettings(int buildTimeSeconds, int buildAreaWidth, int buildAreaLength, int buildAreaHeight,
                           int buildAreaPadding, Material baseBlockMaterial, Material paddingBlockMaterial,
                           Material wallsBlockMaterial, boolean buildOwnFirstPrompt) {
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
    this.buildOwnFirstPrompt = buildOwnFirstPrompt;
  }

  public int getBuildTimeSeconds() { return buildTimeSeconds; }
  public int getBuildAreaWidth() { return buildAreaWidth; }
  public int getBuildAreaLength() {  return buildAreaLength; }
  public int getBuildAreaHeight() { return buildAreaHeight; }
  public int getBuildAreaPadding() { return buildAreaPadding; }
  public int getBuildAreaXOffset() { return buildAreaXOffset; }
  public int getBuildAreaZOffset() { return buildAreaZOffset; }
  public boolean getBuildOwnFirstPrompt() { return buildOwnFirstPrompt; }

  public void setBuildTimeSeconds(int buildTimeSeconds) { this.buildTimeSeconds = buildTimeSeconds; }
  public void setBuildAreaWidth(int buildAreaWidth) { this.buildAreaWidth = buildAreaWidth; }
  public void setBuildAreaLength(int buildAreaLength) { this.buildAreaLength = buildAreaLength; }
  public void setBuildAreaHeight(int buildAreaHeight) { this.buildAreaHeight = buildAreaHeight; }
  public void setBuildAreaPadding(int buildAreaPadding) { this.buildAreaPadding = buildAreaPadding; }
  public void setBuildAreaXOffset(int buildAreaXOffset) { this.buildAreaXOffset = buildAreaXOffset; }
  public void setBuildAreaZOffset(int buildAreaZOffset) { this.buildAreaZOffset = buildAreaZOffset; }
  public void setBaseBlockMaterial(Material baseBlockMaterial) { this.baseBlockMaterial = baseBlockMaterial; }
  public void setPaddingBlockMaterial(Material paddingBlockMaterial) { this.paddingBlockMaterial = paddingBlockMaterial; }
  public void setWallsBlockMaterial(Material wallsBlockMaterial) { this.wallsBlockMaterial = wallsBlockMaterial; }
  public void setBuildOwnFirstPrompt(boolean buildOwnFirstPrompt) { this.buildOwnFirstPrompt = buildOwnFirstPrompt; }

  public Material getBaseBlockMaterial() { return baseBlockMaterial; }
  public Material getPaddingBlockMaterial() { return paddingBlockMaterial; }
  public Material getWallsBlockMaterial() { return wallsBlockMaterial; }
}
