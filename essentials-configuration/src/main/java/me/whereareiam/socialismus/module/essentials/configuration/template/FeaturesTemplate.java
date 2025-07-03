package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;

import java.util.Map;

@Singleton
public class FeaturesTemplate implements DefaultConfig<FeaturesConfig> {
	@Override
	public FeaturesConfig getDefault() {
		FeaturesConfig config = new FeaturesConfig();

		// Default values
		CommandFeature pm = CommandFeature.builder()
				.enabled(true)
				.registerCommands(true)
				.build();

		config.setFeatures(Map.of(
				"privateMessage", pm
		));

		return config;
	}
}
