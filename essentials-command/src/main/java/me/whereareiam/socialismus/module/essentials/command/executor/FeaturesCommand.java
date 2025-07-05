package me.whereareiam.socialismus.module.essentials.command.executor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.Serializer;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandCooldown;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.Map;

@Singleton
public class FeaturesCommand extends CommandBase {
	private static final String COMMAND_NAME = "features";
	private final Provider<EssentialsCommands> commands;
	private final Provider<EssentialsMessages> messages;
	private final FeatureManager featureManager;

	@Inject
	public FeaturesCommand(
			Provider<EssentialsCommands> commands,
			Provider<EssentialsMessages> messages,
			FeatureManager featureManager
	) {
		super(COMMAND_NAME);

		this.commands = commands;
		this.messages = messages;
		this.featureManager = featureManager;
	}

	@Command("%command." + COMMAND_NAME)
	@CommandDescription("%description." + COMMAND_NAME)
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onCommand(DummyPlayer dummyPlayer) {
		var messages = this.messages.get().getCommands().getFeaturesCommand();
		final Map<String, Feature> features = featureManager.getActiveFeatures();

		String format = String.join("\n", messages.getFormat());

		if (features.isEmpty()) {
			format = format.replace("{features}", messages.getNoFeatures());
			dummyPlayer.sendMessage(Serializer.serialize(dummyPlayer, format));
			return;
		}

		StringBuilder featureList = new StringBuilder();
		for (String feature : features.keySet()) {
			String featureFormat = messages.getFeatureFormat().replace("{feature}", feature);
			featureList.append(featureFormat).append("\n");
		}

		format = format.replace("{features}", featureList.toString());
		dummyPlayer.sendMessage(Serializer.serialize(dummyPlayer, format));
	}

	@Override
	public CommandEntity getCommandEntity() {
		return commands.get().getCommands().get(COMMAND_NAME);
	}

	@Override
	public Map<String, String> getTranslations() {
		CommandEntity command = commands.get().getCommands().get("features");

		return Map.of(
				"command." + command.getAliases().get(0) + ".name", command.getUsage().replace("{alias}", String.join("|", command.getAliases())),
				"command." + command.getAliases().get(0) + ".permission", command.getPermission(),
				"command." + command.getAliases().get(0) + ".description", command.getDescription(),
				"command." + command.getAliases().get(0) + ".usage", command.getUsage()
		);
	}
}
