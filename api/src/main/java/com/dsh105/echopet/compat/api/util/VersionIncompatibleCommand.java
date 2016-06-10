/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.util;

import com.dsh105.echopet.compat.api.plugin.IEchoPetPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VersionIncompatibleCommand implements CommandExecutor {

    private final IEchoPetPlugin plugin;
    private String cmdLabel;
    private String msg;
    private String perm;
    private String permMsg;

    public VersionIncompatibleCommand(IEchoPetPlugin plugin, String cmdLabel, String msg, String perm, String permMsg) {
        this.plugin = plugin;
        this.cmdLabel = cmdLabel;
        this.msg = msg;
        this.perm = perm;
        this.permMsg = permMsg;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission(this.perm)) {
            commandSender.sendMessage(plugin.getPrefix() + " " + this.msg);
        } else {
            commandSender.sendMessage(plugin.getPrefix() + " " + this.permMsg);
        }
        return true;
    }
}