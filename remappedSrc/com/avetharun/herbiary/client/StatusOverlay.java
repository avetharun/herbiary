package com.avetharun.herbiary.client;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Stack;

public class StatusOverlay {
    float fov_alt;
    public static int getBlockHeight() {
        MinecraftClient client = MinecraftClient.getInstance();
        int guiScale = client.options.getGuiScale().getValue();
        int scaleFactor = guiScale > 0 ? guiScale : 1;
        TextRenderer textRenderer = client.textRenderer;

        int height = textRenderer.fontHeight;
        int margin = 5 * scaleFactor / 2; // Same as text padding
        return height - margin; // Adjust 5 for padding
    }
    public static HashMap<String, PlayerStatus> localStatuses;
    public static void render(MatrixStack matrices) {
        return;
//        MinecraftClient client = MinecraftClient.getInstance();
//        InGameHud hud = client.inGameHud;
//        TextRenderer textRenderer = client.textRenderer;
//
//        int guiScale = client.options.getGuiScale().getValue();
//        int scaleFactor = guiScale > 0 ? guiScale : 1;
//        int margin = 5 * scaleFactor / 2; // Same as text padding
//        int screenWidth = client.getWindow().getFramebufferWidth() / scaleFactor;
//        int screenHeight = client.getWindow().getFramebufferHeight() / scaleFactor;
//        int x = screenWidth - margin * 2; // Adjust 5 for padding
//        int y = margin; // Start at the top of the screen
//
//        for (int i = 0; i < localStatuses.size(); i++) {
//            String id = (String) localStatuses.keySet().toArray()[i];
//            PlayerStatus status = localStatuses.get(id);
//            String display = status.title + "  " + (int)status.percentage + ((int)status.percentage < 100 ? " ": "") + "%";
////            if (status.percentage < status.requiredPercentageToDisplay) {continue;}
//            // Calculate the width and height of the text with the current GUI scale
//            int width = textRenderer.getWidth(display);
//            int height = textRenderer.fontHeight;
//            // Adjust the y position for each status block
//            // Render the background
//            DrawableHelper.fill(matrices, x - margin - width, (int) (y), x + margin * 2, (int) (y + height + margin * 3), 0x80000000);
//            // Render the text
//            textRenderer.drawWithShadow(matrices, display, x + margin - width, y + height, 0xFFFFFF);
//            y += (height + margin * 3.5f);
//        }
    }
}
