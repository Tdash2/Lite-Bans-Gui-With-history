package me.dash.history.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getView().getTitle().endsWith("History")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

            if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                OfflinePlayer offlinePlayer = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().split("\\s++")[1]);
                event.getWhoClicked().closeInventory();
                Bukkit.dispatchCommand(event.getWhoClicked(), "punish " + offlinePlayer.getName());
            }
        }
    }
}
