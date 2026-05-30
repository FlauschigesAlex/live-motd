package at.flauschigesalex.live_motd.minecraft.velocity

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.lib.minecraft.velocity.base.FlauschigeLibraryVelocity
import at.flauschigesalex.live_motd.Commands
import at.flauschigesalex.live_motd.Configuration
import at.flauschigesalex.live_motd.utils.Commons
import at.flauschigesalex.live_motd.utils.scheduleAsync
import at.flauschigesalex.live_motd.utils.sendNewerVersionMessage
import at.flauschigesalex.rinth.version.checker.VersionChecker
import at.flauschigesalex.rinth.version.listener.VelocityVersionUpdateListener
import at.flauschigesalex.rinth.version.onChanges
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.bstats.velocity.Metrics
import org.slf4j.Logger

@Suppress("unused", "UNUSED_EXPRESSION", "UNUSED_VARIABLE")
@Plugin(id = "live-motd")
internal class VelocityLiveMotdPlugin @Inject private constructor(
    val server: ProxyServer,
    val logger: Logger,
    val bStats: Metrics.Factory
) {
    
    @Subscribe
    private fun onInitialize(event: ProxyInitializeEvent) {
        FlauschigeLibraryVelocity.init(this, server, javaClass.packageName)

        Commons.dataFolder = FileManager("plugins/${javaClass.simpleName}").let {
            it.createDirectory()
            return@let it
        }

        Commands // LOAD COMMANDS
        
        // BEGIN BSTATS
        val metrics = bStats.make(this, 31651)
        
        // BEGIN VERSION CHECKER
        VelocityVersionUpdateListener(server, this) { audience ->
            scheduleAsync {
                val changes = VersionChecker.check(Commons.slug).currentVersionDiff(server).getOrThrow()
                changes.onChanges {
                    audience.sendNewerVersionMessage(this)
                }
            }
        }
    }
    
    @Subscribe(priority = 1)
    private fun onProxyPing(event: ProxyPingEvent) {
        event.ping = event.ping.asBuilder()
            .description(Configuration.MOTD)
            .build()
    }
}