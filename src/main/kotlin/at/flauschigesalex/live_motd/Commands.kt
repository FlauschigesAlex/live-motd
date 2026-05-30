package at.flauschigesalex.live_motd

import at.flauschigesalex.lib.minecraft.brigadier.CommandBuilder
import at.flauschigesalex.lib.minecraft.brigadier.types.internal.GreedyArgumentType
import at.flauschigesalex.lib.minecraft.brigadier.types.internal.LiteralArgumentType
import at.flauschigesalex.lib.minecraft.brigadier.types.primitive.StringArgumentType
import at.flauschigesalex.live_motd.utils.Commons
import at.flauschigesalex.live_motd.utils.Commons.MAX_CHARACTER_AMOUNT
import at.flauschigesalex.live_motd.utils.Commons.MAX_LINE_AMOUNT
import at.flauschigesalex.live_motd.utils.Permissions
import at.flauschigesalex.live_motd.utils.sendTranslated
import at.flauschigesalex.live_motd.utils.validate
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

internal object Commands {
    
    init {
        CommandBuilder("live-motd") {
            this.alias("motd", "message-of-the-day")
            this.permission(Permissions.MOTD_COMMAND)
            
            this.argument("--reload-config", LiteralArgumentType.literal().alias("--reload")) {
                this.execute { context -> 
                    val sender = context.sender
                    
                    Configuration.reload()
                    sender.sendTranslated("motd.reload.config")
                }
            }
            this.argument("motd", MOTDArgumentType) {
                this.execute { context ->
                    val sender = context.sender
                    
                    val newMOTD = context.arguments.greedyByType<String>("motd")?.value?.joinToString(" ")?.validate()?.trim()
                        ?: return@execute

                    Configuration.richMOTD = newMOTD
                    
                    val formatted = "\n<reset>${newMOTD}"
                    sender.sendTranslated("motd.set.new", formatted) {
                        "<gray>$it"
                    }
                    
                    val split = newMOTD.split("<newline>")

                    if (split.any { it.length > MAX_CHARACTER_AMOUNT }) {
                        sender.playSound(Sound.sound(Key.key("entity.pillager.hurt"), Sound.Source.MASTER, 1f, 1f))
                        sender.sendTranslated("motd.warn.maxchars", MAX_CHARACTER_AMOUNT) {
                            "<light_purple>$it"
                        }
                    } else if (split.size > MAX_LINE_AMOUNT) {
                        sender.playSound(Sound.sound(Key.key("entity.pillager.hurt"), Sound.Source.MASTER, 1f, 1f))
                        sender.sendTranslated("motd.warn.maxlines", MAX_LINE_AMOUNT) {
                            "<light_purple>$it"
                        }
                    }
                    
                    Configuration.saveConfig(true)
                }
            }
            
            this.execute { context ->
                val sender = context.sender
                val base = context.fullCommand.split(" ").first()
                
                val motd = "\n<reset>${Configuration.richMOTD}"
                
                sender.sendTranslated("motd.current", motd)
                
                val modifyCommand = "/$base <motd>"
                val inlineCommand = "/$base ${Configuration.richMOTD}"
                
                val (color1, color2) = Commons.colorGradient.let { it.first.asHexString() to it.second.asHexString() }
                val component = "<dark_gray>'<click:suggest_command:'${inlineCommand}'><hover:show_text:'<gradient:${color1}:${color2}>$modifyCommand'><gradient:${color1}:${color2}>${modifyCommand}</gradient></hover></click><dark_gray>'"
                sender.sendTranslated("motd.set", component)
            }
        }
    }
}

private object MOTDArgumentType : GreedyArgumentType<String, StringArgumentType>(StringArgumentType.string()) {
    
    override fun defaultChatSuggestions(provided: String, sender: Audience): List<String> =
        listOf(Configuration.richMOTD)

    override val priority: Int = 1
}