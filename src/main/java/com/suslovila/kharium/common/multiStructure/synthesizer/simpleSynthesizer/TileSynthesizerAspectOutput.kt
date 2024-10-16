package com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer

import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
import thaumcraft.api.aspects.IEssentiaTransport
import kotlin.math.min
// thaumcraft API is the bad and good at the same time. This tile works a lot like TileEssentiaReservoir
class TileSynthesizerAspectOutput() : TileDefaultMultiStructureElement(), IEssentiaTransport,
    IAspectContainer {
    override val packetId: Int = 0
    //	private val inputs: Array<ForgeDirection> = arrayOf(ForgeDirection.DOWN, ForgeDirection.UP)

    private var essentia: AspectList = AspectList()

    val capacity: Int
        get() {
            return 64
        }

    // We just do not give a fuck about directions because we have a multistructure
    override fun isConnectable(face: ForgeDirection): Boolean =
        true

    fun hasEmptySpace() : Boolean = essentia.visSize() < capacity
    override fun canInputFrom(face: ForgeDirection) =
        true


    override fun canOutputTo(face: ForgeDirection) =
        true

    override fun setSuction(aspect: Aspect, i: Int) {
    }

    override fun getSuctionType(face: ForgeDirection): Aspect? = null


    override fun getSuctionAmount(forgeDirection: ForgeDirection): Int = /* 156 */
        if (essentia.visSize() < capacity) 24 else 0


    //TAKE FROM THIIIS CONTAINER!!!!!!!!!!!!
    override fun takeEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
        if (canOutputTo(face) && takeFromContainer(aspect, amount)) amount else 0

    override fun addEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int {
        return if (canInputFrom(face)) amount - addToContainer(aspect, amount) else 0
    }
// I just do not know what a hell does some this functions do, I am just copying from thaumcraft with hope everything will work properly
    override fun getEssentiaType(direction: ForgeDirection) =
        if (essentia.visSize() > 0 && direction == ForgeDirection.UNKNOWN) essentia.getAspects()[0] else null
    override fun getEssentiaAmount(forgeDirection: ForgeDirection) = this.essentia.visSize()


    override fun getMinimumSuction(): Int = 0

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
        markForSaveAndSync()
        return leftAmount
    }

    override fun takeFromContainer(aspect: Aspect, am: Int): Boolean {
        if (essentia.getAmount(aspect) >= am) {
            essentia.remove(aspect, am)
            markDirty()
            markForSaveAndSync()
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

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        essentia.readFromNBT(nbttagcompound)
    }

}