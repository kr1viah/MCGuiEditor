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
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static imgui.flag.ImGuiWindowFlags.*;
import static kr1v.mcguieditor.client.McguieditorClient.*;

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

        ImGui.begin(" ", windowFlags);
        if (ImGui.button("Cancel")) {
            assert client != null;
            client.setScreen(parent);
        }
        if (ImGui.button("Save and quit")) {
            this.close();
        }
        if (ImGui.button("Reset to defaults")) {
            resetToDefaults();
        }
        ImGui.end();

        for (Selectable drawable : ((ScreenAccessor) parent).getSelectables()) {
            if (drawable instanceof ClickableWidget widget) {
                if (firstRun)
                    System.out.println(drawable.getClass().getName() + " is instanceof Widget");
                ImGui.setNextWindowPos(widget.getX() * guiScale, widget.getY() * guiScale, ImGuiCond.Appearing);
                ImGui.setNextWindowSize(widget.getWidth() * guiScale, widget.getHeight() * guiScale, ImGuiCond.Appearing);
                String name = widget.getMessage().getString();
                if (name.isEmpty()) name = "?";
                ImGui.begin(name + counter, new ImBoolean(true), windowFlags);
                ImGui.text(name);
                assert client != null;
                windowPositions.add(new Window(ImGui.getWindowPos(), ImGui.getWindowSize(), widget, new ImVec2(widget.getX(), widget.getY())));
                ImGui.end();
            } else if (firstRun)
                System.out.println(drawable.getClass().getName() + " is NOT instanceof Widget");
        }
        firstRun = false;
    }

    public void resetToDefaults() {
        try {
            if (!parent.getTitle().getString().isEmpty())
                JsonUtils.writeToJson(windowPositions, McguieditorClient.configDir + parent.getTitle().getString() + ".backup.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resetToDefaults.add(parent.getTitle().getString());
        assert client != null;
        Screen s = parent;
        s.onDisplayed();
        s.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        client.setScreen(new GuiEditScreen(s.getTitle(), s));
    }

    @Override
    public void close() {
        System.out.println("New thingy");
        windowSets.put(parent.getTitle().getString(), windowPositions);
        addChildIndex = 0;
        assert client != null;
        client.setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return parent.shouldPause();
    }}
