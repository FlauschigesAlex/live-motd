package at.flauschigesalex.live_motd.minecraft.paper

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.lib.minecraft.paper.base.FlauschigeLibraryPaper
import at.flauschigesalex.live_motd.Commands
import at.flauschigesalex.live_motd.Configuration
import at.flauschigesalex.live_motd.utils.Commons
import at.flauschigesalex.live_motd.utils.scheduleAsync
import at.flauschigesalex.live_motd.utils.sendNewerVersionMessage
import at.flauschigesalex.rinth.version.checker.VersionChecker
import at.flauschigesalex.rinth.version.listener.PaperVersionUpdateListener
import at.flauschigesalex.rinth.version.onChanges
import org.bstats.bukkit.Metrics
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused", "UNUSED_EXPRESSION", "UNUSED_VARIABLE")
class PaperLiveMotdPlugin : JavaPlugin(), Listener {
    
    override fun onEnable() {
        FlauschigeLibraryPaper.init(this, javaClass.packageName)

        Commons.dataFolder = FileManager(this.dataFolder)

        Commands // LOAD COMMANDS

        // BEGIN BSTATS
        val metrics = Metrics(this, 31679)

        // BEGIN VERSION CHECKER
        PaperVersionUpdateListener(this) { audience ->
            scheduleAsync {
                val changes = VersionChecker.check(Commons.slug).currentVersionDiff(server).getOrThrow()
                changes.onChanges {
                    audience.sendNewerVersionMessage(this)
                }
            }
        }
    }
    
    @EventHandler
    private fun onPlayerList(event: ServerListPingEvent) {
        event.motd(Configuration.MOTD)
    }
}