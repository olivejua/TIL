package me.olivejua.learningjava.solid.interfacesegregation.better.implement;

import me.olivejua.learningjava.solid.interfacesegregation.better.BearCleaner;
import me.olivejua.learningjava.solid.interfacesegregation.better.BearFeeder;

public class BearCarer implements BearCleaner, BearFeeder {

    public void washTheBear() {
        //I think we missed a spot...
    }

    public void feedTheBear() {
        //Tuna Tuesdays...
    }
}
