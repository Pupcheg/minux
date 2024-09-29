package me.supcheg.minux.config;

import com.google.gson.FieldNamingPolicy;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Getter
@Setter
public final class MinuxConfiguration {
    public static final ConfigClassHandler<MinuxConfiguration> HANDLER = ConfigClassHandler.createBuilder(MinuxConfiguration.class)
            .id(Identifier.of("minux", "configuration"))
            .serializer(config ->
                    GsonConfigSerializerBuilder.create(config)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve("minux.json"))
                            .appendGsonBuilder(gson -> gson
                                    .setPrettyPrinting()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            )
                            .build()
            )
            .build();

    @SerialEntry
    private String shellCommand = "bash";
}
