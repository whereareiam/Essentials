package me.whereareiam.socialismus.module.essentials;

import com.google.inject.AbstractModule;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;

public class CommonConfiguration extends AbstractModule {
	@Override
	protected void configure() {
		bind(FeatureManager.class).to(DefaultFeatureManager.class);
	}
}
