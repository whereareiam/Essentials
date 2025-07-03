package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;

@Singleton
public class EssentialsSettingsTemplate implements DefaultConfig<EssentialsSettings> {
	@Override
	public EssentialsSettings getDefault() {
		EssentialsSettings config = new EssentialsSettings();

		// Default values
		EssentialsSettings.Announce announce = new EssentialsSettings.Announce();
		announce.setFeatureInitialization(true);

		config.setAnnounce(announce);

		return config;
	}
}
