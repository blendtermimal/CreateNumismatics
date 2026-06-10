package dev.ithundxr.createnumismatics.compat.computercraft.implementation.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import dev.ithundxr.createnumismatics.compat.computercraft.ComputerCraftProxy;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.ComputerBehaviour;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.BankTerminalPeripheral;
import dev.ithundxr.createnumismatics.registry.NumismaticsBlocks;
import dev.ithundxr.createnumismatics.util.Utils;
import net.minecraft.core.registries.Registries;

public class ActualComputerCraftProxyImpl {
    public static void registerWithDependency() {
        ComputerCraftProxy.computerFactory = ComputerBehaviour::new;

        PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) ->
            ComputerBehaviour.peripheralProvider(level, blockPos)
        );

        Utils.runOnceRegistered(Registries.BLOCK, () -> PeripheralLookup.get().registerForBlocks(
            (world, pos, state, blockEntity, context) -> BankTerminalPeripheral.INSTANCE,
            NumismaticsBlocks.BANK_TERMINAL.get()
        ));
    }
}
