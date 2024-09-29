package me.supcheg.minux.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import lombok.Getter;
import lombok.Setter;
import me.supcheg.minux.Minux;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Getter
@Setter
public final class MinuxConfiguration {
    public static final ConfigClassHandler<MinuxConfiguration> HANDLER = ConfigClassHandler.createBuilder(MinuxConfiguration.class)
            .id(Identifier.of(Minux.MOD_ID, "configuration"))
            .serializer(config ->
                    GsonConfigSerializerBuilder.create(config)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve(Minux.MOD_ID + ".json"))
                            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                            .build()
            )
            .build();

    @SerialEntry
    private String shellCommand = "bash";
}
