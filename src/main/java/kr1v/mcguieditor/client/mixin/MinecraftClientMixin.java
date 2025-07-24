package kr1v.mcguieditor.client.mixin;

import static kr1v.mcguieditor.client.McguieditorClient.configDir;

import kr1v.mcguieditor.client.JsonUtils;
import kr1v.mcguieditor.client.McguieditorClient;
import kr1v.mcguieditor.client.imgui.ImGuiImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci) {ImGuiImpl.create(window.getHandle());
        configDir = MinecraftClient.getInstance().runDirectory.getPath() + "/config/MCGuiEditor/";
        try {
            Files.createDirectories(Path.of(configDir));
        } catch (IOException ignored) {}
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(configDir))) {
            for (Path file : stream) {
                String filename = file.getFileName().toString();
                if (filename.endsWith(".json")) {
                    filename = filename.substring(0, filename.length() - 5);
                    McguieditorClient.windowSets.put(filename, JsonUtils.readFromJson(file.toString()));
                }
            }
        } catch (IOException e) {
            System.out.println("Couldnt read config for MCGuiEditor!!!");
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void closeImGui(CallbackInfo ci) {
        ImGuiImpl.dispose();
        try {
            Files.createDirectories(Path.of(configDir));
            for (Map.Entry<String, List<kr1v.mcguieditor.client.Window>> windows : McguieditorClient.windowSets.entrySet()) {
                String fileToWriteTo = configDir + windows.getKey() + ".json";
                JsonUtils.writeToJson(windows.getValue(), fileToWriteTo);
            }
        } catch (IOException e) {
            System.out.println("Couldnt write config for MCGuiEditor!!!");
            throw new RuntimeException(e);
        }
    }
}