package com.suslovila.api.kharu;

import java.util.ArrayList;

public interface IKharuTransport {
    //basically, you can write whatever you f*cking want(for example, "fields" that produce kharu). The returned values could be used to handle them in Kharu suppliers
    //the main idea of kharu transport is to find a consumer for a supplier (or an opposite way)
    ArrayList <IKharuConsumer> findKharuConsumers();
    ArrayList <IKharuSupplier> findKharuSuppliers();

}
