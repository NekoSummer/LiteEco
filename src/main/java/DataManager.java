import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Deprecated
public class DataManager {
    private final LiteEco plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;
    public final HashMap<UUID, Double> balances = new HashMap<UUID, Double>();

    public DataManager(LiteEco plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "balances.yml");
        load();
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        balances.clear();
        for (String key : dataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double val = dataConfig.getDouble(key);
            balances.put(uuid, val);
        }
    }

    public void save() {
        for (UUID uuid : balances.keySet()) {
            dataConfig.set(uuid.toString(), balances.get(uuid));
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double get(UUID uuid) {
        if (balances.containsKey(uuid)) {
            return balances.get(uuid);
        }
        return plugin.getConfig().getDouble("starting-balance");
    }

    public void set(UUID uuid, double amount) {
        balances.put(uuid, amount);
    }
}
