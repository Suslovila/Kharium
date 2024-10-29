package com.suslovila.kharium.common.item.implants

//class ImplantReactiveSkin() :
//    ItemImplant(ImplantType.SKIN) {
//    init {
//        unlocalizedName = "blade"
//        setTextureName(Kharium.MOD_ID + ":diary")
//        setMaxStackSize(1)
//        creativeTab = Kharium.tab
//    }
//
//    override fun onPlayerAttackEntityEvent(event: AttackEntityEvent, index: Int, implant: ItemStack) {
//        val lastHurt = "lastHurt"
//        val tag = implant.getOrCreateTag()
//        val worldTime = event.entityPlayer.worldObj.totalWorldTime
//        val previousHitTime = tag.getLong(lastHurt)
//        val basicDamage = 3.0f
//        val basicReloading = 60
//        val halfInvulnarableDelay = 10
//        val hasEnoughTimePassed =
//            abs(previousHitTime - worldTime) > (halfInvulnarableDelay * (9 - RuneUsingItem.getRuneAmountOfType(
//                implant,
//                RuneType.OVERCLOCK
//            )))
//        if (hasEnoughTimePassed) {
//            val damage = basicDamage * (1 + RuneUsingItem.getRuneAmountOfType(implant, RuneType.EXPANSION) / 2)
//
//            (event.target as? EntityLiving)?.run {
//                attackEntityFrom(
//                    DamageSource.outOfWorld,
//                    damage
//                )
//                tag.setLong(lastHurt, worldTime)
//            }
//            event.entityPlayer.addKharu(10_00)
//        }
//        tag.setLong(
//            lastHurt,
//            if (tag.hasKey(lastHurt))
//                tag.getLong(lastHurt)
//            else worldTime
//        )
//    }
//
//    override fun onUseButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
//
//    }
//
//    override fun getCooldownTotal(implant: ItemStack): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun getFuelConsumeOnActivation(implant: ItemStack): MagicFuel {
//        TODO("Not yet implemented")
//    }
//
//    override fun getFuelConsumeOnTick(implant: ItemStack): MagicFuel {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun getKharuEmissionOnTick(implant: ItemStack): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun getKharuEmissionOnSpecial(implant: ItemStack): Int {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun onRenderHandEvent(event: RenderHandEvent, index: Int, implant: ItemStack) {
//        ItemSpaceDividerRenderer.runeModel.renderAll()
//    }
//
//}