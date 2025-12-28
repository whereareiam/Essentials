package me.whereareiam.socialismus.module.essentials.common.config.template;

import com.google.inject.Singleton;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;

import java.util.List;

@Singleton
public class EssentialsMessagesTemplate implements TemplateProvider<EssentialsMessages> {
	@Override
	public EssentialsMessages supply(EssentialsMessages config) {
		// Default values
		EssentialsMessages.Commands commands = new EssentialsMessages.Commands();

		EssentialsMessages.Commands.FeaturesCommand featuresCommand = new EssentialsMessages.Commands.FeaturesCommand();
		featuresCommand.setFormat(List.of(
				" ",
				"<gold><bold> Socialismus</bold> <white>Features",
				" ",
				"{features}",
				" "
		));
		featuresCommand.setFeatureFormat(" <dark_gray>- <green>{feature}");
		featuresCommand.setNoFeatures(" <red>No features available.");
		commands.setFeaturesCommand(featuresCommand);

		config.setCommands(commands);

		return config;
	}
}
