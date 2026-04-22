package hu.malaclord.sableedit.neoforge.client;

import dev.ryanhcode.sable.companion.SableCompanion;
import hu.malaclord.sableedit.SableEdit;
import hu.malaclord.sableedit.client.SableEditClient;
import net.minecraft.core.Vec3i;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SableEdit.MOD_ID, dist = Dist.CLIENT)
public class SableEditNeoForgeClient {
    public SableEditNeoForgeClient() {
        SableEditClient.init();
    }
}
