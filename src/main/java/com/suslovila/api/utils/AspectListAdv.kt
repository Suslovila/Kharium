package com.suslovila.api.utils

import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

class AspectListOne: AspectList() {
	override fun add(asp: Aspect, amount: Int): AspectListOne {
		this.aspects[asp] = 1
		return this
	}

	fun add(asp: Aspect): Pair<AspectListOne, Boolean> {
		if (canAdd(asp))
			return Pair(this, false)

		return Pair(this.add(asp, 1), true)
	}

	fun canAdd(asp: Aspect) = !this.aspects.containsKey(asp) && this.aspects.size <= 2

	fun canAdd() = this.aspects.size <= 2
}