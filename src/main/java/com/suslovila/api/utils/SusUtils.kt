package com.suslovila.api.utils

import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import org.lwjgl.opengl.GL11.glTranslated
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete
import java.awt.Color
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.Pair

object SusUtils {
    //some static fields and functions to make life easier ;)

    var random = Random()
    const val aspectReducePerTick = 3
    const val antiNodeAppearanceTime = 120
    const val humilitasColor = 16727457
    fun glTranslateRandomWithEqualD(factor: Double) =
        glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor))


    fun nextDouble(n1: Double, n2: Double) = ThreadLocalRandom.current().nextDouble(n1, n2)


    fun nextDouble(n1: Double): Double =
        if (Math.abs(n1) == 0.0) 0.0 else ThreadLocalRandom.current().nextDouble(-n1, n1)


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

    fun completeResearchSilently(researchName: String?, player: EntityPlayer?) =
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)


    fun ItemStack.getOrCreateTag(): NBTTagCompound {
        if (!hasTagCompound()) tagCompound = NBTTagCompound()
        return tagCompound
    }

    enum class BasicDirection(val vec3: SusVec3) {
        NORTH(SusVec3(0, 0, -1)),
        SOUTH(SusVec3(0, 0, 1)),
        EAST(SusVec3(1, 0, 0)),
        WEST(SusVec3(-1, 0, 0)),
        UP(SusVec3(0, 1, 0)),
        DOWN(SusVec3(0, -1, 0))

    }

@JvmStatic
fun getCordSystemFromVec3(vec3: SusVec3) : ArrayList<SusVec3> {
    val orthogonal = getOrthogonalVec3(vec3)
    return arrayListOf(vec3.cross(orthogonal).normalize(), vec3.normalize(), orthogonal.normalize())
}

    fun <T> ArrayList<T>.setAndGet(pos : Int, element : T) : ArrayList<T> {
        this[pos] = element
        return this
    }
    fun <T> ArrayList<T>.addAndGet(element : T) : ArrayList<T> {
        this.add(element)
        return this
    }
    @JvmStatic
    fun getOrthogonalVec3(vec3 : SusVec3) : SusVec3 {
        val orthogonal : SusVec3
        val vec3Cords = vec3.cordsAsList
        //Int - the "pos" bounded to x,y,z
        //Double - the value
        val vec3CordsWithIndexes : ArrayList<Pair<Int, Double>>  = arrayListOf()
        for(i in vec3Cords.indices){
            vec3CordsWithIndexes.add(Pair(i, vec3Cords[i]))
        }
        val notNullVec3Cords = vec3CordsWithIndexes.filter { it.second != 0.0 }
        when(notNullVec3Cords.size) {
            3 -> orthogonal = SusVec3(-vec3.z, 0.0, vec3.x)
            2 -> {
                with(notNullVec3Cords) {
                    //swaps the 2 cords of vec3 and makes one of them opposite. The last 3rd cord stays zero. Now vec3 DOT orthogonal = 0 ---> there is angle 90 degrees between them
                    orthogonal = SusVec3.getVectorFromArrayList(arrayListOf(0.0,0.0,0.0).setAndGet(this[1].first, this[0].second).setAndGet(this[0].first, -this[1].second))
                }
            }
            // if only 1 cord is non-zero - we just put it to another's one place (x becomes y, y becomes z, z becomes x), and the other cords stay zero
            1 -> orthogonal = SusVec3.getVectorFromArrayList(arrayListOf(0.0,0.0,0.0).setAndGet((notNullVec3Cords[0].first + 1) % 3, notNullVec3Cords[0].second))

            else -> orthogonal = SusVec3(0.0,0.0,0.0)
        }
        return orthogonal
    }


    fun bindColor(tessellator: Tessellator, color : Int, alpha : Float, fadeFactor : Float) {
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green/ 255.0f
        val b = co.blue / 255.0f
        tessellator.setColorRGBA_F(
            r * fadeFactor,
            g * fadeFactor,
            b * fadeFactor,
            alpha
        )
    }
    fun rotateZ(pos : SusVec3, angle : Double) : SusVec3 {
        val radAngle = angle / 180 * Math.PI
        with(pos){
            return SusVec3(x * cos(radAngle) + y * sin(radAngle), y * cos(radAngle) - x * sin(radAngle), z)
        }
    }
    fun rotateX(pos : SusVec3, angle : Double) : SusVec3 {
        val radAngle = angle / 180 * Math.PI
        with(pos){
            return SusVec3(x, y * cos(radAngle) - z * sin(radAngle), z * cos(radAngle) + y * sin(radAngle))
        }
    }
    fun rotateY(pos : SusVec3, angle : Double) : SusVec3 {
        val radAngle = angle / 180 * Math.PI
        with(pos){
            return SusVec3(x * cos(radAngle) + z * sin(radAngle), y, z * cos(radAngle) - x * sin(radAngle))
        }
    }
}

//basic functions, take angle in radians
fun sin(value : Double) = MathHelper.sin(value.toFloat())
fun cos(value: Double) = MathHelper.cos(value.toFloat())


