package me.whereareiam.socialismus.module.essentials.command.executor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandCooldown;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.Map;

@Singleton
public class FeaturesCommand extends CommandBase {
	private static final String COMMAND_NAME = "features";
	private final Provider<EssentialsCommands> commands;

	@Inject
	public FeaturesCommand(
			Provider<EssentialsCommands> commands
	) {
		super(COMMAND_NAME);

		this.commands = commands;
	}

	@Command("%command." + COMMAND_NAME)
	@CommandDescription("%description." + COMMAND_NAME)
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onCommand(DummyPlayer dummyPlayer) {
		
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
