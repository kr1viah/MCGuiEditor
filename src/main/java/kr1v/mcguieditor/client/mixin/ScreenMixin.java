package kr1v.mcguieditor.client.mixin;

import static kr1v.mcguieditor.client.McguieditorClient.*;

import kr1v.mcguieditor.client.GuiEditScreen;
import kr1v.mcguieditor.client.Window;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow public abstract Text getTitle();

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (keyBinding.matchesKey(keyCode, 0) && !(client.currentScreen instanceof GuiEditScreen)) {
            client.setScreen(new GuiEditScreen(client.currentScreen != null ? client.currentScreen.getTitle() : Text.literal("Unknown screen."),
                    client.currentScreen));
        }
    }

    @Inject(method = "init()V", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        resetToDefaults.remove(this.getTitle().getString());
    }

    @ModifyVariable(method = "addSelectableChild", at = @At("HEAD"), index = 1, argsOnly = true)
    private <T extends Element & Selectable> T addSelectableChild(T original) {
        return handleThingamajig(original);
    }

    @ModifyVariable(method = "addDrawableChild", at = @At("HEAD"), index = 1, argsOnly = true)
    private <T extends Element & Drawable & Selectable> T addDrawableChild(T original) {
        return handleThingamajig(original);
    }

    @Unique
    @SuppressWarnings("unchecked")
    private <T extends Element & Selectable> T handleThingamajig(T original) {
        if (resetToDefaults.contains(this.getTitle().getString())) {
            return original;
        }
        Integer guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();
        if (original instanceof ClickableWidget widget/* && !(original instanceof TextIconButtonWidget.IconOnly)*/) {
            System.out.println(original.getClass().getName() + " is instanceof Widget");
            List<Window> windowSet = windowSets.get(this.getTitle().getString());
            if (windowSet == null) return original;
            for (Window window: windowSet) {
                if (window.checkIfSame(widget)) {
                    widget.setHeight((int)window.size.y / guiScale);
                    widget.setWidth((int)window.size.x / guiScale);
                    widget.setPosition((int)(window.pos.x / guiScale), (int)(window.pos.y / guiScale));
                    return (T)widget;
                }
            }
        } else {
            System.out.println(original.getClass().getName() + " is NOT instanceof Widget");
        }
        return original;
    }
}
