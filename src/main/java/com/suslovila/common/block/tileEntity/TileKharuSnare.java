package com.suslovila.common.block.tileEntity;

import com.suslovila.api.kharu.IKharuConsumer;
import com.suslovila.api.kharu.IKharuContainer;
import com.suslovila.api.kharu.IKharuSupplier;
import com.suslovila.api.kharu.IKharuTransport;
import thaumcraft.api.TileThaumcraft;

import java.util.ArrayList;

//I think "Velonium" is a pretty cool word. We can name something with it
public class TileKharuSnare extends TileThaumcraft implements IKharuTransport {

    @Override
    public ArrayList<IKharuConsumer> findKharuConsumers() {
        return null;
    }

    @Override
    public ArrayList<IKharuSupplier> findKharuSuppliers() {
        return null;
    }
}
