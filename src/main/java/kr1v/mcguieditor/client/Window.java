package kr1v.mcguieditor.client;

import imgui.ImVec2;
import net.minecraft.client.gui.widget.ClickableWidget;

public class Window {
    // should be relative to screen size somehow..
    public ImVec2 pos;
    public ImVec2 size;
    public ImVec2 originalScreenSize;
    public String origMessage;
    public Window(ImVec2 pos, ImVec2 size, ImVec2 originalScreenSize, ClickableWidget origWidget) {
        this.pos = pos;
        this.size = size;
        this.originalScreenSize = originalScreenSize;
        this.origMessage = origWidget.getMessage().getString();
    }

    public boolean checkIfSame(ClickableWidget widget) {
        return widget.getMessage().getString().equals(this.origMessage);
    }
}
