package me.supcheg.minux.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.supcheg.minux.Minux;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Getter
@Setter
@Config(name = Minux.MOD_ID)
public final class MinuxConfiguration implements ConfigData {
    private boolean enabled = true;
    private String shellCommand = "bash";
}
