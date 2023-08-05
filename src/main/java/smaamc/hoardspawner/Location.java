package smaamc.hoardspawner;

import org.bukkit.World;

import java.util.Random;
public class Location extends Hoardspawner {

	public Location findSafeSpawnLocation(Location center, World world, int minDistance, int maxDistance) {
    Random random = new Random();
    int attempts = 0;
    int maxAttempts = 100; // Limit the number of attempts to find a safe location to avoid infinite loop

    while (attempts < maxAttempts) {
        double x = center.getX() + random.nextDouble() * (maxDistance - minDistance) + minDistance;
        double z = center.getZ() + random.nextDouble() * (maxDistance - minDistance) + minDistance;
        double y = world.getHighestBlockYAt((int) x, (int) z);

        if (canMobSpawnSafely(new Location(world, x, y, z))) {
            return new Location(world, x, y, z);
        }
        attempts++;
    }

    // If no safe location found after maxAttempts, return center location as fallback
    return center;
}

}
