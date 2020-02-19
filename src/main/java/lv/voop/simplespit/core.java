package lv.voop.simplespit;

import lv.voop.simplespit.mcstats.Metrics;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class core extends JavaPlugin {
    File configFile;
    FileConfiguration config;
    int test = 1;
    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
    public static Plugin plugin;
    public String text_italic = ChatColor.ITALIC + "";
    @Override
    public void onEnable() {
        int pluginId = 6477;
        Metrics metrics = new Metrics(this, pluginId);
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
                                int cooldownTime = getConfig().getInt("cooldown");
                                if (cooldowns.containsKey(p.getName())) {
                                    long secondsLeft = ((cooldowns.get(p.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                                    if(secondsLeft>0) {
                                        if (secondsLeft <= 1) {
                                            p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Have To Wait " + ChatColor.YELLOW + secondsLeft + ChatColor.RED + " Second before running this command!");
                                            return true;
                                        } else if (secondsLeft >= 1) {
                                            p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Have To Wait " + ChatColor.YELLOW + secondsLeft + ChatColor.RED + " Seconds before running this command!");
                                            return true;
                                        }
                                    }
                                }
                                cooldowns.put(sender.getName(), System.currentTimeMillis());
                                ShootSpit(p);
                                return true;
                            }
                        } else {
                            int cooldownTime = getConfig().getInt("cooldown");
                            if (cooldowns.containsKey(p.getName())) {
                                long secondsLeft = ((cooldowns.get(p.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                                if(secondsLeft>0) {
                                    if (secondsLeft <= 1) {
                                        p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Have To Wait " + ChatColor.YELLOW + secondsLeft + ChatColor.RED + " Second before running this command!");
                                        return true;
                                    } else if (secondsLeft >= 1) {
                                        p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Have To Wait " + ChatColor.YELLOW + secondsLeft + ChatColor.RED + " Seconds before running this command!");
                                        return true;
                                    }
                                }
                            }
                            cooldowns.put(p.getName(), System.currentTimeMillis());
                            ShootSpit(p);
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Don't Have Permission To Run This Command!");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("simplespit.reload")) {
                        ConfigReload(p);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You Don't Have Permission To Run This Command!");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("about")) {
                    TextComponent github = new TextComponent( ChatColor.RED + "[" + ChatColor.YELLOW + "GitHub" + ChatColor.RED + "]" );
                    github.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/voop-lv/SimpleSpit"));
                    p.sendMessage(ChatColor.GRAY + text_italic + "SimpleSpit By " + ChatColor.YELLOW + text_italic + "Commissar_Voop");
                    p.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.YELLOW + "1.5");
                    p.spigot().sendMessage(github);
                    return true;
                }
            }
        } else {
            if (cmd.getName().equalsIgnoreCase("spit")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Only Players Can Run This Command");
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    ConfigCheck();
                    return true;
                } else if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage(ChatColor.GRAY + text_italic + "SimpleSpit By " + ChatColor.YELLOW + text_italic + "Commissar_Voop");
                    sender.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.YELLOW + "1.0");
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spit")) {
            List<String> type = new ArrayList<String>();
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("")){
                    type.add("about");
                    if (sender.hasPermission("simplespit.reload")) {
                        type.add("reload");
                    }
                    return type;
                } else if ((args[0].equalsIgnoreCase("a")) || (args[0].equalsIgnoreCase("ab")) || (args[0].equalsIgnoreCase("abo")) || (args[0].equalsIgnoreCase("abou")) || (args[0].equalsIgnoreCase("about"))) {
                    type.add("about");
                    return type;
                } else if ((args[0].equalsIgnoreCase("r")) || (args[0].equalsIgnoreCase("re")) || (args[0].equalsIgnoreCase("rel")) || (args[0].equalsIgnoreCase("relo")) || (args[0].equalsIgnoreCase("reloa")) || (args[0].equalsIgnoreCase("reload"))) {
                    if (sender.hasPermission("simplespit.reload")) {
                        type.add("reload");
                        return type;
                    }
                } else {
                    type.add("about");
                    if (sender.hasPermission("simplespit.reload")) {
                        type.add("reload");
                    }
                    return type;
                }
            }
            if (args.length >= 2) {
                type.add("");
                return type;
            }
        }
        return null;
    }
}
