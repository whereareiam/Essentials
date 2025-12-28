package me.whereareiam.socialismus.module.essentials.common.config.template;

import com.google.inject.Singleton;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;

import java.util.Map;

@Singleton
public class FeaturesTemplate implements TemplateProvider<FeaturesConfig> {
	@Override
	public FeaturesConfig supply(FeaturesConfig config) {
		// Default values
		CommandFeature pm = CommandFeature.builder()
				.enabled(true)
				.registerCommands(true)
				.build();

		config.setFeatures(Map.of(
				"dialogue", pm
		));

		return config;
	}
}
