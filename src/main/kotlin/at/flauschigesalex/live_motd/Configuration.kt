package at.flauschigesalex.live_motd

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.lib.base.file.JsonManager
import at.flauschigesalex.lib.base.file.ResourceManager
import at.flauschigesalex.lib.base.file.readJson
import at.flauschigesalex.live_motd.utils.Commons
import at.flauschigesalex.live_motd.utils.scheduleAsync
import at.flauschigesalex.live_motd.utils.validate
import kotlinx.serialization.KSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Configuration {
    internal const val VERSION = 1

    private val file = FileManager(Commons.dataFolder, "config.json")
    private var json: JsonManager

    init {
        this.attemptCreateConfig()
        this.json = file.readJson() ?: JsonManager()
    }
    
    internal fun reload() {
        this.json = file.readJson() ?: this.json
    }

    @Deprecated("Internal")
    internal val configVersion: Int = json.getInt("_version") ?: 1
    
    var richMOTD: String
        get() = json.getString("richMOTD")?.validate()
            ?: "<gray>Just another minecraft server."
        set(value) {
            json.put("richMOTD", value.validate())
        }
    
    val MOTD: Component
        get() = MiniMessage.miniMessage().deserialize(richMOTD.validate())

    fun saveConfig(saveAsync: Boolean) {
        if (json.isOriginalContent()) return

        if (saveAsync) {
            scheduleAsync {
                this@Configuration.saveConfig(false)
            }
            
            return
        }
            
        file.createFile()
        file.write(json)
    }

    private fun attemptCreateConfig() {
        if (file.file.isDirectory)
            file.delete()

        if (file.exists) return
        file.createFile()

        ResourceManager("default-config.json")?.let { default ->
            json = default.readJson() ?: return@let null
            file.write(json)
            return@let json
        } ?: JsonManager()

        json.putIfAbsent("_version", VERSION)
        file.write(json)
    }
}