package me.whereareiam.socialismus.module.essentials.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;
import me.whereareiam.socialismus.module.essentials.common.config.EssentialsConfigProvider;
import me.whereareiam.socialismus.module.essentials.common.config.template.EssentialsSettingsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class EssentialsSettingsProvider extends EssentialsConfigProvider<EssentialsSettings> {
	@Inject
	public EssentialsSettingsProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		super(workingPath, reloadableRegistry);
	}

	@Override
	protected EssentialsSettings load() {
		return Config.update(getBasePath().resolve("settings"), EssentialsSettings.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(EssentialsSettingsTemplate.class);
	}
}
