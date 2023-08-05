package smaamc.hoardspawner;

import org.bukkit.World;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;


public class Drops extends Hoardspawner {
	    public class CustomDropHandler {

        public void onEntityDeath(EntityDeathEvent event) {
            int dropAmount = getConfig().getInt("drops");
            Entity entity = event.getEntity();
            World world = entity.getWorld();
            List<ItemStack> customDrops = worldItemDrops.get(world);

            if (customDrops != null && !customDrops.isEmpty()) {
                // Applying custom drop ratio
                int ratio = 1;
                int totalRatio = 0;

                // Calculate totalRatio
                for (ItemStack item : customDrops) {
                    totalRatio += item.getAmount();
                }

                // Drop custom items based on the ratio
                for (int i = 0; i < dropAmount; i++) {
                    int random = new Random().nextInt(totalRatio);
                    int count = 0;
                    for (ItemStack item : customDrops) {
                        count += item.getAmount();
                        if (random < count) {
                            event.getDrops().add(item.clone());
                            getLogger().info("Dropped item: " + item.getType().name());
                            break;
                        }
                    }
                }
            }
        }
    }
}
