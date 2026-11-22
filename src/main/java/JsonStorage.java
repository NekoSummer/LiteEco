import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonStorage implements EcoStorage {

    private final LiteEco plugin;
    private final File dataFile;
    private final Gson config;
    private final Map<UUID, Double> cache = new HashMap<>();

    public JsonStorage(LiteEco plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "balances.json");
        config = new GsonBuilder().setPrettyPrinting().create();
        loadAll();
    }

    private void loadAll() {
        if (!dataFile.exists())
            return;
        try (FileReader reader = new FileReader(dataFile)) {
            Map<String, Double> raw = config.fromJson(reader, Map.class);
            if (raw == null) return;
            cache.clear();
            for (Map.Entry<String, Double> entry: raw.entrySet()) {
                try {
                    UUID uuid = UUID.fromString(entry.getKey());
                    cache.put(uuid, entry.getValue());
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("JSON 内无效UUID: " + entry.getKey());
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("读取 balances.json 失败！");
            e.printStackTrace();
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
        Map<String, Double> exportMap = new HashMap<>();
        for (Map.Entry<UUID, Double> entry: cache.entrySet()) {
            exportMap.put(entry.getKey().toString(), entry.getValue());
        }
        try (FileWriter writer = new FileWriter(dataFile)) {
            config.toJson(exportMap, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("保存 balances.json 失败！");
            e.printStackTrace();
        }
    }
}
