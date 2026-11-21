import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EcoCommand implements CommandExecutor {
    private final LiteEco plugin;

    public EcoCommand(LiteEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("liteeco.admin")) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage("用法：/eco give|take|set <玩家> <金额>");
            return true;
        }
        String op = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("金额必须是数字");
            return true;
        }
        if (amount < 0) {
            sender.sendMessage("不能为负数");
            return true;
        }
        String msg;
        if (op.equalsIgnoreCase("give")) {
            plugin.econ.depositPlayer(target, amount);
            msg = plugin.getConfig().getString("messages.eco-give");
        } else if (op.equalsIgnoreCase("take")) {
            EconomyResponse r = plugin.econ.withdrawPlayer(target, amount);
            if (!r.transactionSuccess()) {
                sender.sendMessage("扣除失败，余额不足");
                return true;
            }
            msg = plugin.getConfig().getString("messages.eco-take");
        } else if (op.equalsIgnoreCase("set")) {
            plugin.dataManager.set(target.getUniqueId(), amount);
            plugin.dataManager.save();
            msg = plugin.getConfig().getString("messages.eco-set");
        } else {
            sender.sendMessage("操作只支持 give / take / set");
            return true;
        }
        msg = msg.replace("%player%", target.getName())
                .replace("%symbol%", plugin.getConfig().getString("currency-symbol"))
                .replace("%amount%", plugin.econ.format(amount));
        sender.sendMessage(plugin.color(msg));
        return true;
    }
}
