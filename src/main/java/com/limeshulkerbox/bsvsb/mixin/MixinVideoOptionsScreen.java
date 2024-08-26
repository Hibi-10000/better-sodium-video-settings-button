package com.limeshulkerbox.bsvsb.mixin;

import com.github.hibi_10000.mods.fabric.bsvsb.LoggerUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.ReesesUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.SodiumUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class MixinVideoOptionsScreen extends OptionsSubScreen {
    public MixinVideoOptionsScreen(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    protected void disableButton(CallbackInfo ci) {
        // https://github.com/CaffeineMC/sodium-fabric/pull/964#issuecomment-966529568
        assert this.list != null;
        AbstractWidget chunkBuilderMode = this.list.findOption(options.prioritizeChunkUpdates());
        if (chunkBuilderMode != null) {
            chunkBuilderMode.active = false;
            chunkBuilderMode.setMessage(
                Component.translatable(
                    "options.generic_value",
                    Component.translatable("options.prioritizeChunkUpdates"),
                    Component.literal("Sodium")
                )
            );
            chunkBuilderMode.setTooltip(null);
        }
    }

    @Override
    protected void addFooter() {
        LinearLayout directionalLayoutWidget = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        directionalLayoutWidget.addChild(Button.builder(Component.translatable("text.bettersodiumvideosettings.sodiumvideosettings"), (button) -> {
            try {
                assert this.minecraft != null;
                Screen newScreen = ReesesUtil.isReesesLoaded()
                    ? ReesesUtil.getReesesScreen(this)
                    : SodiumUtil.getSodiumScreen(this);
                assert newScreen != null;
                this.minecraft.setScreen(newScreen);
            } catch (Exception e) {
                LoggerUtil.throwError(e);
            }
        }).build());
        directionalLayoutWidget.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onClose();
        }).build());
    }
}
