package dev.ithundxr.createnumismatics.compat.computercraft.implementation;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ActualComputerCraftProxy {
    @ExpectPlatform
    public static void registerWithDependency() {
        throw new AssertionError();
    }
}
