package hu.malaclord.sableedit.fabric.client;

import hu.malaclord.sableedit.client.SableEditClient;
import net.fabricmc.api.ClientModInitializer;

public final class SableEditFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        SableEditClient.init();
    }
}
