package me.supcheg.minux.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.minux.Minux;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
                translatable("minux.command.restart.success", NamedTextColor.GREEN)
        );
        return Command.SINGLE_SUCCESS;
    }

    private int getRunningShell(@NotNull CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendMessage(
                translatable()
                        .key("minux.command.shell.get.running")
                        .color(NamedTextColor.YELLOW)
                        .arguments(
                                text(minux.getTerminal().getShellCommand(), NamedTextColor.WHITE)
                        )
        );
        return Command.SINGLE_SUCCESS;
    }

    private int getConfiguredShell(@NotNull CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendMessage(
                translatable()
                        .key("minux.command.shell.get.configured")
                        .color(NamedTextColor.YELLOW)
                        .arguments(
                                text(minux.getConfiguration().getShellCommand(), NamedTextColor.WHITE)
                        )
        );
        return Command.SINGLE_SUCCESS;
    }

    private int setShellCommand(@NotNull CommandContext<ServerCommandSource> ctx) {
        String oldShellCommand = minux.getConfiguration().getShellCommand();
        String newShellCommand = getString(ctx, COMMAND);

        minux.getConfiguration().setShellCommand(newShellCommand);

        ctx.getSource().sendMessage(
                translatable()
                        .key("minux.command.shell.replace")
                        .color(NamedTextColor.YELLOW)
                        .arguments(
                                text(oldShellCommand, NamedTextColor.WHITE),
                                text(newShellCommand, NamedTextColor.WHITE)
                        )
        );
        return Command.SINGLE_SUCCESS;
    }
}
