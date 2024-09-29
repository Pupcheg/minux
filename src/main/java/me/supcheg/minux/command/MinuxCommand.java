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
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.text.Text.translatable;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor
public final class MinuxCommand {
    private static final String COMMAND = "command";

    private final Minux minux;

    @NotNull
    public LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("minux")
                .then(literal("restart")
                        .executes(this::restart)
                )
                .then(literal("shell")
                        .then(literal("set")
                                .then(argument(COMMAND, string())
                                        .executes(this::setShellCommand)
                                )
                        )
                        .then(literal("get")
                                .then(literal("running")
                                        .executes(this::getRunningShell)
                                )
                                .then(literal("configured")
                                        .executes(this::getConfiguredShell)
                                )
                        )
                );
    }

    @SneakyThrows
    private int restart(@NotNull CommandContext<ServerCommandSource> ctx) {
        minux.restartTerminal();
        ctx.getSource().sendMessage(
                translatable("minux.command.restart.success")
                        .formatted(Formatting.GREEN)
        );
        return Command.SINGLE_SUCCESS;
    }

    @SneakyThrows
    private int getRunningShell(@NotNull CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendMessage(
                translatable("minux.command.shell.get.running",
                        Text.literal(minux.getTerminal().getShellCommand()).formatted(Formatting.WHITE)
                ).formatted(Formatting.YELLOW)
        );
        return Command.SINGLE_SUCCESS;
    }

    @SneakyThrows
    private int getConfiguredShell(@NotNull CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendMessage(
                translatable("minux.command.shell.get.configured",
                        Text.literal(minux.getConfiguration().getShellCommand()).formatted(Formatting.WHITE)
                ).formatted(Formatting.YELLOW)
        );
        return Command.SINGLE_SUCCESS;
    }

    @SneakyThrows
    private int setShellCommand(@NotNull CommandContext<ServerCommandSource> ctx) {
        String oldShellCommand = minux.getConfiguration().getShellCommand();
        String newShellCommand = getString(ctx, COMMAND);

        ctx.getSource().sendMessage(
                translatable("minux.command.shell.replace",
                        Text.literal(oldShellCommand).formatted(Formatting.WHITE),
                        Text.literal(newShellCommand).formatted(Formatting.WHITE)
                ).formatted(Formatting.YELLOW)
        );
        return Command.SINGLE_SUCCESS;
    }
}
