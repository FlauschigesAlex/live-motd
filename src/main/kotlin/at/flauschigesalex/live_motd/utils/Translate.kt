package at.flauschigesalex.live_motd.utils

import com.velocitypowered.api.proxy.Player as ProxiedPlayer
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player as PaperPlayer
import java.util.*

internal object Translate {
    
    fun translate(key: String, locale: Locale): String = runCatching {
        
        require(key.isNotEmpty()) { "Key must not be empty!" }

        val bundle = ResourceBundle.getBundle(
            "i18n/messages",
            locale,
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
        )
        
        return bundle.getString(key)
    }.getOrNull() ?: "?($key)"
}

val Audience.locale: Locale
    get() = runCatching { (this as? ProxiedPlayer)?.effectiveLocale }.getOrNull()
        ?: runCatching { (this as? PaperPlayer)?.locale() }.getOrNull()
        ?: Locale.getDefault()

fun Audience.sendRichMessage(message: String) =
    MiniMessage.miniMessage().deserialize(message).also { this.sendMessage(it) }

fun Audience.sendTranslated(key: String, vararg args: Any?, richConsumer: Audience.(String) -> String = { it }) {
    val translation = Translate.translate(key, this.locale)
    val richTranslation = richConsumer.invoke(this, translation)
    
    val (color1, color2) = Commons.colorGradient.let { it.first.asHexString() to it.second.asHexString() }
    this.sendRichMessage("<dark_gray>[<gradient:${color1}:${color2}>LiveMotd</gradient><dark_gray>] <gray>$richTranslation".format(*args))
}