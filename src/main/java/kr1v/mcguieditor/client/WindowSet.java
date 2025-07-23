package kr1v.mcguieditor.client;

import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class WindowSet {
    public List<Window> windows;
    public String screenClass;
    public WindowSet(List<Window> windows, String screenClass) {
        this.windows = windows;
        this.screenClass = screenClass;
    }
}
