package com.github.hibi_10000.mods.fabric.bsvsb;

import me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class ReesesUtil {
    private ReesesUtil() {}

    public static boolean isReesesLoaded() {
        return FabricLoader.getInstance().isModLoaded("reeses-sodium-options");
    }

    public static Screen getReesesScreen(Screen screen) {
        try {
            var constructor = SodiumVideoOptionsScreen.class.getConstructor(Screen.class, List.class);
            return constructor.newInstance(screen, SodiumUtil.getPages(screen));
        } catch (Exception e) {
            LoggerUtil.throwError(e);
        }
        return null;
    }
}
