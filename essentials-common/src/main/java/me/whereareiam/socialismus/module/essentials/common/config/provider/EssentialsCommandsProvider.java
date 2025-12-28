package me.whereareiam.socialismus.module.essentials.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import me.whereareiam.socialismus.module.essentials.common.config.EssentialsConfigProvider;
import me.whereareiam.socialismus.module.essentials.common.config.template.EssentialsCommandsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class EssentialsCommandsProvider extends EssentialsConfigProvider<EssentialsCommands> {
	@Inject
	public EssentialsCommandsProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		super(workingPath, reloadableRegistry);
	}

	@Override
	protected EssentialsCommands load() {
		return Config.update(getBasePath().resolve("commands"), EssentialsCommands.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(EssentialsCommandsTemplate.class);
	}
}
