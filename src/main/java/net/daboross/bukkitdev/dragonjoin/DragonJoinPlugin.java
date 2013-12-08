/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.dragonjoin;

import java.util.logging.Level;
import net.daboross.bukkitdev.dragonjoin.listeners.OnFirstJoinListener;
import net.daboross.bukkitdev.dragonjoin.listeners.OnJoinListener;
import net.daboross.bukkitdev.dragonjoin.listeners.OnLeaveListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DragonJoinPlugin extends JavaPlugin {

    private DragonCreator dc;

    @Override
    public void onEnable() {
        dc = new DragonCreator(this);
        saveDefaultConfig();
        register(dc);
        for (String mode : getConfig().getStringList("modes")) {
            switch (mode) {
                case "onjoin":
                    register(new OnJoinListener(dc));
                    getLogger().log(Level.INFO, "Registered listener OnJoin");
                    break;
                case "onfirstjoin":
                    register(new OnFirstJoinListener(dc));
                    getLogger().log(Level.INFO, "Registered listener OnFirstJoin");
                    break;
                case "onleave":
                    register(new OnLeaveListener(dc));
                    getLogger().log(Level.INFO, "Registered listener OnLeave");
                    break;
                default:
                    getLogger().log(Level.WARNING, "Mode ''{0}'' unknown.", mode);
            }
        }
    }

    private void register(Listener l) {
        getServer().getPluginManager().registerEvents(l, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("dragonjoin.trigger") && (sender instanceof Player)) {
            dc.createDragon(((Player) sender).getLocation());
            return true;
        }
        return false;
    }
}
