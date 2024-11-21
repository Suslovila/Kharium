package com.suslovila.kharium.api.process

// is not serialised
abstract class ClientProcess(
    var x : Double,
    var y: Double,
    var z: Double,

    val duration: Int
) : IProcess {
    var timer = 0

    override fun isExpired() =
        timer < duration



}