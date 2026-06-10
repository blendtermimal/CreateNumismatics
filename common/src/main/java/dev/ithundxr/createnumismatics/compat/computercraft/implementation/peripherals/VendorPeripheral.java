package dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals;

import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import dev.ithundxr.createnumismatics.content.vendor.VendorBlockEntity;

public class VendorPeripheral extends SyncedPeripheral<VendorBlockEntity> {
    public VendorPeripheral(VendorBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction(mainThread = true)
    public final void setCoinAmount(String coinName, int amount) throws LuaException {
        Coin coin = getCoinFromName(coinName);
        blockEntity.setPrice(coin, amount);
        blockEntity.notifyUpdate();
    }

    @LuaFunction(mainThread = true)
    public final void setTotalPrice(int spurAmount) {
        for (Coin coin : Coin.byValueDescending) {
            int coinAmount = coin.convert(spurAmount).getFirst();
            blockEntity.setPrice(coin, coinAmount);
            spurAmount -= coin.toSpurs(coinAmount);
        }
        blockEntity.notifyUpdate();
    }

    @LuaFunction
    public final int getTotalPrice() {
        return blockEntity.getTotalPrice();
    }

    @LuaFunction
    public final int getPrice(String coinName) throws LuaException {
        Coin coin = getCoinFromName(coinName);
        return blockEntity.getPrice(coin);
    }

    private Coin getCoinFromName(String coinName) throws LuaException {
        for (Coin coin : Coin.values()) {
            if (coin.getName().equalsIgnoreCase(coinName))
                return coin;
        }
        throw new LuaException("incorrect coin name");
    }

    @Override
    public String getType() {
        return "Numismatics_Vendor";
    }
}
