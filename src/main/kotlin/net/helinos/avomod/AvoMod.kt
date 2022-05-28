package net.helinos.avomod

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AvoMod : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getLogger().info("Enabled " + this.name)
    }

    override fun onDisable() {
        Bukkit.getLogger().info("Disabled " + this.name)
    }
}