package dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dev.ithundxr.createnumismatics.Numismatics;
import dev.ithundxr.createnumismatics.content.bank.IDCardItem;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryCardMethods implements GenericPeripheral {
    @Override
    public String id() {
        return Numismatics.MOD_ID + ":inventory_cards";
    }

    @LuaFunction(mainThread = true)
    public final boolean isIdentityCard(Container inventory, int slot) throws LuaException {
        ItemStack stack = getStack(inventory, slot);
        return !stack.isEmpty() && stack.getItem() instanceof IDCardItem;
    }

    @LuaFunction(mainThread = true)
    public final boolean isIdentityCardLinked(Container inventory, int slot) throws LuaException {
        ItemStack stack = getStack(inventory, slot);
        return !stack.isEmpty() && stack.getItem() instanceof IDCardItem && IDCardItem.isBound(stack);
    }

    @LuaFunction(mainThread = true)
    public final String getIdentityCardUUID(Container inventory, int slot) throws LuaException {
        ItemStack stack = getStack(inventory, slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof IDCardItem))
            return null;

        UUID id = IDCardItem.get(stack);
        return id == null ? null : id.toString();
    }

    @LuaFunction(mainThread = true)
    public final Map<Integer, Map<String, Object>> findLinkedIdentityCards(Container inventory) {
        Map<Integer, Map<String, Object>> cards = new LinkedHashMap<>();

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof IDCardItem) || !IDCardItem.isBound(stack))
                continue;

            UUID id = IDCardItem.get(stack);
            Map<String, Object> card = new LinkedHashMap<>();
            card.put("slot", slot + 1);
            card.put("uuid", id == null ? null : id.toString());
            cards.put(slot + 1, card);
        }

        return cards;
    }

    private ItemStack getStack(Container inventory, int slot) throws LuaException {
        int slotIndex = slot - 1;
        if (slotIndex < 0 || slotIndex >= inventory.getContainerSize())
            throw new LuaException("slot out of range");
        return inventory.getItem(slotIndex);
    }
}
