package com.suslovila.utils

import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.aspects.AspectList
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.function.BiConsumer

object SUSUtils {
    //some static fields and functions to make life easier
    var random = Random()
    const val halfConvertionTime = 120
    const val himilitasColor = 16727457
    fun glTranslateRandomEqualD(factor: Double) = glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor))


    fun nextDouble(n1: Double, n2: Double) = ThreadLocalRandom.current().nextDouble(n1, n2)


    fun nextDouble(n1: Double) : Double = if (n1 == 0.0) 0.0 else ThreadLocalRandom.current().nextDouble(-n1, n1)


    fun wrapDegrees(p_14178_: Float): Float {
        var f = p_14178_ % 360.0f
        if (f >= 180.0f) {
            f -= 360.0f
        }
        if (f < -180.0f) {
            f += 360.0f
        }
        return f
    }

    fun getAspectList(stack: ItemStack?) = ThaumcraftApiHelper.getObjectAspects(stack) ?: AspectList()


    fun randomSign() = if (random.nextBoolean()) 1 else -1

    fun completeNormalResearch(researchName: String?, player: EntityPlayer?, world: World) {
        PacketHandler.INSTANCE.sendTo(PacketResearchComplete(researchName), player as EntityPlayerMP?)
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)
        world.playSoundAtEntity(player, "thaumcraft:learn", 0.75f, 1.0f)
    }

    fun getOrCreateTag(itemStack: ItemStack): NBTTagCompound {
        if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()
        return itemStack.tagCompound
    }


}
