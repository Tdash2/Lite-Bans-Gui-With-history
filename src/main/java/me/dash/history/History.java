package me.dash.history;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.milkbowl.vault.permission.Permission;
import me.dash.history.commands.HistoryCommand;
import me.dash.history.events.InventoryEvent;
import me.dash.history.sql.Database;
import me.dash.history.type.HistoryType;
import me.dash.history.user.HistoryUser;
import me.dash.history.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class History extends JavaPlugin {
    private static Permission perms = null;

    private Database database;
    private Map<UUID, HistoryUser> historyUsers = new HashMap<>();

    @Override
    public void onEnable() {
        database = new Database(getConfig().getString("name"),
                getConfig().getString("password"),
                getConfig().getString("database"),
                getConfig().getString("host"),
                getConfig().getInt("port"));

        database.createConnection();
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(), this);
        getCommand("history").setExecutor(new HistoryCommand(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        setupPermissions();

    }

    @Override
    public void onDisable() {
        try {
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Inventory getPlayerHistory(UUID id) {
        OfflinePlayer off = Bukkit.getOfflinePlayer(id);
        Inventory inventory = Bukkit.createInventory(null, 54, off.getName() + "'s History");

        HistoryUser user = historyUsers.get(id);
        if (user == null) {
            user = new HistoryUser(id, this);
            historyUsers.put(id, user);
        }

        user.loadHistory();
        for (int i = 0; i < user.getHistoryTypes().size(); i++) {
            HistoryType type = user.getHistoryTypes().get(i);
            ItemBuilder builder = new ItemBuilder(type.material());

            DateFormat df = new SimpleDateFormat("yyyy - MM - dd");
            DateFormat sec = new SimpleDateFormat("yyyy - MM - dd HH:mm::ss");

            builder.setName(type.name());

            builder.setLore(" - " + ChatColor.RED + "Type: " + type.name(),
                            " - " + ChatColor.RED + "Active: " + type.isActive(),
                            " - " + ChatColor.RED + "Reason: " + type.getReason(),
                            " - " + ChatColor.RED + "Occurred: " + df.format(type.getOccurred()),
                            " - " + ChatColor.RED + "Expiring: " + sec.format(type.getExpiring()),
                            " - " + ChatColor.RED + "Issued by: " + type.getExecutor());
            inventory.setItem(i, builder.toItemStack());
        }

        inventory.setItem(53, new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "Punish " + off.getName()).toItemStack());
        return inventory;
    }

    public Database database() {
        return this.database;
    }
    public void server(String PlayerName, String Ban_reson , Player csender, String Time) {

        Player player = (Player) csender;
        if (player.hasPermission("Gui.punish")) {
            Gui guiii = new Gui(this, 3,"Ban " + PlayerName + " on what servers");
            String n1i = getConfig().getString("Server_1");
            String l1i = "Bans the player only on the " + n1i + ".";
            String[] strArrayrri = {ChatColor.RESET + l1i};
            OutlinePane paneei = new OutlinePane(3, 1, 9, 3);
            ItemStack itemmi = new ItemStack(Material.IRON_SWORD);
            ItemMeta mmetai = itemmi.getItemMeta();
            mmetai.setDisplayName(ChatColor.RED + n1i);
            mmetai.setLore(Arrays.asList(strArrayrri));
            itemmi.setItemMeta(mmetai);
            GuiItem guiItemm = new GuiItem(itemmi, event -> {
                player.closeInventory();

                String bcmd = getConfig().getString("Ban_Command");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + Time + " server:" + n1i+ " " + Ban_reson);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
            });
            paneei.addItem(guiItemm);
            guiii.addPane(paneei);
            String n2 = getConfig().getString("Server_2");
            String l2 = "Bans the player only on the " + n2+ ".";
            String[] strArrayrr2 = {ChatColor.RESET + l2};
            OutlinePane panee1 = new OutlinePane(5, 1, 9, 3);
            ItemStack itemm1 = new ItemStack(Material.DIAMOND);
            ItemMeta mmeta1 = itemmi.getItemMeta();
            mmeta1.setDisplayName(ChatColor.RED + n2);
            mmeta1.setLore(Arrays.asList(strArrayrr2));
            itemm1.setItemMeta(mmeta1);
            GuiItem guiItemm1 = new GuiItem(itemm1, event -> {
                player.closeInventory();
                String bcmd = getConfig().getString("Ban_Command");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + Time + " server:" + n2+ " " + Ban_reson);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
            });
            panee1.addItem(guiItemm1);
            guiii.addPane(panee1);
            String n3 = "All Servers";
            String l3 = "Bans the player on all servers";
            String[] strArrayrr3 = {ChatColor.RESET + l3};
            OutlinePane panee2 = new OutlinePane(4, 1, 9, 3);
            ItemStack itemm2 = new ItemStack(Material.FIRE_CHARGE);
            ItemMeta mmeta2 = itemmi.getItemMeta();
            mmeta2.setDisplayName(ChatColor.RED + n3);
            mmeta2.setLore(Arrays.asList(strArrayrr3));
            itemm2.setItemMeta(mmeta2);
            GuiItem guiItemm2 = new GuiItem(itemm2, event -> {
                player.closeInventory();
                String bcmd = getConfig().getString("Ban_Command");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + Time + " " + Ban_reson);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
            });
            panee2.addItem(guiItemm2);
            guiii.addPane(panee2);

            String n32 = "Back";
            OutlinePane panee22 = new OutlinePane(8, 2, 9, 3);
            ItemStack itemm22 = new ItemStack(Material.BARRIER);
            ItemMeta mmeta22 = itemmi.getItemMeta();
            mmeta22.setDisplayName(ChatColor.RED + n32);
            mmeta22.setLore(null);

            itemm22.setItemMeta(mmeta22);
            GuiItem guiItemm22 = new GuiItem(itemm22, event -> {
                player.closeInventory();
                command(PlayerName, Ban_reson, csender);
            });
            panee22.addItem(guiItemm22);
            guiii.addPane(panee22);

            guiii.show((HumanEntity) csender);
            guiii.setOnGlobalClick(click -> click.setCancelled(true));
        } else {
            csender.sendMessage(ChatColor.RED + "You Don't have permission");
        }
    }
    public void command(String PlayerName, String Ban_reson , Player csender) {

        Player player = (Player) csender;
        if (player.hasPermission("Gui.punish")) {
            Gui guii = new Gui(this, 3,"Ban " + PlayerName + " for " + Ban_reson);
            String n1 = getConfig().getString("Time_1");
            String l1 = getConfig().getString("Lore_1");
            String[] strArrayrr = {ChatColor.RESET + l1};
            OutlinePane panee = new OutlinePane(2, 1, 9, 3);
            ItemStack itemm = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta = itemm.getItemMeta();
            mmeta.setDisplayName(ChatColor.RED + n1);
            mmeta.setLore(Arrays.asList(strArrayrr));
            itemm.setItemMeta(mmeta);
            GuiItem guiItemm = new GuiItem(itemm, event -> {
                player.closeInventory();
                //player.performCommand("punishtime1 " + PlayerName + " " + Ban_reson);
                punishtime1(PlayerName, Ban_reson, player);
            });
            panee.addItem(guiItemm);
            guii.addPane(panee);
            String n2 = getConfig().getString("Time_2");
            String l2 = getConfig().getString("Lore_2");
            String[] strArrayrr2 = {ChatColor.RESET + l2};
            OutlinePane panee1 = new OutlinePane(3, 1, 9, 3);
            ItemStack itemm1 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta1 = itemm.getItemMeta();
            mmeta1.setDisplayName(ChatColor.RED + n2);
            mmeta1.setLore(Arrays.asList(strArrayrr2));
            itemm1.setItemMeta(mmeta1);
            GuiItem guiItemm1 = new GuiItem(itemm1, event -> {
                player.closeInventory();
                //player.performCommand("punishtime2 " + PlayerName + " " + Ban_reson);
                punishtime2(PlayerName, Ban_reson, player);
            });
            panee1.addItem(guiItemm1);
            guii.addPane(panee1);
            String n3 = getConfig().getString("Time_3");
            String l3 = getConfig().getString("Lore_3");
            String[] strArrayrr3 = {ChatColor.RESET + l3};
            OutlinePane panee2 = new OutlinePane(4, 1, 9, 3);
            ItemStack itemm2 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta2 = itemm.getItemMeta();
            mmeta2.setDisplayName(ChatColor.RED + n3);
            mmeta2.setLore(Arrays.asList(strArrayrr3));
            itemm2.setItemMeta(mmeta2);
            GuiItem guiItemm2 = new GuiItem(itemm2, event -> {
                player.closeInventory();
                //player.performCommand("punishtime3 " + PlayerName + " " + Ban_reson);
                punishtime3(PlayerName, Ban_reson, player);
            });
            panee2.addItem(guiItemm2);
            guii.addPane(panee2);
            String n4 = getConfig().getString("Time_4");
            String l4 = getConfig().getString("Lore_4");
            String[] strArrayrr4 = {ChatColor.RESET + l4};
            OutlinePane panee3 = new OutlinePane(5, 1, 9, 3);
            ItemStack itemm3 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta3 = itemm.getItemMeta();
            mmeta3.setDisplayName(ChatColor.RED + n4);
            mmeta3.setLore(Arrays.asList(strArrayrr4));
            itemm3.setItemMeta(mmeta3);
            GuiItem guiItemm3 = new GuiItem(itemm3, event -> {
                player.closeInventory();
                //player.performCommand("punishtime4 " + PlayerName + " " + Ban_reson);
                punishtime4(PlayerName, Ban_reson, player);
            });
            panee3.addItem(guiItemm3);
            guii.addPane(panee3);
            String n5 = getConfig().getString("Time_5");
            String l5 = getConfig().getString("Lore_5");
            String[] strArrayrr5 = {ChatColor.RESET + l5};
            OutlinePane panee4 = new OutlinePane(6, 1, 9, 3);
            ItemStack itemm4 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta4 = itemm.getItemMeta();
            mmeta4.setDisplayName(ChatColor.RED + n5);
            mmeta4.setLore(Arrays.asList(strArrayrr5));
            itemm4.setItemMeta(mmeta4);
            GuiItem guiItemm4 = new GuiItem(itemm4, event -> {
                player.closeInventory();
                //player.performCommand("punishtime5 " + PlayerName + " " + Ban_reson);
                punishtime5(PlayerName, Ban_reson, player);
            });
            panee4.addItem(guiItemm4);
            guii.addPane(panee4);
            String Reason_6 = ("Back");
            OutlinePane pane6 = new OutlinePane(8, 2, 9, 5);
            ItemStack item6 = new ItemStack(Material.BARRIER);
            ItemMeta meta6 = itemm.getItemMeta();

            meta6.setDisplayName(ChatColor.RED + Reason_6);

            meta6.setLore(null);
            item6.setItemMeta(meta6);
            GuiItem guiItem6 = new GuiItem(item6, event -> {
                player.closeInventory();
                //player.performCommand("punishtime " + args[0] + " " + Reason_5);
                player.performCommand("newban " + PlayerName);
            });
            pane6.addItem(guiItem6);
            guii.addPane(pane6);

            guii.show((HumanEntity) csender);
            guii.setOnGlobalClick(click -> click.setCancelled(true));
        } else {
            csender.sendMessage(ChatColor.RED + "You Don't have permission");
        }
    }
    public void command1(String PlayerName, String Ban_reson ,Player csender) {

        Player player = (Player) csender;
        if (player.hasPermission("Gui.punish")) {
            Gui guii = new Gui( this, 3,"Mute " + PlayerName + " for " + Ban_reson);
            String n1 = getConfig().getString("Time_1");
            String l1 = getConfig().getString("Lore_1");
            String[] strArrayrr = {ChatColor.RESET + l1};
            OutlinePane panee = new OutlinePane(2, 1, 9, 3);
            ItemStack itemm = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta = itemm.getItemMeta();
            mmeta.setDisplayName(ChatColor.RED + n1);
            mmeta.setLore(Arrays.asList(strArrayrr));
            itemm.setItemMeta(mmeta);
            GuiItem guiItemm = new GuiItem(itemm, event -> {
                player.closeInventory();
                //player.performCommand("punishtime1 " + PlayerName + " " + Ban_reson);
                punishtime11(PlayerName, Ban_reson, player);
            });
            panee.addItem(guiItemm);
            guii.addPane(panee);
            String n2 = getConfig().getString("Time_2");
            String l2 = getConfig().getString("Lore_2");
            String[] strArrayrr2 = {ChatColor.RESET + l2};
            OutlinePane panee1 = new OutlinePane(3, 1, 9, 3);
            ItemStack itemm1 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta1 = itemm.getItemMeta();
            mmeta1.setDisplayName(ChatColor.RED + n2);
            mmeta1.setLore(Arrays.asList(strArrayrr2));
            itemm1.setItemMeta(mmeta1);
            GuiItem guiItemm1 = new GuiItem(itemm1, event -> {
                player.closeInventory();
                //player.performCommand("punishtime2 " + PlayerName + " " + Ban_reson);
                punishtime21(PlayerName, Ban_reson, player);
            });
            panee1.addItem(guiItemm1);
            guii.addPane(panee1);
            String n3 = getConfig().getString("Time_3");
            String l3 = getConfig().getString("Lore_3");
            String[] strArrayrr3 = {ChatColor.RESET + l3};
            OutlinePane panee2 = new OutlinePane(4, 1, 9, 3);
            ItemStack itemm2 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta2 = itemm.getItemMeta();
            mmeta2.setDisplayName(ChatColor.RED + n3);
            mmeta2.setLore(Arrays.asList(strArrayrr3));
            itemm2.setItemMeta(mmeta2);
            GuiItem guiItemm2 = new GuiItem(itemm2, event -> {
                player.closeInventory();
                //player.performCommand("punishtime3 " + PlayerName + " " + Ban_reson);
                punishtime31(PlayerName, Ban_reson, player);
            });
            panee2.addItem(guiItemm2);
            guii.addPane(panee2);
            String n4 = getConfig().getString("Time_4");
            String l4 = getConfig().getString("Lore_4");
            String[] strArrayrr4 = {ChatColor.RESET + l4};
            OutlinePane panee3 = new OutlinePane(5, 1, 9, 3);
            ItemStack itemm3 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta3 = itemm.getItemMeta();
            mmeta3.setDisplayName(ChatColor.RED + n4);
            mmeta3.setLore(Arrays.asList(strArrayrr4));
            itemm3.setItemMeta(mmeta3);
            GuiItem guiItemm3 = new GuiItem(itemm3, event -> {
                player.closeInventory();
                //player.performCommand("punishtime4 " + PlayerName + " " + Ban_reson);
                punishtime41(PlayerName, Ban_reson, player);
            });
            panee3.addItem(guiItemm3);
            guii.addPane(panee3);
            String n5 = getConfig().getString("Time_5");
            String l5 = getConfig().getString("Lore_5");
            String[] strArrayrr5 = {ChatColor.RESET + l5};
            OutlinePane panee4 = new OutlinePane(6, 1, 9, 3);
            ItemStack itemm4 = new ItemStack(Material.valueOf(getConfig().getString("Time_bolck")));
            ItemMeta mmeta4 = itemm.getItemMeta();
            mmeta4.setDisplayName(ChatColor.RED + n5);
            mmeta4.setLore(Arrays.asList(strArrayrr5));
            itemm4.setItemMeta(mmeta4);
            GuiItem guiItemm4 = new GuiItem(itemm4, event -> {
                player.closeInventory();
                //player.performCommand("punishtime5 " + PlayerName + " " + Ban_reson);
                punishtime51(PlayerName, Ban_reson, player);
            });
            panee4.addItem(guiItemm4);
            guii.addPane(panee4);

            String Reason_6 = ("Back");
            OutlinePane pane6 = new OutlinePane(8, 2, 9, 5);
            ItemStack item6 = new ItemStack(Material.BARRIER);
            ItemMeta meta6 = itemm.getItemMeta();

            meta6.setDisplayName(ChatColor.RED + Reason_6);

            meta6.setLore(null);
            item6.setItemMeta(meta6);
            GuiItem guiItem6 = new GuiItem(item6, event -> {
                player.closeInventory();
                //player.performCommand("punishtime " + args[0] + " " + Reason_5);
                player.performCommand("newmute " + PlayerName);
            });
            pane6.addItem(guiItem6);
            guii.addPane(pane6);
            guii.show((HumanEntity) csender);
            guii.setOnGlobalClick(click -> click.setCancelled(true));
        } else {
            csender.sendMessage(ChatColor.RED + "You Don't have permission");
        }
    }
    public void punishtime1(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                String b1 = getConfig().getString("Ban_Time_1");
                if (player.hasPermission("Gui.time.1")) {
                    String bcmd = getConfig().getString("Ban_Command");
                   server(PlayerName,Ban_reson, player, b1);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b1 + " " + Ban_reson);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);


                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime2(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                String b2 = getConfig().getString("Ban_Time_2");
                if (player.hasPermission("Gui.time.2")) {
                    String bcmd = getConfig().getString("Ban_Command");
                    server(PlayerName,Ban_reson, player, b2);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b2 + " " + Ban_reson);
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);

                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime3(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");

            }
            else{
                if (player.hasPermission("Gui.time.3")) {
                    String b3 = getConfig().getString("Ban_Time_3");
                    String bcmd = getConfig().getString("Ban_Command");
                    server(PlayerName,Ban_reson, player, b3);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b3 + " " + Ban_reson);
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);

                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime4(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else {
                if (player.hasPermission("Gui.time.4")) {
                    String b4 = getConfig().getString("Ban_Time_4");
                    String bcmd = getConfig().getString("Ban_Command");
                    server(PlayerName,Ban_reson, player, b4);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b4 + " " + Ban_reson);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime5(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                if (player.hasPermission("Gui.time.5")) {
                    String b5 = getConfig().getString("Ban_Time_5");
                    String bcmd = getConfig().getString("Ban_Command");

                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),bcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b5 + " " + Ban_reson);
                   // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
                    server(PlayerName,Ban_reson, player, b5);

                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime11(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot ban this player");
        } else {
            Player player = (Player) csender;

            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot mute Your Self");
            }
            else{
                String b1 = getConfig().getString("Ban_Time_1");
                if (player.hasPermission("Gui.time.1")) {
                    String mcmd = getConfig().getString("Mute_Command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), mcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b1 + " " + Ban_reson);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime21(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot mute this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                String b2 = getConfig().getString("Ban_Time_2");
                if (player.hasPermission("Gui.time.2")) {
                    String mcmd = getConfig().getString("Mute_Command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), mcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b2 + " " + Ban_reson);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);

                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime31(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot mute this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                if (player.hasPermission("Gui.time.3")) {
                    String b3 = getConfig().getString("Ban_Time_3");
                    String mcmd = getConfig().getString("Mute_Command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), mcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b3 + " " + Ban_reson);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }
    public void punishtime41(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot mute this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else {
                if (player.hasPermission("Gui.time.4")) {
                    String b4 = getConfig().getString("Ban_Time_4");
                    String mcmd = getConfig().getString("Mute_Command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), mcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b4 + " " + Ban_reson);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);
                }
            }
        }
    }
    public void punishtime51(String PlayerName, String Ban_reson ,Player csender){
        if(perms.playerHas("world", PlayerName , "gui.no")) {
            Player player = (Player) csender;
            player.sendMessage(ChatColor.RED + "You cannot mute this player");
        } else {
            Player player = (Player) csender;
            if(player.getName().equalsIgnoreCase(PlayerName)) {
                player.sendMessage(ChatColor.RED + "You Cannot Ban Your Self");
            }
            else{
                if (player.hasPermission("Gui.time.5")) {
                    String b5 = getConfig().getString("Ban_Time_5");
                    String mcmd = getConfig().getString("Mute_Command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), mcmd + " " + PlayerName + " --sender=" + csender.getName() + " --sender-uuid=" + csender.getUniqueId() + " " + b5 + " " + Ban_reson);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Smite " + PlayerName);

                }else {
                    player.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            }
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("newban")) {
            if (args.length > 0) {
                if (sender instanceof Player) ;
                Player player = (Player) sender;
                if (player.hasPermission("Gui.punish.ban")) {
                    Gui gui = new Gui(this,5, "Ban: " + args[0]);
                    String lure_1 = getConfig().getString("First_Reason_lore");
                    String Reason_1 = getConfig().getString("First_Reason");
                    String[] strArray = {ChatColor.RESET + lure_1};

                    OutlinePane pane = new OutlinePane(2, 1, 9, 5);
                    ItemStack item = new ItemStack(Material.valueOf(getConfig().getString("First_Reason_block")));
                    ItemMeta meta = item.getItemMeta();
                    String[] strArrayd = {ChatColor.RESET + lure_1};
                    meta.setDisplayName(ChatColor.RED + Reason_1);
                    meta.setLore(Arrays.asList(strArrayd));
                    item.setItemMeta(meta);
                    GuiItem guiItem = new GuiItem(item, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_1, player );
                    });
                    pane.addItem(guiItem);
                    gui.addPane(pane);



                    String lure_2 = getConfig().getString("Second__Reason_lore");
                    String Reason_2 = getConfig().getString("Second__Reason");
                    String[] strArray2 = {ChatColor.RESET + lure_1};
                    OutlinePane pane1 = new OutlinePane(3, 1, 9, 5);
                    ItemStack item1 = new ItemStack(Material.valueOf(getConfig().getString("Second_Reason_block")));
                    ItemMeta meta1 = item.getItemMeta();
                    String[] strArray1 = {ChatColor.RESET + lure_2};
                    meta1.setDisplayName(ChatColor.RED + Reason_2);
                    meta1.setLore(Arrays.asList(strArray1));
                    item1.setItemMeta(meta1);
                    GuiItem guiItem1 = new GuiItem(item1, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_2, player );
                    });
                    pane1.addItem(guiItem1);
                    gui.addPane(pane1);


                    String lure_3 = getConfig().getString("Third_Reason_lore");
                    String Reason_3 = getConfig().getString("Third_Reason");
                    String[] strArray3 = {ChatColor.RESET + lure_3};
                    OutlinePane pane2 = new OutlinePane(4, 1, 9, 5);
                    ItemStack item2 = new ItemStack(Material.valueOf(getConfig().getString("Third_Reason_block")));
                    ItemMeta meta2 = item.getItemMeta();
                    meta2.setDisplayName(ChatColor.RED + Reason_3);
                    meta2.setLore(Arrays.asList(strArray3));
                    item2.setItemMeta(meta2);
                    GuiItem guiItem2 = new GuiItem(item2, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_3, player );
                    });
                    pane2.addItem(guiItem2);
                    gui.addPane(pane2);


                    String lure_4 = getConfig().getString("Forth_Reason_lore");
                    String Reason_4 = getConfig().getString("Forth_Reason");
                    String[] strArray4 = {ChatColor.RESET + lure_4};
                    OutlinePane pane3 = new OutlinePane(5, 1, 9, 5);
                    ItemStack item3 = new ItemStack(Material.valueOf(getConfig().getString("Forth_Reason_block")));
                    ItemMeta meta3 = item.getItemMeta();
                    meta3.setDisplayName(ChatColor.RED + Reason_4);
                    meta3.setLore(Arrays.asList(strArray4));
                    item3.setItemMeta(meta3);
                    GuiItem guiItem3 = new GuiItem(item3, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_4, player );
                    });
                    pane3.addItem(guiItem3);
                    gui.addPane(pane3);

                    String lure_5 = getConfig().getString("Fith_Reason_lore");
                    String Reason_5 = getConfig().getString("Fith_Reason");
                    String[] strArray5 = {ChatColor.RESET + lure_5};
                    OutlinePane pane4 = new OutlinePane(6, 1, 9, 5);
                    ItemStack item4 = new ItemStack(Material.valueOf(getConfig().getString("Fith_Reason_block")));
                    ItemMeta meta4 = item.getItemMeta();
                    meta4.setDisplayName(ChatColor.RED + Reason_5);
                    meta4.setLore(Arrays.asList(strArray5));
                    item4.setItemMeta(meta4);
                    GuiItem guiItem4 = new GuiItem(item4, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_5, player );
                    });
                    pane4.addItem(guiItem4);
                    gui.addPane(pane4);

                    String lure_6 = getConfig().getString("Sixth_Reason_lore");
                    String Reason_6 = getConfig().getString("Sixth_Reason");
                    String[] strArray6 = {ChatColor.RESET + lure_6};
                    OutlinePane pane5 = new OutlinePane(2, 2, 9, 5);
                    ItemStack item5 = new ItemStack(Material.valueOf(getConfig().getString("Sixth_Reason_block")));
                    ItemMeta meta5 = item.getItemMeta();
                    meta5.setDisplayName(ChatColor.RED + Reason_6);
                    meta5.setLore(Arrays.asList(strArray6));
                    item5.setItemMeta(meta5);
                    GuiItem guiItem5 = new GuiItem(item5, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_6, player );
                    });
                    pane5.addItem(guiItem5);
                    gui.addPane(pane5);

                    String lure_7 = getConfig().getString("Seventh_Reason_lore");
                    String Reason_7 = getConfig().getString("Seventh_Reason");
                    String[] strArray7 = {ChatColor.RESET + lure_7};
                    OutlinePane pane6 = new OutlinePane(3, 2, 9, 5);
                    ItemStack item6 = new ItemStack(Material.valueOf(getConfig().getString("Seventh_Reason_block")));
                    ItemMeta meta6 = item.getItemMeta();
                    meta6.setDisplayName(ChatColor.RED + Reason_7);
                    meta6.setLore(Arrays.asList(strArray7));
                    item6.setItemMeta(meta6);
                    GuiItem guiItem6 = new GuiItem(item6, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_7, player );
                    });
                    pane6.addItem(guiItem6);
                    gui.addPane(pane6);


                    String lure_8 = getConfig().getString("Eighth_Reason_lore");
                    String Reason_8 = getConfig().getString("Eighth_Reason");
                    String[] strArray8 = {ChatColor.RESET + lure_8};
                    OutlinePane pane7 = new OutlinePane(4, 2, 9, 5);
                    ItemStack item7 = new ItemStack(Material.valueOf(getConfig().getString("Eighth_Reason_block")));
                    ItemMeta meta7 = item.getItemMeta();
                    meta7.setDisplayName(ChatColor.RED + Reason_8);
                    meta7.setLore(Arrays.asList(strArray8));
                    item7.setItemMeta(meta7);
                    GuiItem guiItem7 = new GuiItem(item7, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_8, player );
                    });
                    pane7.addItem(guiItem7);
                    gui.addPane(pane7);

                    String lure_9 = getConfig().getString("Ninth_Reason_lore");
                    String Reason_9 = getConfig().getString("Ninth_Reason");
                    String[] strArray9 = {ChatColor.RESET + lure_9};
                    OutlinePane pane8 = new OutlinePane(5, 2, 9, 5);
                    ItemStack item8 = new ItemStack(Material.valueOf(getConfig().getString("Ninth_Reason_block")));
                    ItemMeta meta8 = item.getItemMeta();
                    meta8.setDisplayName(ChatColor.RED + Reason_9);
                    meta8.setLore(Arrays.asList(strArray9));
                    item8.setItemMeta(meta8);
                    GuiItem guiItem8 = new GuiItem(item8, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_9, player );
                    });
                    pane8.addItem(guiItem8);
                    gui.addPane(pane8);

                    String lure_10 = getConfig().getString("Tenth_Reason_lore");
                    String Reason_10 = getConfig().getString("Tenth_Reason");
                    String[] strArray10 = {ChatColor.RESET + lure_10};
                    OutlinePane pane9 = new OutlinePane(6, 2, 9, 5);
                    ItemStack item9 = new ItemStack(Material.valueOf(getConfig().getString("Tenth_Reason_block")));
                    ItemMeta meta9 = item.getItemMeta();
                    meta9.setDisplayName(ChatColor.RED + Reason_10);
                    meta9.setLore(Arrays.asList(strArray10));
                    item9.setItemMeta(meta9);
                    GuiItem guiItem9 = new GuiItem(item9, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_10, player );
                    });
                    pane9.addItem(guiItem9);
                    gui.addPane(pane9);


                    String lure_11 = getConfig().getString("Eleventh_Reason_lore");
                    String Reason_11 = getConfig().getString("Eleventh_Reason");
                    String[] strArray11 = {ChatColor.RESET + lure_11};
                    OutlinePane pane10 = new OutlinePane(2, 3, 9, 5);
                    ItemStack item10 = new ItemStack(Material.valueOf(getConfig().getString("Eleventh_Reason_block")));
                    ItemMeta meta10 = item.getItemMeta();
                    meta10.setDisplayName(ChatColor.RED + Reason_11);
                    meta10.setLore(Arrays.asList(strArray11));
                    item10.setItemMeta(meta10);
                    GuiItem guiItem10 = new GuiItem(item10, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_11, player );
                    });
                    pane10.addItem(guiItem10);
                    gui.addPane(pane10);

                    String lure_12 = getConfig().getString("Twelfth_Reason_lore");
                    String Reason_12 = getConfig().getString("Twelfth_Reason");
                    String[] strArray12 = {ChatColor.RESET + lure_12};
                    OutlinePane pane12 = new OutlinePane(3, 3, 9, 5);
                    ItemStack item12 = new ItemStack(Material.valueOf(getConfig().getString("Twelfth_Reason_block")));
                    ItemMeta meta12 = item.getItemMeta();
                    meta12.setDisplayName(ChatColor.RED + Reason_12);
                    meta12.setLore(Arrays.asList(strArray12));
                    item12.setItemMeta(meta12);
                    GuiItem guiItem12 = new GuiItem(item12, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_12, player );
                    });
                    pane12.addItem(guiItem12);
                    gui.addPane(pane12);

                    String lure_13 = getConfig().getString("Thirteenth_Reason_lore");
                    String Reason_13 = getConfig().getString("Thirteenth_Reason");
                    String[] strArray14 = {ChatColor.RESET + lure_13};
                    OutlinePane pane14 = new OutlinePane(4, 3, 9, 5);
                    ItemStack item13 = new ItemStack(Material.valueOf(getConfig().getString("Thirteenth_Reason_block")));
                    ItemMeta meta13 = item.getItemMeta();
                    meta13.setDisplayName(ChatColor.RED + Reason_13);
                    meta13.setLore(Arrays.asList(strArray14));
                    item13.setItemMeta(meta13);
                    GuiItem guiItem13 = new GuiItem(item13, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_13, player );
                    });
                    pane14.addItem(guiItem13);
                    gui.addPane(pane14);

                    String lure_14 = getConfig().getString("Fourteenth_Reason_lore");
                    String Reason_14 = getConfig().getString("Fourteenth_Reason");
                    String[] strArray15 = {ChatColor.RESET + lure_14};
                    OutlinePane pane15 = new OutlinePane(4, 3, 9, 5);
                    ItemStack item14 = new ItemStack(Material.valueOf(getConfig().getString("Fourteenth_Reason_block")));
                    ItemMeta meta14 = item.getItemMeta();
                    meta14.setDisplayName(ChatColor.RED + Reason_14);
                    meta14.setLore(Arrays.asList(strArray15));
                    item14.setItemMeta(meta14);
                    GuiItem guiItem14 = new GuiItem(item14, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_14, player );
                    });
                    pane14.addItem(guiItem14);
                    gui.addPane(pane14);

                    String lure_15 = getConfig().getString("Fifteenth_Reason_lore");
                    String Reason_15 = getConfig().getString("Fifteenth_Reason");
                    String[] strArray16 = {ChatColor.RESET + lure_15};
                    OutlinePane pane16 = new OutlinePane(6, 3, 9, 5);
                    ItemStack item15 = new ItemStack(Material.valueOf(getConfig().getString("Fifteenth_Reason_block")));
                    ItemMeta meta15 = item.getItemMeta();
                    meta15.setDisplayName(ChatColor.RED + Reason_15);
                    meta15.setLore(Arrays.asList(strArray16));
                    item15.setItemMeta(meta15);
                    GuiItem guiItem15 = new GuiItem(item15, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command(args[0], Reason_15, player );
                    });
                    pane16.addItem(guiItem15);
                    gui.addPane(pane16);

                    String Reason_66 = ("Back");
                    OutlinePane pane66 = new OutlinePane(8, 4, 9, 5);
                    ItemStack item66 = new ItemStack(Material.BARRIER);
                    ItemMeta meta66 = item.getItemMeta();

                    meta66.setDisplayName(ChatColor.RED + Reason_66);

                    meta66.setLore(null);
                    item66.setItemMeta(meta66);
                    GuiItem guiItem66 = new GuiItem(item66, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_5);
                        player.performCommand("punish " + args[0]);
                    });
                    pane66.addItem(guiItem66);
                    gui.addPane(pane66);

                    gui.show((HumanEntity) sender);
                    gui.setOnGlobalClick(click -> click.setCancelled(true));
                } else {
                    sender.sendMessage(ChatColor.RED + "You Don't have permission");
                }

            } else {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "You need to put a player name");
            }
        }
        if (command.getName().equals("newmute")) {
            if (args.length > 0) {
                if (sender instanceof Player) ;
                Player player = (Player) sender;
                if (player.hasPermission("Gui.punish.mute")) {
                    Gui gui = new Gui(this,3, "Mute: " + args[0]);
                    String lure_1 = ("Repeatedly asking for staff");
                    String Reason_1 = ("Begging For staff");
                    String[] strArray = {ChatColor.RESET + lure_1};

                    OutlinePane pane = new OutlinePane(2, 1, 9, 5);
                    ItemStack item = new ItemStack (Material.GOLD_BLOCK);
                    ItemMeta meta = item.getItemMeta();
                    String[] strArrayd = {ChatColor.RESET + lure_1};
                    meta.setDisplayName(ChatColor.RED + Reason_1);
                    meta.setLore(Arrays.asList(strArrayd));
                    item.setItemMeta(meta);
                    GuiItem guiItem = new GuiItem(item, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_1);
                        command1(args[0], Reason_1, player );
                    });
                    pane.addItem(guiItem);
                    gui.addPane(pane);


                    String lure_2 = ("Repeatedly saying the same messaged or similar");
                    String Reason_2 = ("Spaming");
                    String[] strArray2 = {ChatColor.RESET + lure_1};
                    OutlinePane pane1 = new OutlinePane(3, 1, 9, 5);
                    ItemStack item1 = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta meta1 = item.getItemMeta();
                    String[] strArray1 = {ChatColor.RESET + lure_2};
                    meta1.setDisplayName(ChatColor.RED + Reason_2);
                    meta1.setLore(Arrays.asList(strArray1));
                    item1.setItemMeta(meta1);
                    GuiItem guiItem1 = new GuiItem(item1, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_2);
                        command1(args[0], Reason_2, player );
                    });
                    pane1.addItem(guiItem1);
                    gui.addPane(pane1);


                    String lure_3 = ("Advertising A Server or discord in chat.");
                    String Reason_3 = ("Advertising");
                    String[] strArray3 = {ChatColor.RESET + lure_3};
                    OutlinePane pane2 = new OutlinePane(4, 1, 9, 5);
                    ItemStack item2 = new ItemStack(Material.SIGN);
                    ItemMeta meta2 = item.getItemMeta();
                    meta2.setDisplayName(ChatColor.RED + Reason_3);
                    meta2.setLore(Arrays.asList(strArray3));
                    item2.setItemMeta(meta2);
                    GuiItem guiItem2 = new GuiItem(item2, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_3);
                        command1(args[0], Reason_3, player );
                    });
                    pane2.addItem(guiItem2);
                    gui.addPane(pane2);


                    String lure_4 = ("Cussing In Chat");
                    String Reason_4 = ("Saying Cusswords in chat");
                    String[] strArray4 = {ChatColor.RESET + lure_4};
                    OutlinePane pane3 = new OutlinePane(5, 1, 9, 5);
                    ItemStack item3 = new ItemStack(Material.STRING);
                    ItemMeta meta3 = item.getItemMeta();
                    meta3.setDisplayName(ChatColor.RED + Reason_4);
                    meta3.setLore(Arrays.asList(strArray4));
                    item3.setItemMeta(meta3);
                    GuiItem guiItem3 = new GuiItem(item3, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_4);
                        command1(args[0], Reason_4, player );
                    });
                    pane3.addItem(guiItem3);
                    gui.addPane(pane3);

                    String lure_5 = ("Pretending To be staff");
                    String Reason_5 = ("Impersonating Staff");
                    String[] strArray5 = {ChatColor.RESET + lure_5};
                    OutlinePane pane4 = new OutlinePane(6, 1, 9, 5);
                    ItemStack item4 = new ItemStack(Material.BOOK);
                    ItemMeta meta4 = item.getItemMeta();
                    meta4.setDisplayName(ChatColor.RED + Reason_5);
                    meta4.setLore(Arrays.asList(strArray5));
                    item4.setItemMeta(meta4);
                    GuiItem guiItem4 = new GuiItem(item4, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_5);
                        command1(args[0], Reason_5, player );
                    });
                    pane4.addItem(guiItem4);
                    gui.addPane(pane4);


                    String Reason_6 = ("Back");
                    OutlinePane pane6 = new OutlinePane(8, 2, 9, 5);
                    ItemStack item6 = new ItemStack(Material.BARRIER);
                    ItemMeta meta6 = item.getItemMeta();

                    meta6.setDisplayName(ChatColor.RED + Reason_6);

                    meta6.setLore(null);
                    item6.setItemMeta(meta6);
                    GuiItem guiItem6 = new GuiItem(item6, event -> {
                        player.closeInventory();
                        //player.performCommand("punishtime " + args[0] + " " + Reason_5);
                        player.performCommand("punish " + args[0]);
                    });
                    pane6.addItem(guiItem6);
                    gui.addPane(pane6);

                    gui.show((HumanEntity) sender);
                    gui.setOnGlobalClick(click -> click.setCancelled(true));
                } else {
                    sender.sendMessage(ChatColor.RED + "You Don't have permission");
                }

            } else {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "You need to put a player name");
            }
        }
        if (command.getName().equals("punish")) {
            if (args.length > 0) {
                if (sender instanceof Player) ;
                Player player = (Player) sender;

                if (player.hasPermission("Gui.punish")) {

                    Gui guiiii = new Gui(this, 3,"Punish " + args[0]);

                    OutlinePane paneee = new OutlinePane(3, 1, 9, 3);
                    ItemStack itemm = new ItemStack(Material.IRON_AXE);
                    ItemMeta mmeta = itemm.getItemMeta();
                    mmeta.setDisplayName(ChatColor.RED + "New Ban");
                    itemm.setItemMeta(mmeta);
                    GuiItem guiItemmm = new GuiItem(itemm, event -> {
                        player.closeInventory();
                        player.performCommand("newban " + args[0]);
                    });
                    paneee.addItem(guiItemmm);
                    guiiii.addPane(paneee);

                    OutlinePane paneee9 = new OutlinePane(5, 1, 9, 3);
                    ItemStack iitemm9 = new ItemStack(Material.BOOK );
                    ItemMeta mmmeta9 = itemm.getItemMeta();
                    mmmeta9.setDisplayName(ChatColor.RED + "New mute");

                    iitemm9.setItemMeta(mmmeta9);
                    GuiItem guiItemmm9 = new GuiItem(iitemm9, event -> {
                        player.closeInventory();
                        player.performCommand("newmute " + args[0]);
                    });
                    paneee9.addItem(guiItemmm9);
                    guiiii.addPane(paneee9);

                    OutlinePane paneee5 = new OutlinePane(1, 1, 9, 3);
                    ItemStack iitemm5 = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta mmmeta5 = itemm.getItemMeta();
                    mmmeta5.setDisplayName(ChatColor.GREEN + "Pardon");

                    iitemm5.setItemMeta(mmmeta5);
                    GuiItem guiItemmm5 = new GuiItem(iitemm5, event -> {
                        player.closeInventory();
                        player.performCommand("pardon " + args[0]);
                    });
                    paneee5.addItem(guiItemmm5);
                    guiiii.addPane(paneee5);

                    OutlinePane paneee2 = new OutlinePane(7, 1, 9, 3);
                    ItemStack iitemm2 = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta mmmeta2 = itemm.getItemMeta();
                    mmmeta2.setDisplayName(ChatColor.RED + "Player History");

                    iitemm2.setItemMeta(mmmeta2);
                    GuiItem guiItemmm2 = new GuiItem(iitemm2, event -> {
                        player.closeInventory();
                        player.performCommand("punshment:history " + args[0]);
                    });
                    paneee2.addItem(guiItemmm2);
                    guiiii.addPane(paneee2);


                    guiiii.show((HumanEntity) sender);
                    guiiii.setOnGlobalClick(click -> click.setCancelled(true));
                } else {
                    sender.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            } else {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "You need to put a player name");
            }

        }
        if (command.getName().equals("pardon")) {
            if (args.length > 0) {
                if (sender instanceof Player) ;
                Player player = (Player) sender;
                if (player.hasPermission("Gui.pardon")) {
                    Gui guiii = new Gui(this,3, "Pardon " + args[0]);

                    OutlinePane paneee = new OutlinePane(2, 1, 9, 3);
                    ItemStack itemm = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta mmeta = itemm.getItemMeta();
                    mmeta.setDisplayName(ChatColor.GREEN + "Unban");

                    itemm.setItemMeta(mmeta);
                    GuiItem guiItemmm = new GuiItem(itemm, event -> {
                        if(player.hasPermission("Gui.pardon.unban"));
                        player.closeInventory();
                        player.setOp(true);
                        String n1i = getConfig().getString("Server_1");
                        String n2 = getConfig().getString("Server_2");
                        player.performCommand("unban -s " + args[0] + " server:" + n1i);
                        player.performCommand("unban -s " + args[0]);
                        player.performCommand("unban -s " + args[0] + " server:" + n2  );
                        player.setOp(false);


                    });





                    paneee.addItem(guiItemmm);
                    guiii.addPane(paneee);

                    OutlinePane paneee2 = new OutlinePane(4, 1, 9, 3);
                    ItemStack iitemm = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta mmmeta = iitemm.getItemMeta();
                    mmmeta.setDisplayName(ChatColor.GREEN + "Unmute");

                    iitemm.setItemMeta(mmmeta);
                    GuiItem guiItemmmf = new GuiItem(iitemm, event -> {
                        player.closeInventory();
                        if(player.hasPermission("Gui.pardon.unmute"));
                        if (player.isOp()) {
                            player.performCommand("unmute " + args[0]);
                        } else {
                            player.setOp(true);
                            player.performCommand("unmute " + args[0]);
                            player.setOp(false);
                        }
                    });
                    paneee.addItem(guiItemmmf);
                    guiii.addPane(paneee2);



                    OutlinePane paneee1 = new OutlinePane(6, 1, 9, 3);
                    ItemStack iitemm1 = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta mmmeta1 = itemm.getItemMeta();
                    mmmeta1.setDisplayName(ChatColor.RED + "No");

                    iitemm1.setItemMeta(mmmeta1);
                    GuiItem guiItemmm1 = new GuiItem(iitemm1, event -> {
                        player.closeInventory();
                        player.performCommand("punish " + args[0]);
                    });
                    paneee1.addItem(guiItemmm1);
                    guiii.addPane(paneee1);
                    guiii.show((HumanEntity) sender);
                    guiii.setOnGlobalClick(click -> click.setCancelled(true));
                } else {
                    sender.sendMessage(ChatColor.RED + "You Don't have permission");
                }
            } else {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "You need to put a player name");
            }
        }
        return true;
    }
    public static Permission getPermissions() {
        return perms;
    }
}
