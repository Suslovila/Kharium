package com.suslovila.kharium.common.multiStructure.javaExample;



import com.suslovila.kharium.Kharium;
import com.suslovila.kharium.common.multiStructure.TileFilling;
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerAspectInput;
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerAspectOutput;
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerCore;
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerKharuInput;
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure;
import com.suslovila.sus_multi_blocked.api.multiblock.VALIDATION_TYPE;
import com.suslovila.sus_multi_blocked.utils.Position;
import com.suslovila.sus_multi_blocked.utils.SusVec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MultiStructureSynthesizer extends MultiStructure<SynthesizerAdditionalData, SynthesizerElement> {
    // Singletoning

    public static final MultiStructureSynthesizer INSTANCE = new MultiStructureSynthesizer();

    // so look, depending on set and stored value in world when writing to file, now, using block meta, I can control which tile entity should live in each part of multistructure
    // what to do with different tileEntities you should decide on your own
    public final List<Supplier<TileEntity>> possibleTilesByMeta = Arrays.asList(
            TileFilling::new,
            TileSynthesizerAspectInput::new,
            TileSynthesizerKharuInput::new,
            TileSynthesizerCore::new,
            TileSynthesizerAspectOutput::new
    );

    private MultiStructureSynthesizer() {
        super("/assets/" + Kharium.MOD_ID + "/structures/synthesizer.json",
                SynthesizerElement.class,
                new ArrayList<>(Collections.singletonList(ForgeDirection.UP)),
                true,
                VALIDATION_TYPE.EACH_BLOCK);
    }

    @Override
    public <T extends TileEntity> void render(T tile, SusVec3 playersOffset, float partialTicks) {
        // Rendering logic here
    }

    // fire from any place in code you want like:
    // MultiStructureSynthesizer.tryConstruct(...)
    @Override
    public boolean tryConstruct(World world, Position clickedPosition, EntityPlayer player) {

        if (player == null || !(player.getHeldItem().getItem() instanceof ItemWandCasting)) {
            return false;
        }

        ItemWandCasting wand = (ItemWandCasting) player.getHeldItem().getItem();
        AspectList aspectList = new AspectList()
                .add(Aspect.FIRE, 1)
                .add(Aspect.EARTH, 1)
                .add(Aspect.ORDER, 1)
                .add(Aspect.AIR, 1)
                .add(Aspect.ENTROPY, 1)
                .add(Aspect.WATER, 1);

        boolean hasEnoughVis = wand.consumeAllVisCrafting(player.getHeldItem(), player, aspectList, false);

        if (hasEnoughVis) {
            boolean basicSuccess = super.tryConstruct(world, clickedPosition, player);
            if (basicSuccess) {
                AspectList consumeAspectList = new AspectList()
                        .add(Aspect.FIRE, 70)
                        .add(Aspect.EARTH, 70)
                        .add(Aspect.ORDER, 70)
                        .add(Aspect.AIR, 70)
                        .add(Aspect.ENTROPY, 70)
                        .add(Aspect.WATER, 70);
                wand.consumeAllVisCrafting(player.getHeldItem(), player, consumeAspectList, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void finaliseConstruction(World world, Position masterPosition, ForgeDirection facing, int rotationAngle, EntityPlayer player) {
        super.finaliseConstruction(world, masterPosition, facing, rotationAngle, player);

        world.playSoundEffect(
                masterPosition.getX() + 0.5,
                masterPosition.getY() + 0.5,
                masterPosition.getZ() + 0.5,
                "thaumcraft:wand",
                1.0f,
                1.0f
        );
    }
}


