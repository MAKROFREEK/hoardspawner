package smaamc.hoardspawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Purge extends Hoardspawner {

private void purgeExcessMobsInChunk(Chunk chunk, int maxMobs) {
    World world = chunk.getWorld();
    int mobCount = 0;

    // Count the number of mobs in the chunk
    for (Entity entity : chunk.getEntities()) {
        if (entity instanceof LivingEntity) { // Assuming mobs are living entities (e.g., zombies, skeletons)
            mobCount++;
        }
    }

    if (mobCount <= maxMobs) {
        // No need to purge, the mob count is within the limit
        return;
    }

    // Purge the excess mobs
    int mobsToRemove = mobCount - maxMobs;
    for (Entity entity : chunk.getEntities()) {
        if (entity instanceof LivingEntity && mobsToRemove > 0) {
            entity.remove();
            mobsToRemove--;
        }
    }
}

}
