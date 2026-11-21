import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class LiteEco extends JavaPlugin {
    public EcoCore core;
    @Deprecated
    public DataManager dataManager;
    public Economy econ;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        YamlStorage storage = new YamlStorage(this);
        double start = getConfig().getDouble("starting-balance", 1000);
        core = new EcoCore(storage, start);
        econ = new LiteEcoProvider(this, core);

        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("eco").setExecutor(new EcoCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));

        getServer().getServicesManager().register(Economy.class, econ, this, ServicePriority.Highest);
        getLogger().info("LiteEco loaded! Vault economy registered.");
    }

    @Override
    public void onDisable() {
        if (core != null) {
            core = null;
        }
        getLogger().info("LiteEco saved & disabled");
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
