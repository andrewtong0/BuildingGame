package andrew.BuildingGame.Game;

public class JsonStrings {
    public static String generateNextPromptRoundTitle(String promptName) {
        return "[{\"text\":\"\"},{\"text\":\"\\u2582\",\"bold\":true,\"color\":\"#9b9587\"},{\"text\":\"\\u2583\"," +
                "\"bold\":true,\"color\":\"#0000b2\"},{\"text\":\"\\u2585\",\"bold\":true,\"color\":\"#0000d8\"}," +
                "{\"text\":\"\\u2586 " + promptName + " \\u2586\",\"bold\":true,\"color\":\"#0000fc\"},{\"text\":\"" +
                "\\u2585\",\"bold\":true,\"color\":\"#0000d8\"},{\"text\":\"\\u2583\",\"bold\":true,\"color\":\"" +
                "#0000b2\"},{\"text\":\"\\u2582\",\"bold\":true,\"color\":\"#00008c\"}]";
    }
}
