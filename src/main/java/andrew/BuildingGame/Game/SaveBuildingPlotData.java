package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveBuildingPlotData {
    String gameDate;

    public SaveBuildingPlotData() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        gameDate = formatter.format(date);
    }

//    https://www.spigotmc.org/threads/how-to-make-a-written-book.47065/?__cf_chl_jschl_tk__=6ea861508ec237c2c97179138e1d358d64c50f70-1619562890-0-AY421g109g3-wiI7s7ufhun6gMkSxJxOVmX65ZtwpkEUxT8rqCazmIHepYdSi16lN3Bmq1_pgOEB3DXFKqiIkIvV-S3sdrm2K6gxHHURauwJ8KG4hTT81kN9DUgkV_zBrRhv4aZ88teYDcYdsUmIcM8Ns2NZ-0rnMrvAHfuQIOQWvS_TEBlsPIOVcs9HrZxYClGSZeRDI_rzWZvTdgcXB1trQXIYOUzJ5bolPS8buOdIL5aoMGpyC2TysP3AqUl_f-CKnxxk7BK848cBBWWfSYV-1Oiw6l7FAnbEcc51H3C5T-xV13p5sGgm2_WyF8JsO29qE1g4totoK7qAAUuezBHT_Ch7BJLM-TH9Cn7nxYd5U1yN-ihmhV5BsdvuCarn9HNoaOsUR-kHs72kkMfunsGRrs9BOkhI6JAVnJ2HJKMxx9jQPYrd19fqCOPdy7FpB6RRo1kKtL-AwmWU68f6dzw
    public void savePlotData(BuildingPlot bp) {

        Location bpLoc = bp.getPlotLocation();
        Location chestLocation = new Location(
                bpLoc.getWorld(),
                bpLoc.getBlockX() + 1,
                bpLoc.getBlockY(),
                bpLoc.getBlockZ() + 1
        );
        Block block = chestLocation.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) chestLocation.getBlock().getState();
        Inventory inv = chest.getBlockInventory();
        ItemStack book = constructBook(bp);
        inv.addItem(book);
    }

    private ItemStack constructBook(BuildingPlot bp) {
        ItemStack writtenBook = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta bm = (BookMeta) writtenBook.getItemMeta();

        String title = constructTitleString(bp);
        String prompter = constructPrompterString(bp);
        String guesser = constructGuesserString(bp);

        List<String> pages = new ArrayList<>();
        pages.add(gameDate);
        pages.add(title);
        pages.add(prompter);
        pages.add(guesser);

        bm.setTitle("Building Game");
        bm.setPages(pages);
        bm.setAuthor(bp.getBuilder().getName());

        writtenBook.setItemMeta(bm);
        return writtenBook;
    }

    private String constructTitleString(BuildingPlot bp) {
        String buildTitle = bp.getGivenPrompt().getPromptString();
        String builderName = bp.getBuilder().getName();
        return buildTitle + " built by " + builderName;
    }

    private String constructPrompterString(BuildingPlot bp) {
        String prompterName = bp.getGivenPrompt().getPromptGiver().getName();
        return "Prompt given by " + prompterName;
    }

    private String constructGuesserString(BuildingPlot bp) {
        String guesserGuess = bp.getGuessedPrompt().getPromptString();
        String guesserName = bp.getGuessedPrompt().getPromptGiver().getName();
        return guesserGuess + "'s guess: " + guesserName;
    }
}
