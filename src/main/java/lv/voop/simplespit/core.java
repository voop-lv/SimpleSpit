package lv.voop.simplespit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class core extends JavaPlugin {
    File configFile;
    FileConfiguration config;
    int test = 1;
    public static Plugin plugin;
    public String text_italic = ChatColor.ITALIC + "";
    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = new YamlConfiguration();
    }

    @Override
    public void onDisable() {
    }
    private void ConfigReload(CommandSender s) {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            ConfigCopy(getResource("config.yml"), configFile);
            s.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.GRAY + text_italic + "config.yml" + ChatColor.WHITE + " was not found 1 file copied!");
            System.out.println(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.GRAY + text_italic + "config.yml" + ChatColor.WHITE + " was not found 1 file copied!");
        } else {
            reloadConfig();
            s.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.WHITE + "Config Loaded!");;
            System.out.println(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.WHITE + "Config Loaded!");
        }
    }
    private void ConfigCheck(){
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            ConfigCopy(getResource("config.yml"), configFile);
            System.out.println(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.GRAY + text_italic + "config.yml" + ChatColor.WHITE + " was not found 1 file copied!");
        } else {
            reloadConfig();
            System.out.println(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SimpleSpit" + ChatColor.GOLD + "] " + ChatColor.WHITE + "Config Loaded!");
        }
    }
    private void ConfigCopy(InputStream in , File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ShootSpit(Player p) {
        if (this.getConfig().getBoolean("simplespit") == true) {
            Vector dir = p.getLocation().getDirection();
            Vector vec = new Vector(dir.getX() * 0.3, dir.getY() * 0.2, dir.getZ() * 0.3);
            p.launchProjectile(LlamaSpit.class, vec);
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "This Command Is Disabled In The Config File!");
        }
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("spit")) {
                Player p = (Player)sender;
                if (args.length == 0) {
                    if (p.hasPermission("simplespit.use")) {
                        if (this.getConfig().getBoolean("disable-for_specmode") == true) {
                            if (p.getGameMode() == GameMode.SPECTATOR) {
                                sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Can't Run This Command While In Spectator Mode!");
                                return true;
                            } else {
                                //Logic
                                ShootSpit(p);
                                return true;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Don't Have Permission To Run This Command!");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("simplespit.reload")) {
                        //Config Reload Logic
                        ConfigReload(p);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Don't Have Permission To Run This Command!");
                        return true;
                    }
                }
            }
        } else {
            if (cmd.getName().equalsIgnoreCase("spit")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Only Players Can Run This Command");
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    //Config Reload Logic
                    ConfigCheck();
                    return true;
                }
            }
            return true;
        }
        return false;
    }
}
