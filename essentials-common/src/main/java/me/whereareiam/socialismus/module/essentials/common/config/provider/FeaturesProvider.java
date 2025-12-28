package me.whereareiam.socialismus.module.essentials.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.common.config.EssentialsConfigProvider;
import me.whereareiam.socialismus.module.essentials.common.config.template.FeaturesTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class FeaturesProvider extends EssentialsConfigProvider<FeaturesConfig> {
	@Inject
	public FeaturesProvider(
			@Named("featuresPath") Path featuresPath,
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		super(workingPath, reloadableRegistry);
	}

	@Override
	protected FeaturesConfig load() {
		return Config.update(getBasePath().resolve("features").resolve("features"), FeaturesConfig.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(FeaturesTemplate.class);
	}
}
