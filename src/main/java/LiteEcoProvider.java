import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class LiteEcoProvider extends AbstractEconomy {
    private final LiteEco plugin;
    private final EcoCore core;

    public LiteEcoProvider(LiteEco plugin, EcoCore core) {
        this.plugin = plugin;
        this.core = core;
    }

    private UUID getUuidByName(String playerName) {
        Player online = Bukkit.getPlayer(playerName);
        if (online != null) return online.getUniqueId();
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        return op.getUniqueId();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "LiteEco";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return plugin.getConfig().getInt("decimal-places", 2);
    }

    @Override
    public String format(double amount) {
        String sym = plugin.getConfig().getString("currency-symbol", "$");
        return sym + String.format("%." + fractionalDigits() + "f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Money";
    }

    @Override
    public String currencyNameSingular() {
        return "Money";
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return core.hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName) {
        return core.hasAccount(getUuidByName(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return core.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName) {
        return core.getBalance(getUuidByName(playerName));
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return core.withdraw(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return core.withdraw(getUuidByName(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return core.deposit(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return core.deposit(getUuidByName(playerName), amount);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return core.createAccount(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return core.createAccount(getUuidByName(playerName));
    }

    // ========== 废弃 + World重载 ==========
    @Deprecated
    @Override
    public boolean has(String playerName, double amount) {
        UUID uuid = getUuidByName(playerName);
        return core.canAfford(uuid, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        UUID uuid = getUuidByName(playerName);
        return core.canAfford(uuid, amount);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return core.hasAccount(getUuidByName(playerName));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return core.getBalance(getUuidByName(playerName));
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return core.withdraw(getUuidByName(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return core.deposit(getUuidByName(playerName), amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return core.createAccount(getUuidByName(playerName));
    }

    // ========== 银行统一返回未实现 ==========
    private EconomyResponse noBank() {
        return new EconomyResponse(0, 0,
                EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return noBank();
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return noBank();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return noBank();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return noBank();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return noBank();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return noBank();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return noBank();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return noBank();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return noBank();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return noBank();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return noBank();
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }
}
