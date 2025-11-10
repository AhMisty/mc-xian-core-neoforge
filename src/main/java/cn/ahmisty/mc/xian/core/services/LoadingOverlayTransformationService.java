package cn.ahmisty.mc.xian.core.services;

import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

public class LoadingOverlayTransformationService implements ITransformationService {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String Name = "xian";

    @Override
    public @NotNull String name() {
        return Name;
    }

    @Override
    public void initialize(IEnvironment environment) {
        LOGGER.info("xian-ITransformationService loading");
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {
        LOGGER.info("xian-ITransformationService loaded");
    }

    @Override
    public @NotNull List<? extends ITransformer<?>> transformers() {
        return List.of();
    }
}
