package at.flauschigesalex.live_motd.utils

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.rinth.version.ProjectVersionDiff
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.format.TextColor

internal object Commons {

    const val slug = "live-motd"
    val colorGradient: Pair<TextColor, TextColor> = Pair(TextColor.fromHexString("#8D02FB")!!, TextColor.fromHexString("#EB00FF")!!)
    
    val MAX_LINE_AMOUNT = 2
    val MAX_CHARACTER_AMOUNT = 50

    lateinit var dataFolder: FileManager
}

internal fun Audience.sendNewerVersionMessage(changes: ProjectVersionDiff) {
    this.sendTranslated("version.update.line1", changes.newer.slug, "<gold>${changes.newer.version}</gold>") { "<yellow>$it" }
    this.sendTranslated("version.update.line2", "<red>${changes.older.version}</red>", "<yellow>${changes.indexDifference}</yellow>")
    this.sendTranslated("version.update.line3", "<green><u><click:open_url:'${changes.newer.downloadUrl}'>${changes.newer.downloadUrl}</click></u></green>")
}