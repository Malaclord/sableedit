package hu.malaclord.sableedit.context;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Location;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import hu.malaclord.sableedit.Adaptor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class SubLevelContext extends LocationContext {
    private final @NotNull SubLevelAccess access;

    public SubLevelContext(@NotNull SubLevelAccess access) {
        this.access = access;
    }

    @Override
    public boolean isLevel() {
        return false;
    }

    public @NotNull SubLevelAccess getAccess() {
        return access;
    }

    private final Vector3d vec = new Vector3d();
    private final Adaptor adaptor = Adaptor.getInstance();

    @Override
    public Location transform(Location location) {
        adaptor.adapt(location.toVector(), vec);
        access.logicalPose().transformPositionInverse(vec);
        Vector3 pos = (adaptor.adapt(vec));

        adaptor.adapt(location.getDirection(), vec);
        access.logicalPose().transformNormalInverse(vec);
        Vector3 dir = (adaptor.adapt(vec));

        return new Location(
                location.getExtent(),
                pos,
                dir
        );
    }

    @Override
    public Location transformBack(Location location) {
        adaptor.adapt(location.toVector(), vec);
        access.logicalPose().transformPosition(vec);
        Vector3 pos = (adaptor.adapt(vec));

        adaptor.adapt(location.getDirection(), vec);
        access.logicalPose().transformNormal(vec);
        Vector3 dir = (adaptor.adapt(vec));

        return new Location(
                location.getExtent(),
                pos,
                dir
        );
    }
}
