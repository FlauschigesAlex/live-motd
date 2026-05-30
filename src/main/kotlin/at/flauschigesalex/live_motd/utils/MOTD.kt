package at.flauschigesalex.live_motd.utils

fun String.validate(): String = this
    .replace("\n", "<newline>")