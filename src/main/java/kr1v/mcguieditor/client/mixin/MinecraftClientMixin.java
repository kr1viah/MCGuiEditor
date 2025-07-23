package kr1v.mcguieditor.client.mixin;

import kr1v.mcguieditor.client.JsonUtils;
import kr1v.mcguieditor.client.McguieditorClient;
import kr1v.mcguieditor.client.imgui.ImGuiImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Window;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initImGui(RunArgs args, CallbackInfo ci) {
        ImGuiImpl.create(window.getHandle());
        try {
            Files.createDirectories(Path.of(MinecraftClient.getInstance().runDirectory.getPath() + "/config/MCGuiEditor/screens.json"));
            McguieditorClient.windowSets = JsonUtils.readFromJson(MinecraftClient.getInstance().runDirectory.getPath() + "/config/MCGuiEditor/screens.json");
        } catch (IOException ignored) {
System.out.println("Couldnt read config for MCGuiEditor!!!");
            throw new RuntimeException(ignored);
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void closeImGui(CallbackInfo ci) {
        ImGuiImpl.dispose();
        try {
            Files.createDirectories(Path.of(MinecraftClient.getInstance().runDirectory.getPath() + "/config/MCGuiEditor/screens.json"));
            JsonUtils.writeToJson(McguieditorClient.windowSets, MinecraftClient.getInstance().runDirectory.getPath() + "/config/MCGuiEditor/screens.json");
        } catch (IOException e) {
            System.out.println("Couldnt write config for MCGuiEditor!!!");
            throw new RuntimeException(e);
        }
    }

}