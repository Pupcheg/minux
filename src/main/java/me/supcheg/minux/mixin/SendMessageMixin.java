package me.supcheg.minux.mixin;

import me.supcheg.minux.Minux;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ChatScreen.class)
public final class SendMessageMixin {
    @Inject(
            method = "sendMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"
            ),
            cancellable = true
    )
    public void sendMessageToTerminal(String chatText, boolean addToHistory, @NotNull CallbackInfo ci) {
        if (Minux.getInstance().getConfiguration().isEnabled()) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(chatText));
            Minux.getInstance().sendCommandOrRestart(chatText);
            ci.cancel();
        }
    }
}
