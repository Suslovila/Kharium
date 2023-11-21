package com.suslovila.api.kharu


sealed interface IKharuManipulator

interface IKharuContainer : IKharuSupplier, IKharuConsumer {
    fun getStoredKharuAmount(): Int
    fun getCapacity() : Int
}

interface IKharuConsumer  : IKharuManipulator {
    //this methods return successfully put Kharu
    fun putToItself(amount: Int): Int

    // as in thaumcraft, suction determines which transport will take essentia from another.
    fun getSuction()
    fun setSuction()
}

interface IKharuSupplier  : IKharuManipulator {
    //this methods return successfully taken Kharu
    fun takeFromItself(amount: Int): Int
}



interface IKharuTransport : IKharuManipulator {

//basically, you can write whatever you f*cking want
    /*
    The main idea of kharu transport is similar to thaumcraft's essentia transport - one essentia transport takes
    kharu from another - all logic is individual for each kharu transport

*/
    //Mostly kharu tranports are tileEntities, but not always
    fun getSuction()
    fun setSuction()
}
