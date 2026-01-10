package me.whereareiam.socialismus.module.essentials.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;
import me.whereareiam.socialismus.module.essentials.common.config.EssentialsConfigProvider;
import me.whereareiam.socialismus.module.essentials.common.config.template.EssentialsMessagesTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class EssentialsMessagesProvider extends EssentialsConfigProvider<EssentialsMessages> {
	@Inject
	public EssentialsMessagesProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		super(workingPath, reloadableRegistry);
	}

	@Override
	protected EssentialsMessages load() {
		return Config.update(getBasePath().resolve("messages"), EssentialsMessages.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(EssentialsMessagesTemplate.class);
	}
}
