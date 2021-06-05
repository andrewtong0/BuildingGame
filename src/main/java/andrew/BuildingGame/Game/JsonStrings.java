package andrew.BuildingGame.Game;

public class JsonStrings {
    public static String generateNextPromptRoundTitle(String promptName) {
        return "[{\"text\":\"\"},{\"text\":\"\\u2582\",\"bold\":true,\"color\":\"#9b9587\"},{\"text\":\"\\u2583\"," +
                "\"bold\":true,\"color\":\"#0000b2\"},{\"text\":\"\\u2585\",\"bold\":true,\"color\":\"#0000d8\"}," +
                "{\"text\":\"\\u2586 " + promptName + " \\u2586\",\"bold\":true,\"color\":\"#0000fc\"},{\"text\":\"" +
                "\\u2585\",\"bold\":true,\"color\":\"#0000d8\"},{\"text\":\"\\u2583\",\"bold\":true,\"color\":\"" +
                "#0000b2\"},{\"text\":\"\\u2582\",\"bold\":true,\"color\":\"#00008c\"}]";
    }

    public static String generateIntroText() {
        return "[\"\",{\"text\":\"\\n\"},{\"text\":\"\\u2583\",\"color\":\"#008F00\"},{\"text\":\"\\u2585\",\"color" +
                "\":\"#398E0E\"},{\"text\":\"\\u2585\",\"color\":\"#738E1D\"},{\"text\":\"\\u2586\",\"color\":" +
                "\"#AC8E2C\"},{\"text\":\"\\u2586\",\"color\":\"#CAA824\"},{\"text\":\"\\u2587\",\"color\":" +
                "\"#DFCA16\"},{\"text\":\"\\u2587\",\"color\":\"#F4ED07\"},{\"text\":\" Minecraft Building Game" +
                " \",\"bold\":true,\"color\":\"yellow\"},{\"text\":\"\\u2587\",\"color\":\"#F2EC07\"},{\"text\":" +
                "\"\\u2587\",\"color\":\"#DAC615\"},{\"text\":\"\\u2586\",\"color\":\"#C1A123\"},{\"text\":\"\\u2586" +
                "\",\"color\":\"#A1852A\"},{\"text\":\"\\u2585\",\"color\":\"#6C891C\"},{\"text\":\"\\u2585\"," +
                "\"color\":\"#368C0E\"},{\"text\":\"\\u2583\",\"color\":\"#019001\"},{\"text\":\"\\n\"}," +
                "{\"text\":\"                      Coded by \"},{\"text\":\"***REMOVED***\",\"color\":\"green\"}," +
                "{\"text\":\"\\n                     Design by \"},{\"text\":\"***REMOVED***\",\"color\":\"red\"}," +
                "{\"text\":\"\\n \"}]";
    }

    public static String generateInitialPromptText() {
        return "[\"\",{\"text\":\"Start by entering a building prompt with\",\"color\":\"gold\"}," +
                "{\"text\":\" /bgprompt \",\"color\":\"#80F894\"}]";
    }

    public static String generateGuessText() {
        return "[\"\",{\"text\":\"Guess what this build is! \",\"color\":\"gold\"},{\"text\":\"Use \"}," +
                "{\"text\":\"/bgprompt\",\"color\":\"#80F894\"},{\"text\":\" to guess! \\u0020\"}]";
    }

    public static String generateAddedPromptText(String givenPrompt) {
        return "[\"\",{\"text\":\"Prompt added:\",\"color\":\"gold\"},{\"text\":\" " + givenPrompt + "\"}]";
    }

    public static String generateUpdatedPromptText(String givenPrompt) {
        return "[\"\",{\"text\":\"Prompt updated:\",\"color\":\"gold\"},{\"text\":\" " + givenPrompt + "\"}]";
    }

    public static String generateBuildingPhaseCompleteText() {
        return "[\"\",{\"text\":\"\\n\"},{\"text\":\"\\u2731 \\u2731 \",\"color\":\"#F4FE3F\"},{\"text\":\"B\"," +
                "\"color\":\"#F4FD3F\"},{\"text\":\"ui\",\"color\":\"#F5FE3F\"},{\"text\":\"ld\",\"color\":\"" +
                "#F6FE3F\"},{\"text\":\"i\",\"color\":\"#F6FE3E\"},{\"text\":\"ng \",\"color\":\"#F7FE3F\"}," +
                "{\"text\":\"p\",\"color\":\"#F8FE3E\"},{\"text\":\"h\",\"color\":\"#F8FE3F\"},{\"text\":\"ase \"," +
                "\"color\":\"#F9FE3F\"},{\"text\":\"co\",\"color\":\"#FAFE3F\"},{\"text\":\"mpl\",\"color\":\"" +
                "#FBFE3F\"},{\"text\":\"et\",\"color\":\"#FCFE3F\"},{\"text\":\"e \\u2731 \",\"color\":\"#FDFE3F\"}," +
                "{\"text\":\"\\u2731 \",\"color\":\"#FEFE3F\"},{\"text\":\"\\n\\n\\n\"},{\"text\":\"\\u2756 \\" +
                "u2756 T\",\"color\":\"#F4FE3F\"},{\"text\":\"i\",\"color\":\"#F5FE3F\"},{\"text\":\"m\",\"color\":\"" +
                "#F5FE3E\"},{\"text\":\"e to \",\"color\":\"#F6FE3F\"},{\"text\":\"r\",\"color\":\"#F7FD3F\"}," +
                "{\"text\":\"e\",\"color\":\"#F7FE3F\"},{\"text\":\"vie\",\"color\":\"#F8FE3F\"},{\"text\":\"w t\"," +
                "\"color\":\"#F9FE3F\"},{\"text\":\"he b\",\"color\":\"#FAFE3F\"},{\"text\":\"ui\",\"color\":\"" +
                "#FBFE3F\"},{\"text\":\"lds\",\"color\":\"#FCFE3F\"},{\"text\":\"! \\u2756 \",\"color\":\"#FDFE3F\"}," +
                "{\"text\":\"\\u2756\",\"color\":\"#FEFE3F\"},{\"text\":\"\\n \"}]";
    }

    public static String generateNewBuildRoundText(String roundPrompt, String ideaUser) {
        return "[\"\",{\"text\":\"\\u2584\",\"color\":\"#FE3F3F\"},{\"text\":\"\\u2584\",\"color\":\"#FE4D3F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE5C3F\"},{\"text\":\"\\u2584\",\"color\":\"#FD6B3F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE793F\"},{\"text\":\"\\u2584\",\"color\":\"#FE883F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE973F\"},{\"text\":\"\\u2584\",\"color\":\"#FEA53F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEB43F\"},{\"text\":\"\\u2584\",\"color\":\"#FEC33F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FED13F\"},{\"text\":\"\\u2584\",\"color\":\"#FEE03F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEEF3F\"},{\"text\":\"\\u2584\",\"color\":\"#FEFE3F\"}," +
                "{\"text\":\" \"},{\"text\":\"NEW ROUND\",\"underlined\":true,\"color\":\"green\"}," +
                "{\"text\":\" \\u2584\",\"color\":\"#FEFE3F\"},{\"text\":\"\\u2584\",\"color\":\"#FEEF3F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEE03F\"},{\"text\":\"\\u2584\",\"color\":\"#FDD13F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEC33F\"},{\"text\":\"\\u2584\",\"color\":\"#FEB43F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEA53F\"},{\"text\":\"\\u2584\",\"color\":\"#FE973F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE883F\"},{\"text\":\"\\u2584\",\"color\":\"#FE793F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE6B3F\"},{\"text\":\"\\u2584\",\"color\":\"#FE5C3F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE4D3F\"},{\"text\":\"\\u2584\",\"color\":\"#FE3F3F\"}," +
                "{\"text\":\"\\n\\nNew round's prompt: \"},{\"text\":\"" + roundPrompt + "\",\"color\":\"aqua\"}," +
                "{\"text\":\"\\n\\nIdea by: \"},{\"text\":\"" + ideaUser + "\",\"color\":\"#4BB7F8\"}," +
                "{\"text\":\"\\n\"},{\"text\":\"\\u2584\",\"color\":\"#FE3F3F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE4F3F\"},{\"text\":\"\\u2584\",\"color\":\"#FE603F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE713F\"},{\"text\":\"\\u2584\",\"color\":\"#FE823F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FE933F\"},{\"text\":\"\\u2584\",\"color\":\"#FEA43F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEB43F\"},{\"text\":\"\\u2584\",\"color\":\"#FEC53F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FED63F\"},{\"text\":\"\\u2584\",\"color\":\"#FEE73F\"}," +
                "{\"text\":\"\\u2584\",\"color\":\"#FEF83F\"},{\"text\":\"\\u2584\\u2584\\u2584\\u2584\\u2584" +
                "\\u2584\\u2584\\u2584\\u2584\\u2584\\u2584\",\"color\":\"#FEFE3F\"},{\"text\":\"\\u2584\"," +
                "\"color\":\"#FEF83F\"},{\"text\":\"\\u2584\",\"color\":\"#FEE73F\"},{\"text\":\"\\u2584\",\"color\":" +
                "\"#FED63F\"},{\"text\":\"\\u2584\",\"color\":\"#FEC53F\"},{\"text\":\"\\u2584\",\"color\":" +
                "\"#FEB43F\"},{\"text\":\"\\u2584\",\"color\":\"#FEA43F\"},{\"text\":\"\\u2584\",\"color\":" +
                "\"#FE933F\"},{\"text\":\"\\u2584\",\"color\":\"#FE823F\"},{\"text\":\"\\u2584\",\"color\":" +
                "\"#FE713F\"},{\"text\":\"\\u2584\",\"color\":\"#FE603F\"},{\"text\":\"\\u2584\",\"color\":" +
                "\"#FE4F3F\"},{\"text\":\"\\u2584\",\"color\":\"#FE3F3F\"},{\"text\":\"\\n\\n \"}]";
    }

    public static String generateViewingPhaseText(int currPhase, int totalPhases, String builderPrompt, String builder,
                                                  String prompter, String guesser, String guess) {
        String currPhaseString = Integer.toString(currPhase);
        String totalPhaseString = Integer.toString(totalPhases);

        return "[\"\",{\"text\":\"                                                               \"," +
                "\"bold\":true,\"strikethrough\":true},{\"text\":\"\\n\",\"bold\":true}," +
                "{\"text\":\"          \\u300a Viewing Phase: Build " + currPhaseString + " out of " +
                totalPhaseString + " \\u300b\",\"bold\":true,\"color\":" + "\"#5DF36A\"},{\"text\":\"\\n\\n\"}," +
                "{\"text\":\"" + builderPrompt + "\",\"color\":\"yellow\"}," + "{\"text\":\" built by \"}," +
                "{\"text\":\"" + builder + "\",\"color\":\"#4BB7F8\"},{\"text\":\"\\n\\n" + "Prompt given by \"}," +
                "{\"text\":\"" + prompter + "\",\"color\":\"#F87262\"},{\"text\":\"\\n\\n\"}," +
                "{\"text\":\"" + guesser + "\",\"color\":\"gray\"},{\"text\":\" guessed: \",\"color\":\"white\"}," +
                "{\"text\":\"" + guess + "\",\"color\":\"#FFD269\"},{\"text\":\"\\n\"},{\"text\":" +
                "\"                                                               \\n\",\"bold\":true," +
                "\"strikethrough\":true}]\n";
    }

    public static String generateFinalBuildingRoundText() {
        return "[{\"text\":\"\"},{\"text\":\"Fin\",\"bold\":true,\"color\":\"yellow\"}," + 
        "{\"text\":\"a\",\"bold\":true,\"color\":\"#fefe3e\"},{\"text\":\"l Bu\",\"bold\":true,\"color\":\"yellow\"}," +
        "{\"text\":\"i\",\"bold\":true,\"color\":\"#fdfd3f\"},{\"text\":\"lding Round! \u270e\",\"bold\":true,\"color\":\"yellow\"}]";  
    }
}
