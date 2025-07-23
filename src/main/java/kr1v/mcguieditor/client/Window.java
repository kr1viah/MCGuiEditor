package kr1v.mcguieditor.client;

import imgui.ImVec2;

public class Window {
    public ImVec2 pos;
    public ImVec2 size;
    public int navigationOrder;
    public Window(ImVec2 pos, ImVec2 size, int navigationOrder) {
        this.pos = pos;
        this.size = size;
        this.navigationOrder = navigationOrder;
    }
}
