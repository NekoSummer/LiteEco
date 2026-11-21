import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlStorage implements EcoStorage {
    private final LiteEco plugin;
    private final File dataFile;
    private final FileConfiguration config;
    private final Map<UUID, Double> cache = new HashMap<>();

    public YamlStorage(LiteEco plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "balances.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("balances.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(dataFile);
        loadAll();
    }

    private void loadAll() {
        cache.clear();
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double val = config.getDouble(key);
                cache.put(uuid, val);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public double getBalance(UUID uuid) {
        return cache.getOrDefault(uuid, 0D);
    }

    @Override
    public void setBalance(UUID uuid, double amount) {
        cache.put(uuid, amount);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return cache.containsKey(uuid);
    }

    @Override
    public void createAccount(UUID uuid, double startAmount) {
        cache.put(uuid, startAmount);
    }

    @Override
    public void save() {
        for (Map.Entry<UUID, Double> entry : cache.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("保存经济数据失败！");
            e.printStackTrace();
        }
    }
}
