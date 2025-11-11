package cn.ahmisty.mc.xian.core.services;

import com.google.auto.service.AutoService;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@AutoService(ITransformationService.class)
public class LoadingOverlayTransformationService implements ITransformationService {
    public static final String NAME = "xian";
    private static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public @NotNull String name() {
        return NAME;
    }

    @Override
    public void initialize(IEnvironment environment) {
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {
    }

    @Override
    public @NotNull List<? extends ITransformer<?>> transformers() {
        return List.of();
    }
}
