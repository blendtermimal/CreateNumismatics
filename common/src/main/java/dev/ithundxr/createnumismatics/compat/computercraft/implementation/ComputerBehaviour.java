package dev.ithundxr.createnumismatics.compat.computercraft.implementation;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.BrassDepositorPeripheral;
import dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals.VendorPeripheral;
import dev.ithundxr.createnumismatics.content.depositor.BrassDepositorBlockEntity;
import dev.ithundxr.createnumismatics.content.vendor.VendorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ComputerBehaviour extends AbstractComputerBehaviour {
    Supplier<IPeripheral> peripheralSupplier;

    public static IPeripheral peripheralProvider(Level level, BlockPos blockPos) {
        AbstractComputerBehaviour behavior = BlockEntityBehaviour.get(level, blockPos, AbstractComputerBehaviour.TYPE);
        if (behavior instanceof ComputerBehaviour real)
            return real.getPeripheral();
        return null;
    }

    public ComputerBehaviour(SmartBlockEntity te) {
        super(te);
        this.peripheralSupplier = getPeripheralFor(te);
    }

    public static Supplier<IPeripheral> getPeripheralFor(SmartBlockEntity be) {
        if (be instanceof BrassDepositorBlockEntity scbe)
            return () -> new BrassDepositorPeripheral(scbe);
        if (be instanceof VendorBlockEntity scbe)
            return () -> new VendorPeripheral(scbe);

        throw new IllegalArgumentException("No peripheral available for " + be.getType());
    }

    @Override
    public <T> T getPeripheral() {
        //noinspection unchecked
        return (T) peripheralSupplier.get();
    }
}
