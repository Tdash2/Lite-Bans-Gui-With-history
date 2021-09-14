package me.dash.history.type.types;

import me.dash.history.History;
import me.dash.history.type.HistoryType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.UUID;

public class KickHistory extends HistoryType {

    public KickHistory(History history, UUID id, String reason, long occurred, long expiring, String executor, boolean active) {
        super(history, id, reason, occurred, expiring, executor, active);
    }

    @Override
    public String name() {
        return ChatColor.GOLD +  "KICK";
    }

    @Override
    public Material material() {
        return Material.STICK;
    }
}
