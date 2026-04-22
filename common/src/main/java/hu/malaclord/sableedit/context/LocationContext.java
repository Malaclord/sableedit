package hu.malaclord.sableedit.context;

import com.sk89q.worldedit.util.Location;

public abstract class LocationContext {
    public abstract boolean isLevel();
    public abstract Location transform(Location location);
    public abstract Location transformBack(Location location);
}
