package kr1v.mcguieditor.client;

import imgui.ImVec2;
import net.minecraft.client.gui.widget.ClickableWidget;

public class Window {
    public ImVec2 pos;
    public ImVec2 size;
    public String origMessage;
    public Window(ImVec2 pos, ImVec2 size, ClickableWidget origWidget) {
        this.pos = pos;
        this.size = size;
        this.origMessage = origWidget.getMessage().getString();
    }

    public boolean checkIfSame(ClickableWidget widget) {
        return widget.getMessage().getString().equals(this.origMessage);
    }
}
