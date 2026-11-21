import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    private final LiteEco plugin;

    public PayCommand(LiteEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("liteeco.pay")) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("控制台不能转账");
            return true;
        }
        Player from = (Player) sender;
        if (args.length != 2) {
            sender.sendMessage("用法：/pay <玩家> <金额>");
            return true;
        }
        OfflinePlayer to = Bukkit.getOfflinePlayer(args[0]);
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("金额必须是数字");
            return true;
        }
        if (amount <= 0) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.pay-negative")));
            return true;
        }
        EconomyResponse res = plugin.econ.withdrawPlayer(from, amount);
        if (!res.transactionSuccess()) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.pay-not-enough")));
            return true;
        }
        plugin.econ.depositPlayer(to, amount);

        String sendMsg = plugin.getConfig().getString("messages.pay-success")
                .replace("%player%", to.getName())
                .replace("%symbol%", plugin.getConfig().getString("currency-symbol"))
                .replace("%amount%", plugin.econ.format(amount));
        from.sendMessage(plugin.color(sendMsg));

        if (to.isOnline()) {
            String recvMsg = plugin.getConfig().getString("messages.pay-receive")
                    .replace("%player%", from.getName())
                    .replace("%symbol%", plugin.getConfig().getString("currency-symbol"))
                    .replace("%amount%", plugin.econ.format(amount));
            to.getPlayer().sendMessage(plugin.color(recvMsg));
        }
        return true;
    }
}
