package com.limeshulkerbox.bsvsb.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// https://github.com/CaffeineMC/sodium-fabric/blob/dev/src/main/java/net/caffeinemc/mods/sodium/mixin/features/gui/hooks/settings/OptionsScreenMixin.java
@Mixin(value = OptionsScreen.class, priority = -5000)
public abstract class MixinOptionsScreen extends Screen {
    @Shadow
    @Final
    private Options options;

    protected MixinOptionsScreen(Component title) {
        super(title);
    }

    // in createButton no name method ButtonWidget
    @Inject(method = "method_19828", at = @At("HEAD"), cancellable = true)
    private void disableSodiumSettings(CallbackInfoReturnable<Screen> cir) {
        assert this.minecraft != null;
        cir.setReturnValue(new VideoSettingsScreen(this, this.minecraft, this.options));
    }
}
