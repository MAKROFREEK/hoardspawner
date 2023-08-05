package smaamc.hoardspawner;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commands extends Hoardspawner {



    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smaamc.hoard.admin")) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /hoard <start|stop|reload>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "start":
                startHoardSpawning();
                startHoard = true;
                sender.sendMessage("Hoard spawning started.");
                break;
            case "stop":
                stopHoardSpawning();
                startHoard = false;
                sender.sendMessage("Hoard spawning stopped.");
                break;
            case "reload":
                reloadConfig();
                sender.sendMessage("Configuration reloaded.");
                break;
            default:
                sender.sendMessage("Usage: /hoard <start|stop|reload>");
                break;
        }

        return true;
    }


}
