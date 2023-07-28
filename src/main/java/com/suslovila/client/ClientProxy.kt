package com.suslovila.client

import com.suslovila.client.particles.antiNodeBolt.AntiNodeBolt
import com.suslovila.client.render.ClientEventHandler
import com.suslovila.client.render.block.BlockEssentiaReservoirVoidRenderer
import com.suslovila.client.render.item.ItemAntiNodeRenderer
import com.suslovila.client.render.tile.TileAntiNodeRenderer
import com.suslovila.client.render.tile.TileEssentiaReservoirVoidRenderer
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeControllerBaseRenderer
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeWatcherRenderer
import com.suslovila.common.CommonProxy
import com.suslovila.common.block.ModBlocks
import com.suslovila.common.block.tileEntity.TileAntiNode
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase
import com.suslovila.common.item.ItemCrystallizedAntiMatterRenderer
import com.suslovila.common.item.ModItems
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.world.World
import net.minecraftforge.client.IItemRenderer
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
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        ModBlocks.registerRender()
        setupItemRenderers()

        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNode::class.java, TileAntiNodeRenderer())
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaReservoirVoid::class.java, TileEssentiaReservoirVoidRenderer())
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNodeControllerBase::class.java, TileAntiNodeControllerBaseRenderer())
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNodeWatcher::class.java, TileAntiNodeWatcherRenderer())


        RenderingRegistry.registerBlockHandler(BlockEssentiaReservoirVoidRenderer())

    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
    }

    override fun registerRenderers() {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler())
        MinecraftForgeClient.registerItemRenderer(ModItems.antiMatter, ItemCrystallizedAntiMatterRenderer)
    }

    private fun setupItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.ANTI_NODE), ItemAntiNodeRenderer())
    }

    override fun getServerGuiElement(guiId: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? { return null }

    override fun getClientGuiElement(guiId: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? { return null }

}