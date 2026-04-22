package hu.malaclord.sableedit;

import hu.malaclord.sableedit.context.LocationContext;
import org.jetbrains.annotations.NotNull;

public interface AbstractPlayerActorExtended {
    void sableEdit$setContext(@NotNull LocationContext context);
    @NotNull LocationContext sableEdit$getContext();
}
