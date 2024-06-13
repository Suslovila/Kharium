package com.suslovila.kharium.common


import com.suslovila.kharium.utils.config.Config
import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.kharium.common.event.FMLEventListener
import com.suslovila.kharium.common.event.KharuTickHandler
import com.suslovila.kharium.common.event.MixinListener
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.research.ACAspect
import com.suslovila.kharium.research.AntiCraftResearchRegistry
import com.suslovila.kharium.common.event.PrimordialExplosionHandler
import com.suslovila.kharium.common.sync.PacketHandler
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler
import com.suslovila.kharium.utils.config.ConfigWitchery
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge


open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        Config.registerServerConfig(event.suggestedConfigurationFile)
        ConfigWitchery.registerServerConfig(event.suggestedConfigurationFile)

        FMLCommonHandler.instance().bus().register(FMLEventListener)
        MinecraftForge.EVENT_BUS.register(FMLEventListener)

        FMLCommonHandler.instance().bus().register(PrimordialExplosionHandler)
        MinecraftForge.EVENT_BUS.register(PrimordialExplosionHandler)

        FMLCommonHandler.instance().bus().register(KharuTickHandler)
        MinecraftForge.EVENT_BUS.register(KharuTickHandler)

        FMLCommonHandler.instance().bus().register(KharuInfluenceHandler)
        MinecraftForge.EVENT_BUS.register(KharuInfluenceHandler)

        MinecraftForge.EVENT_BUS.register(MixinListener)
        ModBlocks.register()
        ModItems.register()
        ACAspect.initAspects()
        PacketHandler.init()
    }

    open fun nodeAntiBolt(worldObj: World, x: Float, y: Float, z: Float, x2: Float, y2: Float, z2: Float) {

    }

    open fun init(event: FMLInitializationEvent) {

    }


    open fun postInit(event: FMLPostInitializationEvent) {
        ACAspect.initItemsAspects()
        AntiCraftResearchRegistry.integrateInfusion()
        AntiCraftResearchRegistry.integrateResearch()
    }

    open fun registerRenderers() {}


}
