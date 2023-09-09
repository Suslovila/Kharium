package com.suslovila.api.kharu;

public interface IKharuContainer extends IKharuSupplier, IKharuConsumer {
    int getKharuAmount();

    int getCapacity();

    //returns successfully taken amount
    int takeFromItself(int amount);

    //returns amount of kharu that wasn't added
    int putToItself(int amount);
}
