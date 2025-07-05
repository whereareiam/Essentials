package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;

import java.util.List;

@Singleton
public class EssentialsMessagesTemplate implements DefaultConfig<EssentialsMessages> {
	@Override
	public EssentialsMessages getDefault() {
		EssentialsMessages config = new EssentialsMessages();

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

		EssentialsMessages.Features features = new EssentialsMessages.Features();

		EssentialsMessages.Features.PrivateMessage privateMessage = new EssentialsMessages.Features.PrivateMessage();
		privateMessage.setNoRecipient("{prefix}<white>You must specify a recipient to send a private message to.");
		features.setPrivateMessage(privateMessage);

		config.setFeatures(features);

		return config;
	}
}
