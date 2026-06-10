package dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.ithundxr.createnumismatics.Numismatics;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankTerminalPeripheral implements IPeripheral {
    public static final BankTerminalPeripheral INSTANCE = new BankTerminalPeripheral();

    @LuaFunction
    public final Map<Integer, String> getAccounts() {
        Map<Integer, String> accounts = new HashMap<>();
        int index = 1;
        for (UUID id : Numismatics.BANK.accounts.keySet()) {
            accounts.put(index++, id.toString());
        }
        return accounts;
    }

    @LuaFunction
    public final String getAccountLabel(String accountID) {
        BankAccount account = getAccount(accountID);
        if (account == null)
            return null;

        String label = account.getLabel();
        if (label != null)
            return label;

        return account.getDisplayName().getString();
    }

    @LuaFunction
    public final boolean isPlayerOwned(String accountID) {
        BankAccount account = getAccount(accountID);
        return account != null && account.type == BankAccount.Type.PLAYER;
    }

    @LuaFunction
    public final int getBalance(String accountID) {
        BankAccount account = getAccount(accountID);
        if (account == null)
            return -1;
        return account.getBalance();
    }

    @Nullable
    private BankAccount getAccount(String accountID) {
        try {
            return Numismatics.BANK.getAccount(UUID.fromString(accountID));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    @Override
    public String getType() {
        return "Numismatics_BankTerminal";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }
}
