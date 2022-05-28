package net.helinos.avomod

import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class AvoMod : JavaPlugin() {

    companion object {
        const val NAME = "AvoMod"
        const val VERSION = "1.0.0"
        const val ALIAS = "$NAME $VERSION"
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(EventHandler, this)
        Logger.info("$ALIAS enabled")
    }

    override fun onDisable() {
        // todo: idk if paper does this already
        HandlerList.unregisterAll(this)
        Logger.info("$ALIAS disabled")
    }
}
