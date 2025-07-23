package kr1v.mcguieditor.client;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import kr1v.mcguieditor.client.imgui.ImGuiImpl;
import kr1v.mcguieditor.client.mixin.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static imgui.flag.ImGuiWindowFlags.*;
import static kr1v.mcguieditor.client.McguieditorClient.counter;
import static kr1v.mcguieditor.client.McguieditorClient.windowSets;

public class GuiEditScreen extends Screen {
    public static int addChildIndex = 0;
    private final Screen parent;
    private boolean firstRun = true;
    private List<Window> windowPositions = new ArrayList<>();

    public GuiEditScreen(Text title, Screen parent) {

        super(title);
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ImGuiImpl.draw(this::renderImGui);
    }

    private void renderImGui(final ImGuiIO io) {
        windowPositions = new ArrayList<>();
        int windowFlags = NoSavedSettings + NoTitleBar + NoCollapse;
        Integer guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();

        if (firstRun) {
            counter++;
        }

        for (Drawable drawable : ((ScreenAccessor) parent).getDrawables()) {
            if (drawable instanceof ClickableWidget widget) {
//                if (widget instanceof TextIconButtonWidget.IconOnly) continue;
                ImGui.setNextWindowPos(widget.getX() * guiScale, widget.getY() * guiScale, ImGuiCond.Appearing);
                ImGui.setNextWindowSize(widget.getWidth() * guiScale, widget.getHeight() * guiScale, ImGuiCond.Appearing);
                String name = widget.getMessage().getString();
                if (name.isEmpty()) name = "?";
                ImGui.begin(name + counter, new ImBoolean(true), windowFlags);
                ImGui.text(name);
                assert client != null;
                windowPositions.add(new Window(ImGui.getWindowPos(), ImGui.getWindowSize(), new ImVec2(client.getWindow().getWidth(), client.getWindow().getHeight()), widget));
                ImGui.end();
            }
        }
        firstRun = false;
    }

    @Override
    public void close() {
        System.out.println("New thingy");
        windowSets.put(this.parent.getTitle().getString(), windowPositions);
        System.out.println();
        System.out.println();
        addChildIndex = 0;
        assert client != null;
        client.setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return parent.shouldPause();
    }}
