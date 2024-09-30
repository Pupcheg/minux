package me.supcheg.minux.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.minux.Minux;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.screen.ScreenTexts.onOrOff;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.text.Text.translatable;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor
public final class MinuxCommand {
    private static final String COMMAND = "command";
    private static final String STATE = "state";

    private final Minux minux;

    @NotNull
    public LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("minux")
                .then(literal("restart")
                        .executes(this::restart)
                )
                .then(literal("enabled")
                        .executes(this::getEnabled)
                        .then(argument(STATE, bool())
                                .executes(this::setEnabled)
                        )
                )
                .then(literal("shell")
                        .executes(this::getConfiguredShell)
                        .then(argument(COMMAND, string())
                                .executes(this::setShellCommand)
                        )
                );
    }

    @SneakyThrows
    private int restart(@NotNull CommandContext<ServerCommandSource> ctx) {
        minux.restartTerminal();

        ctx.getSource().sendFeedback(() -> translatable("minux.command.restart.success"), true);
        return Command.SINGLE_SUCCESS;
    }

    private int getEnabled(@NotNull CommandContext<ServerCommandSource> ctx) {
        boolean enabled = minux.getConfiguration().isEnabled();

        ctx.getSource().sendFeedback(() -> translatable("minux.command.enabled.get", onOrOff(enabled)), true);
        return Command.SINGLE_SUCCESS;
    }

    private int setEnabled(@NotNull CommandContext<ServerCommandSource> ctx) {
        boolean enabled = getBool(ctx, STATE);

        minux.getConfiguration().setEnabled(enabled);

        ctx.getSource().sendFeedback(() -> translatable("minux.command.enabled.set", onOrOff(enabled)), true);
        return Command.SINGLE_SUCCESS;
    }

    private int getConfiguredShell(@NotNull CommandContext<ServerCommandSource> ctx) {
        String configured = minux.getConfiguration().getShellCommand();
        String running = minux.getTerminal().getShellCommand();

        ctx.getSource().sendFeedback(() -> translatable("minux.command.shell.get", Text.of(configured), Text.of(running)), true);
        return Command.SINGLE_SUCCESS;
    }

    private int setShellCommand(@NotNull CommandContext<ServerCommandSource> ctx) {
        String command = getString(ctx, COMMAND);

        minux.getConfiguration().setShellCommand(command);

        ctx.getSource().sendFeedback(() -> translatable("minux.command.shell.set", Text.of(command)), true);
        return Command.SINGLE_SUCCESS;
    }
}
