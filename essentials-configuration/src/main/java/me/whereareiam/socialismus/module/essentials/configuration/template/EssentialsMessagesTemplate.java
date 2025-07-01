package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;

@Singleton
public class EssentialsMessagesTemplate implements DefaultConfig<EssentialsMessages> {
	@Override
	public EssentialsMessages getDefault() {
		EssentialsMessages config = new EssentialsMessages();

		// Default values

		return config;
	}
}
