package me.supcheg.minux.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {
    @NotNull
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(MinuxConfiguration.class, parent).get();
    }
}
