import java.util.UUID;

public interface EcoStorage {
    double getBalance(UUID uuid);
    void setBalance(UUID uuid, double amount);
    boolean hasAccount(UUID uuid);
    void createAccount(UUID uuid, double startAmount);
    void save();
}
