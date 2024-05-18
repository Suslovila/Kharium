package com.suslovila.kharium.mixin;

import com.suslovila.kharium.Kharium;
import com.suslovila.kharium.api.kharu.MagicTile;
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler;
import com.suslovila.kharium.utils.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.IEssentiaTransport;
import vazkii.botania.api.mana.IManaBlock;

import static com.suslovila.kharium.mixinUtils.MixinStaticMethods.startNodeTransformation;


@Mixin(value = World.class)
public abstract class MixinMagicTile {

    @Redirect(method = "updateEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"
            ))
    public void abandonUpdate(TileEntity tileEntity) {
        boolean isManaBlock = Kharium.botaniaLoaded && tileEntity instanceof IManaBlock;
        if (tileEntity instanceof IEssentiaTransport || tileEntity instanceof TileThaumcraft || tileEntity instanceof MagicTile || isManaBlock) {
            double percent = KharuInfluenceHandler.INSTANCE.getKharuAmountPercent(tileEntity);
            if (SusMathHelper.INSTANCE.tryWithPercentChance(1 - percent)) {
                tileEntity.updateEntity();
            }
            else {
                System.out.println("We are fucking tile: " + tileEntity);

            }
        } else {
            tileEntity.updateEntity();
        }
    }
}
