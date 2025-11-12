package cn.ahmisty.mc.xian.core.utils;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class Library {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Path BASE = FMLPaths.GAMEDIR.get().resolve("xian").resolve("libraries");
    public final Path name;
    public final Path dir;
    public final Path path;
    public final Arena arena;
    public static final Linker LINKER = Linker.nativeLinker();
    public final SymbolLookup instance;

    public Library (String name, Arena arena) {
        boolean initialized;
        this.arena = arena;
        this.name = Path.of(name).getFileName();
        this.path = BASE.resolve(name);
        this.dir = this.path.getParent();
        if (!Files.exists(this.path)) {
            Path pathInJar = Path.of("/libraries").resolve(name);
            LOGGER.info("Could not find library {} at {}, try to find in jar path {}", this.name, this.dir, pathInJar);
            try (InputStream stream = Library.class.getClassLoader().getResourceAsStream(pathInJar.toString())) {
                if (stream == null) {
                    LOGGER.error("Could not find library {} in jar path {}", this.name, pathInJar);
                    initialized = false;
                } else {
                    LOGGER.info("Successfully found library {} in jar path {}, try to copy it to {}", this.name, pathInJar, this.path);
                    Files.createDirectories(this.dir);
                    Files.copy(stream, this.path, StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("Successfully copied library {} to {}", this.name, this.path);
                    initialized = true;
                }
            } catch (IOException error) {
                LOGGER.error("Could not find library {} with error {}", this.name, error);
                initialized = false;
            }
        } else {
            LOGGER.info("Successfully found library {} at {}", this.name, this.dir);
            initialized = true;
        }
        if (initialized) {
            this.instance = SymbolLookup.libraryLookup(this.path, this.arena);
            LOGGER.info("Successfully loaded library {} at {}", this.name, this.path);
        } else {
            this.instance = null;
        }
    }

    public MethodHandle loadFunction (String name, FunctionDescriptor descriptor, Linker.Option option) {
        Optional<MemorySegment> symbol = this.instance.find(name);
        if (symbol.isEmpty()) {
            return null;
        }
        return LINKER.downcallHandle(symbol.get(), descriptor, option);
    }

    public MethodHandle loadFunction (String name, FunctionDescriptor descriptor) {
        Optional<MemorySegment> symbol = this.instance.find(name);
        if (symbol.isEmpty()) {
            return null;
        }
        return LINKER.downcallHandle(symbol.get(), descriptor);
    }
}
