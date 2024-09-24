package com.suslovila.kharium.common.multiStructure.synthesizer

import com.suslovila.kharium.Kharium
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
import thaumcraft.api.aspects.IEssentiaTransport
import kotlin.math.min


// really works the same as EssentiaReservoir, accepts all aspect types
class TileSynthesizerAspectInput() : TileDefaultMultiStructureElement(), IEssentiaTransport, IAspectContainer {
    override val packetId: Int = 0
    companion object {
        val COMPONENT_INDEX_NBT = Kharium.prefixAppender.doAndGet("componentIndex")
    }

    var essentia: AspectList = AspectList()
//    var correspondingComponentIndex = 0
    val capacity: Int
        get() {
            return 64
        }
// I hope it will not cause NPE -_-
    val connectedSynthesizerCore: TileSynthesizerCore
        get() {
            return world.getTile(this.getMasterPos()) as TileSynthesizerCore
        }

//    val requiredAspect: Aspect?
//        get() {
//            return connectedSynthesizerCore.currentProducingAspect?.components?.get(correspondingComponentIndex)
//        }
    /* if you ask "what a hell is this???" :
    First of all, to achieve my goals, I need to build the structure in world facing NORTH and then give it spinning checks (0, 90, 180 270 degrees)
    I want the tube to connect only to the "back" of Synthesizer. So I need to connect spinning with real world situation. To do this, I need to know
    how make a "forgeFirection facing" from spin angle. To do this, when I write structure to file, I MUST MAKE it facing NORTH in world (but in code
    it will have UP facing to make spinning around UP vector), and now I know what is default forgeDirection for zero angle. Now if I have,
    for example, 90 angle, I know that the start is NORTH, so 90 angle from it is EAST. That's how I get forgeDirection real structure facing from spinning
    FOR Synthesizer.
    I know all this sounds really bad and like a mess, but that is how it works
     */
    val structureForgeDirection: ForgeDirection
        get() {
            val angles = arrayListOf(0, 90, 180, 270)
            val directions = arrayListOf(ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.SOUTH, ForgeDirection.EAST)
            val index = angles.indexOf(structureRotationAngle)
            return directions[index]
        }

    // We just do not give a fuck about directions because we have a multistructure
    override fun isConnectable(face: ForgeDirection): Boolean =
        true

    override fun canInputFrom(face: ForgeDirection) =
        structureForgeDirection == face.opposite


    override fun canOutputTo(face: ForgeDirection) =
        true

    override fun setSuction(aspect: Aspect, i: Int) {
    }

    override fun getSuctionType(face: ForgeDirection): Aspect? = null


    override fun getSuctionAmount(forgeDirection: ForgeDirection): Int =
        if (essentia.visSize() < capacity) 24 else 0


    //TAKE FROM THIIIS CONTAINER!!!!!!!!!!!!
    override fun takeEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
        if (canOutputTo(face) && takeFromContainer(aspect, amount)) amount else 0

    override fun addEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int {
        return if (canInputFrom(face)) amount - addToContainer(aspect, amount) else 0
    }
    // I just do not know what a hell does some this functions do, I am just copying from thaumcraft with hope everything will work properly
    override fun getEssentiaType(direction: ForgeDirection) = essentia.getAspects().firstOrNull()
    override fun getEssentiaAmount(forgeDirection: ForgeDirection) = this.essentia.visSize()

    // also just copying from EssentiaReservoir :(
    override fun getMinimumSuction(): Int = 24

    override fun renderExtendedTube(): Boolean = false
    override fun getAspects(): AspectList = essentia

//		AspectList().add(this.aspectOut, 1).add(this.aspectIn)

    override fun setAspects(aspects: AspectList) {
        this.essentia = aspects.copy()
    }

    override fun doesContainerAccept(aspect: Aspect): Boolean = true

    // returns amount that was left
    override fun addToContainer(aspect: Aspect, amount: Int): Int {
        var leftAmount = amount
        val emptySpace = capacity - essentia.visSize()
        val toPut = min(emptySpace, amount)
        essentia.add(aspect, toPut)
        leftAmount -= toPut
        markDirty()
        markForSync()
        return leftAmount
    }

    override fun takeFromContainer(aspect: Aspect, am: Int): Boolean {
        if (essentia.getAmount(aspect) >= am) {
            essentia.remove(aspect, am)
            markDirty()
            markForSync()
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun takeFromContainer(aspectList: AspectList): Boolean = false

    override fun doesContainerContainAmount(aspect: Aspect, amount: Int): Boolean =
        aspects.getAmount(aspect) >= amount

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun doesContainerContain(requiredAspects: AspectList): Boolean {
        for (aspectType in requiredAspects.getAspects()) {
            if (this.essentia.getAmount(aspectType) < requiredAspects.getAmount(aspectType)) {
                return false
            }
        }

        return true
    }

    override fun containerContains(aspect: Aspect) =
        essentia.getAmount(aspect)


    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        essentia.writeToNBT(nbttagcompound)
//        nbttagcompound.setInteger(COMPONENT_INDEX_NBT, correspondingComponentIndex)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        essentia.readFromNBT(nbttagcompound)
//        correspondingComponentIndex = nbttagcompound.getInteger(COMPONENT_INDEX_NBT)
    }

}