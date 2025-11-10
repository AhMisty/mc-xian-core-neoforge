package cn.ahmisty.mc.xian.core.services;

import com.google.auto.service.AutoService;
import net.neoforged.neoforgespi.ILaunchContext;
import net.neoforged.neoforgespi.locating.IDiscoveryPipeline;
import net.neoforged.neoforgespi.locating.IModFileCandidateLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(IModFileCandidateLocator.class)
public class ModLocator implements IModFileCandidateLocator {
    public static final String NAME = "xian";
    private static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public void findCandidates(ILaunchContext context, IDiscoveryPipeline pipeline) {
        LOGGER.debug("Could not find any other Xian Mod.");
    }
}
