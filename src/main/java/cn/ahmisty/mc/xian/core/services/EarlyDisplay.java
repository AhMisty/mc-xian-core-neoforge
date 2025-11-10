package cn.ahmisty.mc.xian.core.services;

import com.google.auto.service.AutoService;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper;
import net.neoforged.neoforgespi.earlywindow.ImmediateWindowProvider;
import net.neoforged.neoforgespi.locating.IOrderedProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.*;

@AutoService({GraphicsBootstrapper.class, ImmediateWindowProvider.class})
public class EarlyDisplay implements GraphicsBootstrapper, ImmediateWindowProvider, IOrderedProvider {
    public static final String NAME = "xian";
    private static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    private static ImmediateWindowProvider PROVIDER;

    @Override
    public String name () {
        if (PROVIDER != null) {
            return PROVIDER.name();
        }
        return NAME;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void bootstrap(String[] arguments) {
        if (PROVIDER != null) return;
        String ProviderName = FMLConfig.getConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_PROVIDER);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ServiceLoader<ImmediateWindowProvider> serviceLoader = ServiceLoader.load(ImmediateWindowProvider.class, contextClassLoader);
        PROVIDER = serviceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(p -> !Objects.equals(p.name(), NAME))
                .filter(p -> Objects.equals(p.name(), ProviderName))
                .sorted(Comparator.comparingInt(p -> (p instanceof IOrderedProvider op) ? op.getPriority() : 0).reversed())
                .findFirst()
                .orElse(null);
        if (PROVIDER != null) {
            LOGGER.info("Successfully found provider: {}", PROVIDER.name());
        } else {
            String message = "Could not find any other window provider.";
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public void periodicTick() {
        PROVIDER.periodicTick();
    }

    @Override
    public Runnable initialize(String[] args) {
        LOGGER.info("Use provider: {}", PROVIDER.name());
        return PROVIDER.initialize(args);
    }

    @Override
    public void updateFramebufferSize(IntConsumer width, IntConsumer height) {
        PROVIDER.updateFramebufferSize(width, height);
    }

    @Override
    public long setupMinecraftWindow(final IntSupplier width, final IntSupplier height, final Supplier<String> title, final LongSupplier monitor) {
        return PROVIDER.setupMinecraftWindow(width, height, title, monitor);
    }

    @Override
    public boolean positionWindow(Optional<Object> monitor, IntConsumer widthSetter, IntConsumer heightSetter, IntConsumer xSetter, IntConsumer ySetter) {
        return PROVIDER.positionWindow(monitor, widthSetter, heightSetter, xSetter, ySetter);
    }

    @Override
    public <T> Supplier<T> loadingOverlay(Supplier<?> mc, Supplier<?> ri, Consumer<Optional<Throwable>> ex, boolean fade) {
        return PROVIDER.loadingOverlay(mc, ri, ex, fade);
    }

    @Override
    public void updateModuleReads(ModuleLayer layer) {
        PROVIDER.updateModuleReads(layer);
    }

    @Override
    public String getGLVersion() {
        return PROVIDER.getGLVersion();
    }

    @Override
    public void crash(String message) {
        PROVIDER.crash(message);
    }
}
