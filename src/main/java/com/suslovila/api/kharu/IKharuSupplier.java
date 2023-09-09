package com.suslovila.api.kharu;
//pure Kharu sources use this class
public interface IKharuSupplier {
    //returns a value of produced kharu per tick at current moment of time (value can be changed at some moment, for example if anti-node is upgraded or container stopped to output essentia)
    int getKharuOutputAmount();
//here we are supposed to do our dirty things (returned value - the remnant, it can be used in some cases)
    int handleKharuOutput(int amount);

}
