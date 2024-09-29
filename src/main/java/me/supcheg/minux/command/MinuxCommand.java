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
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.text.Text.translatable;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor
public final class MinuxCommand {
    private final Minux minux;

    @NotNull
    public LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("minux")
                .then(argument("restart", string())
                        .executes(this::restart)
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
}
