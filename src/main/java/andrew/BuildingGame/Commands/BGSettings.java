package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.GameSettings;
import andrew.BuildingGame.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;

import java.util.*;

public class BGSettings implements TabExecutor {
  private final Set<String> attributesSet = new HashSet<>(Arrays.asList(
          "buildTime", "buildAreaWidth", "buildAreaLength", "buildAreaHeight", "buildAreaPadding", "buildFirstPrompt"));

  public BGSettings(Main plugin) {
    plugin.getCommand("bgsettings").setExecutor((CommandExecutor) this);
    plugin.getCommand("bgsettings").setTabCompleter((TabCompleter) this);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission(("gs.settings"))) {
      sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
      return true;
    }

    GameSettings settings = Main.getSettings();

    if (args.length < 2 || args.length > 3) { return incorrectUsage(sender); }

    String type = args[0];
    String attr = args[1];
    if (!validateAttribute(sender, attr)) { return true; }

    if (args.length == 2) {
      if (!type.equals("get")) { return incorrectUsage(sender); }
      getSetting(settings, sender, attr);
      return true;
    } else {
      String valString = args[2];
      if (!type.equals("set")) { return incorrectUsage(sender); }
      try {
        int val = Integer.parseInt(valString);
        setSetting(settings, sender, attr, val);
        return true;
      } catch (NumberFormatException e) {
        sender.sendMessage(ChatColor.RED + "Provided value is not an integer!");
        return true;
      }
    }
  }

  private boolean incorrectUsage(CommandSender sender) {
    String incorrectUsage = ChatColor.RED + "Usage: /bgsettings <set,get>";
    sender.sendMessage(incorrectUsage);
    return true;
  }

  private boolean validateAttribute(CommandSender sender, String attr) {
    if (!attributesSet.contains(attr)) {
      sender.sendMessage(ChatColor.RED + "Invalid attribute provided.");
      return false;
    } else { return true; }
  }

  private void setSetting(GameSettings settings, CommandSender sender, String attr, int val) {
    int initialVal = getSettingValue(settings, attr);
    switch(attr) {
      case "buildTime" -> { settings.setBuildTimeSeconds(val); }
      case "buildAreaWidth" -> { settings.setBuildAreaWidth(val); }
      case "buildAreaLength" -> { settings.setBuildAreaLength(val); }
      case "buildAreaHeight" -> { settings.setBuildAreaHeight(val); }
      case "buildAreaPadding" -> { settings.setBuildAreaPadding(val); }
      case "buildFirstPrompt" -> { settings.setBuildOwnFirstPrompt(val == 1); }
    }

    sender.sendMessage(ChatColor.GREEN + attr + ": " + initialVal + " -> " + val);
  }

  private int getSettingValue(GameSettings settings, String attr) {
    int val = 0;
    switch(attr) {
      case "buildTime" -> { val = settings.getBuildTimeSeconds(); }
      case "buildAreaWidth" -> { val = settings.getBuildAreaWidth(); }
      case "buildAreaLength" -> { val = settings.getBuildAreaLength(); }
      case "buildAreaHeight" -> { val = settings.getBuildAreaHeight(); }
      case "buildAreaPadding" -> { val = settings.getBuildAreaPadding(); }
      case "buildFirstPrompt" -> { val = settings.getBuildOwnFirstPrompt() ? 1 : 0; }
    }
    return val;
  }

  private void getSetting(GameSettings settings, CommandSender sender, String attr) {
    sender.sendMessage(ChatColor.GREEN + attr + " current value: " + getSettingValue(settings, attr));
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    List<String> validParams = new ArrayList<>();
    if (args.length == 1) {
      validParams.add("get");
      validParams.add("set");
    } else if (args.length == 2) {
      validParams.addAll(attributesSet);
    } else if (args.length == 3 && args[0].equals("set")) {
      switch (args[1]) {
        case "buildTime" -> {
          validParams.add("60");
          validParams.add("180");
          validParams.add("300");
          validParams.add("600");
        }
        case "buildAreaWidth", "buildAreaLength" -> {
          validParams.add("10");
          validParams.add("15");
          validParams.add("20");
          validParams.add("25");
        }
        case "buildAreaHeight" -> {
          validParams.add("15");
          validParams.add("20");
          validParams.add("30");
          validParams.add("50");
        }
        case "buildAreaPadding" -> {
          validParams.add("3");
          validParams.add("5");
          validParams.add("7");
        }
        case "buildFirstPrompt" -> {
          validParams.add("0");
          validParams.add("1");
        }
      }
    }
    return validParams;
  }
}

