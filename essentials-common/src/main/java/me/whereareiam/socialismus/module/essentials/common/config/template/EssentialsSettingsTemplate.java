package me.whereareiam.socialismus.module.essentials.common.config.template;

import com.google.inject.Singleton;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;

@Singleton
public class EssentialsSettingsTemplate implements TemplateProvider<EssentialsSettings> {
	@Override
	public EssentialsSettings supply(EssentialsSettings config) {
		// Default values
		EssentialsSettings.Announce announce = new EssentialsSettings.Announce();
		announce.setFeatureInitialization(true);

		config.setAnnounce(announce);

		return config;
	}
}
