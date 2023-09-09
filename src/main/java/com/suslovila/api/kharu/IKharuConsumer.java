package com.suslovila.api.kharu;

public interface IKharuConsumer {
    //by idea, all kharu-based things consume kharu each tick to work. So, this method returns required value in ticks
    int requiredKharuAmount();

//here we are supposed to do our dirty things (returned value - the remnant, it can be used in some cases)

    int handleKharuInput(int amount);
}
