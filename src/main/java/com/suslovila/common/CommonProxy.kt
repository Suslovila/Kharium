package com.suslovila.common


import com.suslovila.Config
import com.suslovila.common.block.ModBlocks
import com.suslovila.common.event.FMLEventListener
import com.suslovila.common.event.MixinListener
import com.suslovila.common.item.ModItems
import com.suslovila.research.ACAspect
import com.suslovila.research.AntiCraftResearchRegistry
import com.suslovila.common.event.PrimordialExplosionHandler
import com.suslovila.common.sync.PacketHandler
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge


open class CommonProxy {
    open fun preInit(event : FMLPreInitializationEvent) {
		Config.registerServerConfig(event.suggestedConfigurationFile)
        FMLCommonHandler.instance().bus().register(FMLEventListener)
        MinecraftForge.EVENT_BUS.register(FMLEventListener)
        FMLCommonHandler.instance().bus().register(PrimordialExplosionHandler)
        MinecraftForge.EVENT_BUS.register(PrimordialExplosionHandler)
        MinecraftForge.EVENT_BUS.register(MixinListener)
        ModBlocks.register()
        ModItems.register()
        ACAspect.initAspects()
        PacketHandler.init()
    }

    open fun nodeAntiBolt(worldObj : World, x: Float, y : Float, z : Float, x2 : Float, y2 : Float, z2 : Float) {

    }

    open fun init(event : FMLInitializationEvent) {

    }


    open fun postInit(event : FMLPostInitializationEvent) {
        ACAspect.initItemsAspects()
        AntiCraftResearchRegistry.integrateInfusion()
        AntiCraftResearchRegistry.integrateResearch()
    }

    open fun registerRenderers() {}


}
