package com.suslovila.kharium.integration

import com.emoniph.witchery.util.Log
import cpw.mods.fml.common.Loader
import net.minecraft.entity.EntityLivingBase

abstract class ModHook {
    var initialized = false
    abstract val modID: String

    fun init() {
        initialized = Loader.isModLoaded(
            modID
        )
        if (initialized) {
            doInit()
            Log.instance().debug(
                String.format(
                    "Mod: %s support initialized", *arrayOf<Any>(
                        modID
                    )
                )
            )
        } else {
            Log.instance().debug(
                String.format(
                    "Mod: %s not found", *arrayOf<Any>(
                        modID
                    )
                )
            )
        }
    }

    protected abstract fun doInit()
    fun postInit() {
        if (initialized) {
            doPostInit()
            Log.instance().debug(
                String.format(
                    "Mod: %s support post initialized", *arrayOf<Any>(
                        modID
                    )
                )
            )
        }
    }

    protected abstract fun doPostInit()
    fun tickKharuInfluence(entity: EntityLivingBase?) {
        if (initialized) {
            reducePlayerMagicPower(entity)
        }
    }

    // WARNING!!!!!!!!!!! DO NOT CALL "getOrCreateTag" on items whose maxStackSize is not 1 (stacking problems)
    protected abstract fun reducePlayerMagicPower(entity: EntityLivingBase?)
}

