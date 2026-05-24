package dk.sdu.cbse.main;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginLoader {

    public static ModuleLayer loadPlugins(String pluginsPath) {
        Path pluginsDir = Path.of(pluginsPath);

        ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);

        Set<String> moduleNames = pluginsFinder.findAll()
                .stream()
                .map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        if (moduleNames.isEmpty()) {
            System.out.println("[PluginLoader] No plugins found in: " + pluginsPath);
            return ModuleLayer.boot();
        }

        ModuleLayer bootLayer = ModuleLayer.boot();
        Configuration config = bootLayer.configuration()
                .resolve(pluginsFinder, ModuleFinder.of(), moduleNames);

        ModuleLayer pluginLayer = bootLayer.defineModulesWithOneLoader(
                config, ClassLoader.getSystemClassLoader());

        moduleNames.forEach(name ->
                System.out.println("[PluginLoader] Loaded plugin module: " + name));

        return pluginLayer;
    }
}
