package at.flauschigesalex.live_motd.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val scope = SupervisorJob() + Dispatchers.IO
private val context = CoroutineScope(scope)

internal fun scheduleAsync(block: suspend CoroutineScope.() -> Unit): Result<Unit> = runCatching {
    context.launch { block() }
}.onFailure { it.printStackTrace() }.map {}