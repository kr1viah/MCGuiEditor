package kr1v.mcguieditor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.*;

public class McguieditorClient implements ClientModInitializer {
    public static List<WindowSet> windowSets = new ArrayList<>();
    public static KeyBinding keyBinding;
    public static int counter = 0;
    @Override
    public void onInitializeClient() {
        keyBinding = registerKeyBinding(new KeyBinding(
                "Edit GUI", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "GUI editor" // The translation key of the keybinding's category.
        ));
    }
}
