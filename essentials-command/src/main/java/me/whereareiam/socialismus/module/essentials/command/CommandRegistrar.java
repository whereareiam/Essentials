package me.whereareiam.socialismus.module.essentials.command;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import me.whereareiam.socialismus.module.essentials.command.executor.FeaturesCommand;

import java.util.Map;
import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor
public class CommandRegistrar {
	private final Injector injector;
	private final CommandService commandService;
	private final Provider<EssentialsCommands> commands;
	private final Registry<Map<String, CommandEntity>> commandRegistry;

	public void registerCommands() {
		commandRegistry.register(commands.get().getCommands());

		Stream.of(
				injector.getInstance(FeaturesCommand.class)
		).forEach(commandService::registerCommand);
	}
}
