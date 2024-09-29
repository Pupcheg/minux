package me.supcheg.minux.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.text.Text.translatable;

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {
    @NotNull
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent ->
                YetAnotherConfigLib.create(
                                MinuxConfiguration.HANDLER,
                                (defaults, config, builder) -> builder
                                        .title(translatable("minux.config.title"))
                                        .category(ConfigCategory.createBuilder()
                                                .name(translatable("minux.config.category.main.title"))
                                                .option(
                                                        Option.<String>createBuilder()
                                                                .name(translatable("minux.config.category.shell_command.title"))
                                                                .description(OptionDescription.of(translatable("minux.config.category.shell_command.description")))
                                                                .binding(
                                                                        defaults.getShellCommand(),
                                                                        config::getShellCommand,
                                                                        config::setShellCommand
                                                                )
                                                                .controller(StringControllerBuilder::create)
                                                                .build()
                                                )
                                                .build()
                                        )
                        )
                        .generateScreen(parent);
    }
}
