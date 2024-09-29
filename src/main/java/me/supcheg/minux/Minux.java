package me.supcheg.minux;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.minux.command.MinuxCommand;
import me.supcheg.minux.config.MinuxConfiguration;
import me.supcheg.minux.terminal.Terminal;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Getter
@Environment(EnvType.CLIENT)
public final class Minux implements ClientModInitializer {

    public static final String MOD_ID = "minux";

    private static Minux INSTANCE;
    private Terminal terminal;

    @NotNull
    public static Minux getInstance() {
        return Objects.requireNonNull(INSTANCE, "Invoked too early");
    }

    @NotNull
    public MinuxConfiguration getConfiguration() {
        return MinuxConfiguration.HANDLER.instance();
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        MinuxConfiguration.HANDLER.load();

        registerCommand();

        try {
            restartTerminal();
        } catch (Exception e) {
            log.error("Failed to initialize {}", Minux.class.getSimpleName(), e);
        }
    }

    private void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(new MinuxCommand(this).build())
        );
    }

    public void sendCommandOrRestart(@NotNull String command) {
        try {
            terminal.sendCommand(command);
        } catch (Exception e) {
            try {
                restartTerminal();
            } catch (Exception ex) {
                log.error("Failed to send command '{}' to the terminal", command, e);
            }
        }
    }

    public void restartTerminal() throws IOException {
        if (terminal != null) {
            try {
                terminal.close();
            } catch (Exception e) {
                log.error("Failed to close terminal", e);
            }
        }
        terminal = new Terminal(getConfiguration().getShellCommand());
        terminal.addOutputSubscription(message ->
                MinecraftClient.getInstance().inGameHud.getChatHud()
                        .addMessage(Text.of(message))
        );
    }
}
