package com.limeshulkerbox.bsvsb.mixin;

import com.github.hibi_10000.mods.fabric.bsvsb.LoggerUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.ReesesUtil;
import com.github.hibi_10000.mods.fabric.bsvsb.SodiumUtil;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends GameOptionsScreen {
    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Override
    protected <T extends Element & Selectable> T addSelectableChild(T child) {
        if (child instanceof OptionListWidget optionList) {
            // https://github.com/CaffeineMC/sodium-fabric/pull/964#issuecomment-966529568
            ClickableWidget chunkBuilderMode = optionList.getWidgetFor(gameOptions.getChunkBuilderMode());
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
        return super.addSelectableChild(child);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void mixinInit(CallbackInfo callbackInfo) {
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("text.bettersodiumvideosettings.sodiumvideosettings"), (button) -> {
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
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 100, ordinal = 0))
    private int modifyDonePos(int input) {
        return -5;
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 200, ordinal = 0))
    private int modifyDoneWidth(int input) {
        return 150;
    }
}
