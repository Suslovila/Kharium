package com.suslovila.kharium.common


import com.suslovila.kharium.utils.config.Config
import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.kharium.common.event.*
import com.suslovila.kharium.common.item.ItemPortableAspectContainer
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.research.KhariumAspect
import com.suslovila.kharium.research.AntiCraftResearchRegistry
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler
import com.suslovila.kharium.utils.config.ConfigPortableContainer
import com.suslovila.kharium.utils.config.ConfigImlants
import com.suslovila.kharium.utils.config.ConfigWitchery
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuContainer
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuSnare
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge


open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        KhariumPacketHandler.init()
        Config.registerServerConfig(event.suggestedConfigurationFile)
        ConfigWitchery.registerServerConfig(event.suggestedConfigurationFile)
        ConfigImlants.registerServerConfig(event.suggestedConfigurationFile)
        ConfigKharuSnare.registerServerConfig(event.suggestedConfigurationFile)
        ConfigKharuContainer.registerServerConfig(event.suggestedConfigurationFile)
        ConfigPortableContainer.registerServerConfig(event.suggestedConfigurationFile)

        FMLCommonHandler.instance().bus().register(FMLEventListener)
        MinecraftForge.EVENT_BUS.register(FMLEventListener)

        FMLCommonHandler.instance().bus().register(Icons)
        MinecraftForge.EVENT_BUS.register(Icons)

        FMLCommonHandler.instance().bus().register(ImplantEvents)
        MinecraftForge.EVENT_BUS.register(ImplantEvents)

        FMLCommonHandler.instance().bus().register(PrimordialExplosionHandler)
        MinecraftForge.EVENT_BUS.register(PrimordialExplosionHandler)

        FMLCommonHandler.instance().bus().register(KharuTickHandler)
        MinecraftForge.EVENT_BUS.register(KharuTickHandler)

        FMLCommonHandler.instance().bus().register(KharuInfluenceHandler)
        MinecraftForge.EVENT_BUS.register(KharuInfluenceHandler)



        MinecraftForge.EVENT_BUS.register(MixinListener)
        MinecraftForge.EVENT_BUS.register(ItemPortableAspectContainer)
        ModBlocks.register()
        ModItems.register()
        KhariumAspect.initAspects()
    }

    fun registerServer(){

    }
    open fun nodeAntiBolt(worldObj: World, x: Float, y: Float, z: Float, x2: Float, y2: Float, z2: Float) {

    }

    open fun init(event: FMLInitializationEvent) {

    }


    open fun postInit(event: FMLPostInitializationEvent) {
        KhariumAspect.initItemsAspects()
        AntiCraftResearchRegistry.integrateInfusion()
        AntiCraftResearchRegistry.integrateResearch()


    }

    open fun registerRenderers() {}


}
