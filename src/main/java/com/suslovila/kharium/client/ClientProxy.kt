package com.suslovila.kharium.client


import com.suslovila.kharium.client.clientProcess.ClientProcessHandler
import com.suslovila.kharium.client.gui.GuiImplants
import com.suslovila.kharium.client.implantInfluence.IllusionRenderer
import com.suslovila.kharium.client.particles.antiNodeBolt.AntiNodeBolt
import com.suslovila.kharium.client.render.ClientEventHandler
import com.suslovila.kharium.client.render.block.BlockEssentiaReservoirVoidRenderer
import com.suslovila.kharium.client.render.item.ItemAntiNodeRenderer
import com.suslovila.kharium.client.render.item.ItemCrystallizedAntiMatterRenderer
import com.suslovila.kharium.client.render.item.ItemSpaceDividerRenderer
import com.suslovila.kharium.client.render.tile.RestrainGlassRenderer
import com.suslovila.kharium.client.render.tile.TileEssentiaReservoirVoidRenderer
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.rune.TileRuneRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.TileAntiNodeRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.TileAntiNodeWatcherRenderer
import com.suslovila.kharium.common.CommonProxy
import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.kharium.common.block.tileEntity.*
import com.suslovila.kharium.common.block.tileEntity.rune.TileStabiliserRune
import com.suslovila.kharium.common.item.ItemSpaceDivider
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.world.World
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.MinecraftForge


class ClientProxy : CommonProxy(), IGuiHandler {
    override fun nodeAntiBolt(worldObj: World, x: Float, y: Float, z: Float, x2: Float, y2: Float, z2: Float) {
        val bolt = AntiNodeBolt(worldObj, x.toDouble(), y.toDouble(), z.toDouble(), x2.toDouble(), y2.toDouble(), z2.toDouble(), worldObj.rand.nextLong(), 10, 3.0f, 5)
        bolt.defaultFractal()
        bolt.setType(0)
        bolt.finalizeBolt()
    }

    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)

        FMLCommonHandler.instance().bus().register(GuiImplants)
        MinecraftForge.EVENT_BUS.register(GuiImplants)

        FMLCommonHandler.instance().bus().register(IllusionRenderer)
        MinecraftForge.EVENT_BUS.register(IllusionRenderer)

        FMLCommonHandler.instance().bus().register(ClientProcessHandler)
        MinecraftForge.EVENT_BUS.register(ClientProcessHandler)
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        ModBlocks.registerRender()
        setupItemRenderers()

        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNode::class.java, TileAntiNodeRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaReservoirVoid::class.java, TileEssentiaReservoirVoidRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNodeWatcher::class.java, TileAntiNodeWatcherRenderer)
        RenderingRegistry.registerBlockHandler(BlockEssentiaReservoirVoidRenderer())

//        ClientRegistry.bindTileEntitySpecialRenderer(TileKharuReactor::class.java, TileKharuReactorRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileKharuSnare::class.java, TileKharuSnareRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileRestrainedGlass::class.java, RestrainGlassRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileStabiliserRune::class.java, TileRuneRenderer)

        KeyHandler.register()
    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
    }

    override fun registerRenderers() {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler)
        MinecraftForgeClient.registerItemRenderer(ModItems.crystallizedKharu, ItemCrystallizedAntiMatterRenderer)
        MinecraftForgeClient.registerItemRenderer(ItemSpaceDivider, ItemSpaceDividerRenderer)

    }

    private fun setupItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.ANTI_NODE), ItemAntiNodeRenderer())
    }

    override fun getServerGuiElement(guiId: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? { return null }

    override fun getClientGuiElement(guiId: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? { return null }

}