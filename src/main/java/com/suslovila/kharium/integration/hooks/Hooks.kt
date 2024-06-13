package com.suslovila.kharium.integration.hooks

import com.suslovila.kharium.integration.ModHook

object Hooks {
    val hooks = mutableSetOf<ModHook>()

    fun register(hook: ModHook) {
        hooks.add(hook)
        hook.init()
    }
}