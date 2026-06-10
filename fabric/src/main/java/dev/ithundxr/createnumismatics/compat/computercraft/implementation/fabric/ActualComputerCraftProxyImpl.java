package dev.ithundxr.createnumismatics.compat.computercraft.implementation.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import dev.ithundxr.createnumismatics.compat.computercraft.ComputerCraftProxy;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.ComputerBehaviour;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.BankTerminalPeripheral;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.BrassDepositorPeripheral;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.VendorPeripheral;
import dev.ithundxr.createnumismatics.content.depositor.BrassDepositorBlockEntity;
import dev.ithundxr.createnumismatics.content.vendor.VendorBlockEntity;
import dev.ithundxr.createnumismatics.registry.NumismaticsBlocks;

public class ActualComputerCraftProxyImpl {
    public static void registerWithDependency() {
        ComputerCraftProxy.computerFactory = ComputerBehaviour::new;

        PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) -> {
            if (blockEntity instanceof BrassDepositorBlockEntity brassDepositor)
                return new BrassDepositorPeripheral(brassDepositor);
            if (blockEntity instanceof VendorBlockEntity vendor)
                return new VendorPeripheral(vendor);
            return ComputerBehaviour.peripheralProvider(level, blockPos);
        });

        PeripheralLookup.get().registerForBlocks(
            (world, pos, state, blockEntity, context) -> BankTerminalPeripheral.INSTANCE,
            NumismaticsBlocks.BANK_TERMINAL.get()
        );
    }
}
