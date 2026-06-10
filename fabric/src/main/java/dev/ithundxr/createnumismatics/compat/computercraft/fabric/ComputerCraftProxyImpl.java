package dev.ithundxr.createnumismatics.compat.computercraft.fabric;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.ithundxr.createnumismatics.compat.computercraft.ComputerCraftProxy;

public class ComputerCraftProxyImpl {
    public static AbstractComputerBehaviour behaviour(SmartBlockEntity sbe) {
        if (ComputerCraftProxy.computerFactory == null)
            return ComputerCraftProxy.fallbackFactory.apply(sbe);
        return ComputerCraftProxy.computerFactory.apply(sbe);
    }
}
