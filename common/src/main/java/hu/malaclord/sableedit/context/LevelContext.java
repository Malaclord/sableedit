package hu.malaclord.sableedit.context;

import com.sk89q.worldedit.util.Location;

public class LevelContext extends LocationContext {

    @Override
    public boolean isLevel() {
        return true;
    }

    @Override
    public Location transform(Location location) {
        return location;
    }

    @Override
    public Location transformBack(Location location) {
        return location;
    }
}
