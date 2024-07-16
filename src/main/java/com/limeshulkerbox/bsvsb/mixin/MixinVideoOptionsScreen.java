package com.limeshulkerbox.bsvsb.mixin;

import com.github.hibi_10000.mods.fabric.bsvsb.LoggerUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.ReesesUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.SodiumUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends GameOptionsScreen {
    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "getOptions", at = @At("RETURN"), cancellable = true)
    private static void removeChunkBuilderButton(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        SimpleOption<?>[] value = cir.getReturnValue();
        value = ArrayUtils.removeElement(value, gameOptions.getChunkBuilderMode());
        cir.setReturnValue(value);
    }

    @Override
    protected void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).build());
        directionalLayoutWidget.add(ButtonWidget.builder(Text.translatable("text.bettersodiumvideosettings.sodiumvideosettings"), (button) -> {
            try {
                assert this.client != null;
                Screen newScreen = ReesesUtil.isReesesLoaded()
                    ? ReesesUtil.getReesesScreen(this)
                    : SodiumUtil.getSodiumScreen(this);
                assert newScreen != null;
                this.client.setScreen(newScreen);
            } catch (Exception e) {
                LoggerUtil.throwError(e);
            }
        }).build());
    }
}
