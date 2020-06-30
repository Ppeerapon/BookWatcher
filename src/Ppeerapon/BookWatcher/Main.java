package Ppeerapon.BookWatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BookWatcher is enable");
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("bookwatcher").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BookWatcher is disable");
    }

    ArrayList<UUID> BookWatcher = new ArrayList<>();

    @EventHandler
    private void Book(PlayerEditBookEvent e){
        Player p = e.getPlayer();
        String location = "World: " + p.getWorld().getName() + " X: " + p.getLocation().getBlockX() + " Y: "+ p.getLocation().getBlockY() + " Z: " + p.getLocation().getBlockZ();
        for (Player onlineplayer : Bukkit.getOnlinePlayers()){
            if (BookWatcher.contains(onlineplayer.getUniqueId())){
                if (onlineplayer != p) {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                    BookMeta bookMeta = e.getNewBookMeta().clone();
                    if (!bookMeta.hasTitle()) {
                        bookMeta.setTitle(ChatColor.RED + "Blank");
                    }
                    if (!bookMeta.hasAuthor()) {
                        bookMeta.setAuthor(p.getName());
                    }
                    book.setItemMeta(bookMeta);
                    onlineplayer.getInventory().addItem(book);
                    onlineplayer.sendMessage(ChatColor.AQUA + p.getName() + " has edit book at " + location);
                }
            }
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e){
        Player p = e.getPlayer();
        BookWatcher.remove(p.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("bookwatcher")) {
            if (p.hasPermission("BookWatcher.Use")) {
                UUID uuid = p.getUniqueId();
                if (!BookWatcher.contains(uuid)) {
                    BookWatcher.add(uuid);
                    p.sendMessage(ChatColor.GREEN + "[BW] BookWatcher is enable");
                } else {
                    BookWatcher.remove(uuid);
                    p.sendMessage(ChatColor.RED + "[BW] BookWatcher is disable");
                }
            } else {
                p.sendMessage("You don't have permission");
            }
        }
        return false;
    }

}
