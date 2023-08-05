package smaamc.hoardspawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
public class Hoardspawner extends JavaPlugin implements Listener {

    private Map<UUID, BukkitRunnable> tasks = new HashMap<>();
    private Map<World, List<EntityType>> worldMobConfig = new HashMap<>();
    private final Map<World, List<ItemStack>> worldItemDrops = new HashMap<>();
    private boolean startHoard = false;

    @Override
    public void onEnable() {
        reloadConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Hoardspawner enabled!");
    }

    private boolean canMobSpawnSafely(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        // Check if the mob is spawning on top of lava or cactus
        Material materialBelow = world.getBlockAt(x, y - 1, z).getType();
        if (materialBelow == Material.LAVA || materialBelow == Material.CACTUS) {
            return false;
        }

        // Check if the mob is spawning in a suffocating location (e.g., inside a solid block)
        Material materialHead = world.getBlockAt(x, y + 1, z).getType();
        if (materialHead.isSolid()) {
            return false;
        }

        // Check if the mob is spawning underwater
        Material materialEyes = world.getBlockAt(x, y + 2, z).getType();
        if (materialEyes == Material.WATER || materialEyes == Material.SEAGRASS || materialEyes == Material.TALL_SEAGRASS) {
            return false;
        }

        // Optionally, you can add more checks for other dangerous conditions if needed.

        return true; // The mob can spawn safely.
    }

public void startHoardSpawning() {
    BukkitRunnable task = new BukkitRunnable() {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                World world = player.getWorld();
                int minDistance = getConfig().getInt(world.getName() + ".min_distance");
                int maxDistance = getConfig().getInt(world.getName() + ".max_distance");
                int mobSpawnLimit = getConfig().getInt(world.getName() + ".mob_spawn_limit");
                int mobsToSpawn = getConfig().getInt(world.getName() + ".min_mobs_spawn");
                String mobTypes = getConfig().getString(world.getName() + ".mobs");

                if (startHoard && canMobSpawnSafely(player.getLocation())) {
                    Random random = new Random();
                    for (int i = 0; i < mobSpawnLimit; i++) {
                        Location spawnLocation = findSafeSpawnLocation(player.getLocation(), world, minDistance, maxDistance);
                        EntityType entityType = EntityType.valueOf(mobTypes);
                        world.spawnEntity(spawnLocation, entityType);
                    }
                }
            }
        }
    };

    int globalSpawnTime = getConfig().getInt("global_spawn_time", 5) * 20;
    task.runTaskTimer(this, 0L, globalSpawnTime);
    tasks.put(UUID.randomUUID(), task);
}

    @Override
    public void onDisable() {
        // Save any active configuration here if needed.
        getLogger().info("Hoard spawner disabled.");
    }
    private void stopHoardSpawning() {
            tasks.values().forEach(BukkitRunnable::cancel);
            tasks.clear();
        }
}
