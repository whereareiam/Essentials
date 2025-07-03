package me.whereareiam.socialismus.module.essentials.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.configuration.template.FeaturesTemplate;

import java.nio.file.Path;

@Singleton
public class FeaturesProvider implements Provider<FeaturesConfig>, Reloadable {
	private final Path featuresPath;
	private final ConfigurationLoader configLoader;

	private FeaturesConfig features;

	@Inject
	public FeaturesProvider(
			@Named("featuresPath") Path featuresPath,
			ConfigurationLoader configLoader,
			ConfigurationManager configManager,
			FeaturesTemplate template,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featuresPath = featuresPath;
		this.configLoader = configLoader;

		configManager.addTemplate(FeaturesConfig.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public FeaturesConfig get() {
		if (features != null) return features;

		load();

		return features;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		features = configLoader.load(featuresPath.resolve("features"), FeaturesConfig.class);
	}
}
