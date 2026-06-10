package dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.ithundxr.createnumismatics.Numismatics;
import dev.ithundxr.createnumismatics.content.bank.IDCardItem;
import dev.ithundxr.createnumismatics.content.bank.blaze_banker.BlazeBankerBlockEntity;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.registry.NumismaticsItems;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class BlazeBankerPeripheral implements IPeripheral {
    private final BlazeBankerBlockEntity blockEntity;

    public BlazeBankerPeripheral(BlazeBankerBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @LuaFunction
    public final boolean hasAccount() {
        return blockEntity.hasAccount();
    }

    @LuaFunction
    public final String getAccountId() {
        UUID id = blockEntity.getAccountId();
        return id == null ? null : id.toString();
    }

    @LuaFunction
    public final int getBalance() {
        BankAccount account = blockEntity.getAccount();
        return account == null ? 0 : account.getBalance();
    }

    @LuaFunction
    public final String getLabel() {
        return blockEntity.getLabel();
    }

    @LuaFunction(mainThread = true)
    public final void setLabel(String label) {
        blockEntity.setLabel(label);
    }

    @LuaFunction
    public final boolean isAuthorized(String uuid) throws LuaException {
        UUID id = parseUuid(uuid);
        BankAccount account = blockEntity.getAccount();
        return account != null && account.isAuthorized(id);
    }

    @LuaFunction
    public final Map<Integer, String> listTrusted() {
        Map<Integer, String> trusted = new LinkedHashMap<>();
        int i = 1;
        for (UUID id : blockEntity.getTrustList()) {
            trusted.put(i++, id.toString());
        }
        return trusted;
    }

    @LuaFunction(mainThread = true)
    public final boolean addTrusted(String uuid) throws LuaException {
        return addTrustedWithColor(uuid, "white");
    }

    @LuaFunction(mainThread = true)
    public final boolean addTrustedWithColor(String uuid, String colorName) throws LuaException {
        UUID id = parseUuid(uuid);
        DyeColor color = DyeColor.byName(colorName, DyeColor.WHITE);

        if (isTrustedCardPresent(id))
            return false;

        for (int slot = 0; slot < blockEntity.trustListContainer.getContainerSize(); slot++) {
            if (!blockEntity.trustListContainer.getItem(slot).isEmpty())
                continue;

            ItemStack stack = new ItemStack(NumismaticsItems.ID_CARDS.get(color).get());
            IDCardItem.set(stack, id);
            blockEntity.trustListContainer.setItem(slot, stack);
            blockEntity.notifyUpdate();
            return true;
        }

        throw new LuaException("trust list is full");
    }

    @LuaFunction(mainThread = true)
    public final boolean removeTrusted(String uuid) throws LuaException {
        UUID id = parseUuid(uuid);

        for (int slot = 0; slot < blockEntity.trustListContainer.getContainerSize(); slot++) {
            ItemStack stack = blockEntity.trustListContainer.getItem(slot);
            UUID stackId = IDCardItem.get(stack);
            if (id.equals(stackId)) {
                blockEntity.trustListContainer.setItem(slot, ItemStack.EMPTY);
                blockEntity.notifyUpdate();
                return true;
            }
        }

        return false;
    }

    @LuaFunction(mainThread = true)
    public final Object[] transferTo(String destinationAccountId, int amount) throws LuaException {
        if (amount <= 0)
            throw new LuaException("amount must be positive");

        BankAccount source = blockEntity.getAccount();
        if (source == null)
            return new Object[] { false, "source account unavailable" };

        BankAccount destination = getBankAccount(destinationAccountId);
        if (destination == null)
            return new Object[] { false, "destination account not found" };

        if (!source.deduct(amount))
            return new Object[] { false, "insufficient funds" };

        destination.deposit(amount);
        blockEntity.notifyUpdate();
        return new Object[] { true, null };
    }

    private boolean isTrustedCardPresent(UUID id) {
        for (int slot = 0; slot < blockEntity.trustListContainer.getContainerSize(); slot++) {
            UUID stackId = IDCardItem.get(blockEntity.trustListContainer.getItem(slot));
            if (id.equals(stackId))
                return true;
        }
        return false;
    }

    @Nullable
    private BankAccount getBankAccount(String accountId) throws LuaException {
        UUID id = parseUuid(accountId);
        return Numismatics.BANK.getAccount(id);
    }

    private UUID parseUuid(String uuid) throws LuaException {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new LuaException("invalid uuid");
        }
    }

    @Override
    public String getType() {
        return "Numismatics_BlazeBanker";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }
}
