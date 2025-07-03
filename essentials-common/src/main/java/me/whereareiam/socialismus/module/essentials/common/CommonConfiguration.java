package me.whereareiam.socialismus.module.essentials.common;

import com.google.inject.AbstractModule;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.common.feature.DefaultFeatureManager;

public class CommonConfiguration extends AbstractModule {
	@Override
	protected void configure() {
		bind(FeatureManager.class).to(DefaultFeatureManager.class);
	}
}
