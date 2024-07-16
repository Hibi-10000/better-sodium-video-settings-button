package com.limeshulkerbox.bsvsb.mixin;

import com.github.hibi_10000.mods.fabric.bsvsb.LoggerUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.ReesesUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.SodiumUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends GameOptionsScreen {
    @Shadow
    private OptionListWidget list;

    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void disableButton(CallbackInfo ci) {
        // https://github.com/CaffeineMC/sodium-fabric/pull/964#issuecomment-966529568
        ClickableWidget chunkBuilderMode = this.list.getWidgetFor(gameOptions.getChunkBuilderMode());
        if (chunkBuilderMode != null) {
            chunkBuilderMode.active = false;
            chunkBuilderMode.setMessage(
                Text.translatable(
                    "options.generic_value",
                    Text.translatable("options.prioritizeChunkUpdates"),
                    Text.literal("Sodium")
                )
            );
            chunkBuilderMode.setTooltip(null);
        }
    }

    @Override
    protected void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
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
        directionalLayoutWidget.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).build());
    }
}
