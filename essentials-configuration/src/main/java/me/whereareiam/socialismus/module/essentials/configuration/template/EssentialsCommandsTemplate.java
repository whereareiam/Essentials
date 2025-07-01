package me.whereareiam.socialismus.module.essentials.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;

import java.util.List;

@Singleton
public class EssentialsCommandsTemplate implements DefaultConfig<EssentialsCommands> {
	@Override
	public EssentialsCommands getDefault() {
		EssentialsCommands config = new EssentialsCommands();

		// Default values
		CommandEntity features = CommandEntity.builder()
				.enabled(true)
				.aliases(List.of("features"))
				.permission("socialismus.admin")
				.description("Shows enabled features.")
				.usage("{command} {alias}")
				.cooldown(CommandEntity.Cooldown.builder()
						.enabled(true)
						.duration(2)
						.group("global")
						.build()
				).build();

		config.getCommands().put("features", features);

		return config;
	}
}
