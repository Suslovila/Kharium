package com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.ThaumcraftIntegrator.compositionAmountToAspect
import com.suslovila.kharium.utils.ThaumcraftIntegrator.tryTakeFromContainers
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

class TileSynthesizerCore() : TileDefaultMultiStructureElement() {
    override val packetId: Int = 0
    var currentProducingAspect: Aspect? = Aspect.ENERGY

    // default values are 0, 0, 0
    // represents real-world position
    // are set when constructing
    var tileAspectInputPositions = Array(2) { i -> Position(0, 0, 0) }
    var tileKharuInputPosition = Position(0, 0, 0)
    var tileAspectOutPutPosition = Position(0, 0, 0)

    var synthesizeTimeLeft = 0
    val requiredTime: Int
        get() {
            return currentProducingAspect?.let { compositionAmountToAspect[currentProducingAspect] } ?: 0
        }

    companion object {
        val currentProducingAspectNbt = Kharium.prefixAppender.doAndGet("currentProducingAspect")
    }

    fun isSynthesizing() = synthesizeTimeLeft != 0
    override fun writeCustomNBT(rootNbt: NBTTagCompound) {
        super.writeCustomNBT(rootNbt)
        currentProducingAspect?.let { rootNbt.setString(currentProducingAspectNbt, it.tag) }

        val tagInputs = Array(2) { i -> NBTTagCompound() }
        tileAspectInputPositions.forEachIndexed { index, position -> position.writeTo(tagInputs[index]) }
        tagInputs.forEachIndexed { index, posTag -> rootNbt.setTag("aspectInput$index", posTag) }

        val tileKharuTag = NBTTagCompound()
        tileKharuInputPosition.writeTo(tileKharuTag)
        rootNbt.setTag("kharuInput", tileKharuTag)


        val tileAspectOutput = NBTTagCompound()
        tileAspectOutPutPosition.writeTo(tileAspectOutput)
        rootNbt.setTag("aspectOutput", tileAspectOutput)

        rootNbt.setInteger("progress", synthesizeTimeLeft)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        if (nbttagcompound.hasKey(currentProducingAspectNbt)) {
            currentProducingAspect = Aspect.getAspect(nbttagcompound.getString(currentProducingAspectNbt))
        }


        arrayOf(0, 1).forEach { index ->
            val posTag = nbttagcompound.getCompoundTag("aspectInput$index")
            tileAspectInputPositions[index] = Position.readFrom(posTag)
        }

        val tileKharuTag = nbttagcompound.getCompoundTag("kharuInput")
        tileKharuInputPosition = Position.readFrom(tileKharuTag)

        val tileOutput = nbttagcompound.getCompoundTag("aspectOutput")
        tileAspectOutPutPosition = Position.readFrom(tileOutput)

        synthesizeTimeLeft = nbttagcompound.getInteger("progress")
    }

    override fun updateEntity() {
        super.updateEntity()
        if (!world.isRemote) {
            val synthesizingWasStopped = validateSynthesizing()
            if (!synthesizingWasStopped) {
                tryStartSynthesizing()
                tickSynthesizing()
            }

        }

    }

    private fun validateSynthesizing(): Boolean {
        if (currentProducingAspect == null) {
            synthesizeTimeLeft = 0
            return true
        }
        (world.getTile(tileAspectOutPutPosition) as? TileSynthesizerAspectOutput)?.let { output ->
            if (!output.hasEmptySpace()) return true
        }

        return false
    }

    private fun tickSynthesizing() {
        if (isSynthesizing()) {
            synthesizeTimeLeft -= 1
            markForSave()

            val hasCompleted = synthesizeTimeLeft == 0
            if (hasCompleted) {
                (world.getTile(tileAspectOutPutPosition) as? TileSynthesizerAspectOutput)?.let { output ->
                    currentProducingAspect?.let { aspect ->
                        output.addToContainer(aspect, 1)
                    }
                }
            }
        }
    }

    private fun tryStartSynthesizing() {
        if (!isSynthesizing()) {
            val currentAspect = currentProducingAspect
            if (currentAspect != null) {
                val requiredAspects = AspectList().apply {
                    add(currentAspect.components[0], 5)
                    add(currentAspect.components[1], 5)
                }
                val hasEnoughAspects =
                    tryTakeFromContainers(
                        aspectHolders = tileAspectInputPositions.mapNotNull { position -> world.getTile(position) as? TileSynthesizerAspectInput },
                        aspects = requiredAspects,
                        simulate = true
                    )

                if (hasEnoughAspects) {
                    synthesizeTimeLeft = compositionAmountToAspect[currentAspect]!! * 20
                    tryTakeFromContainers(
                        aspectHolders = tileAspectInputPositions.mapNotNull { position -> world.getTile(position) as? TileSynthesizerAspectInput },
                        aspects = requiredAspects,
                        simulate = false
                    )
                }
            }
        }
    }
}

