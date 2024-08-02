package com.suslovila.kharium.api.event

// is not serialised
abstract class ClientProcess(
    var x : Int,
    var y: Int,
    var z: Int,

    val duration: Int
) : IProcess {
    var timer = 0

    override fun isExpired() =
        timer < duration

}