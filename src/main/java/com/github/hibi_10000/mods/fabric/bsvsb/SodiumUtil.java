package com.github.hibi_10000.mods.fabric.bsvsb;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class SodiumUtil {
    private SodiumUtil() {}

    public static SodiumOptionsGUI getSodiumScreen(Screen screen) {
        try {
            return new SodiumOptionsGUI(screen);
        } catch (Exception e) {
            LoggerUtil.throwError(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static List<OptionPage> getPages(Screen screen) {
        try {
            var pagesField = SodiumOptionsGUI.class.getDeclaredField("pages");
            pagesField.setAccessible(true);
            return (List<OptionPage>) pagesField.get(getSodiumScreen(screen));
        } catch (Exception e) {
            LoggerUtil.throwError(e);
        }
        return null;
    }
}
