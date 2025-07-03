package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;

import java.util.List;

@Singleton
public class FeaturesTemplate implements DefaultConfig<FeaturesConfig> {
	@Override
	public FeaturesConfig getDefault() {
		FeaturesConfig config = new FeaturesConfig();

		// Default values
		Feature pm = Feature.builder()
				.id("privateMessages")
				.enabled(true)
				.build();

		config.setFeatures(List.of(
				pm
		));

		return config;
	}
}
