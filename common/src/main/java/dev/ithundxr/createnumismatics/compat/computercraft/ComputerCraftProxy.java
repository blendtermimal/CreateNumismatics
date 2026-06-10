package dev.ithundxr.createnumismatics.compat.computercraft;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.FallbackComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.ithundxr.createnumismatics.compat.Mods;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.ActualComputerCraftProxy;

import java.util.function.Function;

public class ComputerCraftProxy {
    public static void register() {
        fallbackFactory = FallbackComputerBehaviour::new;
        Mods.COMPUTERCRAFT.executeIfInstalled(() -> ActualComputerCraftProxy::registerWithDependency);
    }

    public static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> fallbackFactory;
    public static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> computerFactory;

    @ExpectPlatform
    public static AbstractComputerBehaviour behaviour(SmartBlockEntity sbe) {
        throw new AssertionError();
    }
}
