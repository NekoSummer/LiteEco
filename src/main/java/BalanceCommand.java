import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    private final LiteEco plugin;

    public BalanceCommand(LiteEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("liteeco.balance")) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("控制台请输入 /bal <玩家>");
                return true;
            }
            Player p = (Player) sender;
            String msg = plugin.getConfig().getString("messages.balance-self")
                    .replace("%symbol%", plugin.getConfig().getString("currency-symbol"))
                    .replace("%balance%", plugin.econ.format(plugin.econ.getBalance(p)));
            sender.sendMessage(plugin.color(msg));
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        String msg = plugin.getConfig().getString("messages.balance-other")
                .replace("%player%", target.getName())
                .replace("%symbol%", plugin.getConfig().getString("currency-symbol"))
                .replace("%balance%", plugin.econ.format(plugin.econ.getBalance(target)));
        sender.sendMessage(plugin.color(msg));
        return true;
    }
}
