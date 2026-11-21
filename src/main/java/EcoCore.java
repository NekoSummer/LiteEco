import net.milkbowl.vault.economy.EconomyResponse;
import java.util.UUID;

public class EcoCore {
    private final EcoStorage storage;
    private final double startBalance;

    public EcoCore(EcoStorage storage, double startBalance) {
        this.storage = storage;
        this.startBalance = startBalance;
    }

    public boolean hasAccount(UUID uuid) {
        return storage.hasAccount(uuid);
    }

    public double getBalance(UUID uuid) {
        return storage.getBalance(uuid);
    }

    public boolean canAfford(UUID uuid, double amount) {
        if (amount < 0) return false;
        return getBalance(uuid) >= amount;
    }

    public EconomyResponse withdraw(UUID uuid, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(uuid),
                    EconomyResponse.ResponseType.FAILURE, "金额不能为负数");
        }
        double bal = getBalance(uuid);
        if (bal < amount) {
            return new EconomyResponse(0, bal,
                    EconomyResponse.ResponseType.FAILURE, "余额不足");
        }
        storage.setBalance(uuid, bal - amount);
        storage.save();
        return new EconomyResponse(amount, bal - amount,
                EconomyResponse.ResponseType.SUCCESS, "");
    }

    public EconomyResponse deposit(UUID uuid, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(uuid),
                    EconomyResponse.ResponseType.FAILURE, "金额不能为负数");
        }
        double bal = getBalance(uuid);
        storage.setBalance(uuid, bal + amount);
        storage.save();
        return new EconomyResponse(amount, bal + amount,
                EconomyResponse.ResponseType.SUCCESS, "");
    }

    public boolean createAccount(UUID uuid) {
        if (hasAccount(uuid)) return false;
        storage.createAccount(uuid, startBalance);
        storage.save();
        return true;
    }
}
