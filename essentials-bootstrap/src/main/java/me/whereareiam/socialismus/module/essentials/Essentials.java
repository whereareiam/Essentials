package me.whereareiam.socialismus.module.essentials;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.essentials.api.feature.FeatureManager;
import me.whereareiam.socialismus.module.essentials.command.CommandRegistrar;
import me.whereareiam.socialismus.module.essentials.common.CommonConfiguration;
import me.whereareiam.socialismus.module.essentials.feature.FeatureConfiguration;
import me.whereareiam.socialismus.registry.base.Registry;
import me.whereareiam.socialismus.starter.ModuleStarter;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Essentials extends ModuleStarter {
	private final Injector parentInjector;
	private final Registry<Reloadable> reloadableRegistry;
	private Injector injector;

	@Override
	public void onLoad() {
		injector =
				Guice.createInjector(
						new EssentialsInjectorConfiguration(
								parentInjector,
								reloadableRegistry),
						new CommonConfiguration(workingPath),
						new FeatureConfiguration());
	}

	@Override
	public void onEnable() {
		injector.getInstance(CommandRegistrar.class).registerCommands();
		injector.getInstance(FeatureManager.class).initializeFeatures();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onUnload() {
	}
}
