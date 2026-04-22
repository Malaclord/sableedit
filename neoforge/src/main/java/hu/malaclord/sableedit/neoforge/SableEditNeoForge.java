package hu.malaclord.sableedit.neoforge;

import hu.malaclord.sableedit.Sableedit;
import net.neoforged.fml.common.Mod;

@Mod(Sableedit.MOD_ID)
public final class SableeditNeoForge {
    public SableeditNeoForge() {
        // Run our common setup.
        Sableedit.init();
    }
}
