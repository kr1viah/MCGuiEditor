package kr1v.mcguieditor.client.mixin;

import static kr1v.mcguieditor.client.GuiEditScreen.addChildIndex;
import static kr1v.mcguieditor.client.McguieditorClient.*;

import kr1v.mcguieditor.client.GuiEditScreen;
import kr1v.mcguieditor.client.Window;
import kr1v.mcguieditor.client.WindowSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @SuppressWarnings("unchecked")
    @ModifyVariable(method = "addDrawableChild", at = @At("HEAD"), index = 1, argsOnly = true)
    private <T extends Element & Drawable & Selectable> T addDrawableChild(T original) {
        Integer guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();
        if (original instanceof ClickableWidget widget && !(original instanceof TextIconButtonWidget.IconOnly)) {
            System.out.println(original.getClass().getName() + " is instanceof Widget");
            for (WindowSet windowSet : windowSets) {
                if (windowSet.screenClass.equals(this.getTitle().getString())) {
                    System.out.println(this.getClass().getName());
                    Window window = windowSet.windows.getFirst();
                    for (Window window1: windowSet.windows) {
                        if (window1.navigationOrder == addChildIndex) window = window1;
                    }
                    System.out.println("Setting height, width, x and y to: " + window.size.y + window.size.x + window.pos.x + window.pos.y);
                    System.out.println();
                    widget.setHeight((int)window.size.y / guiScale);
                    widget.setWidth((int)window.size.x / guiScale);
                    widget.setPosition((int)(window.pos.x / guiScale), (int)(window.pos.y / guiScale));
                    addChildIndex++;
                    return (T)widget;
                }
            }
        } else {
            System.out.println(original.getClass().getName() + " is NOT instanceof Widget");
        }
        return original;
    }
}
