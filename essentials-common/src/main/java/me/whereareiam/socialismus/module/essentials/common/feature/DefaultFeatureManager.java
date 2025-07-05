package me.whereareiam.socialismus.module.essentials.common.feature;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Logger;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;
import me.whereareiam.socialismus.module.essentials.api.output.CommandFeatureInitializer;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DefaultFeatureManager implements FeatureManager {
	private final Injector injector;
	private final FeaturesConfig features;
	private final EssentialsSettings settings;
	private final CommandService commandService;

	private final Set<FeatureInitializer<? extends Feature>> inits;
	private final Registry<Map<String, CommandEntity>> commandRegistry;

	private final Set<Feature> activeFeatures = new HashSet<>();

	@Override
	public void initializeFeatures() {
		var features = this.features.getFeatures();

		boolean announce = settings.getAnnounce().isFeatureInitialization();

		for (var init : inits) {
			String id = init.getId();
			Feature feat = features.get(id);
			if (feat == null || !feat.isEnabled()) continue;

			invokeInit(init, feat);
			if (announce) Logger.info("Feature enabled: {}", id);

			if (feat instanceof CommandFeature cf && cf.isRegisterCommands())
				registerCommandsFor(init, announce);

			activeFeatures.add(feat);
		}
	}

	@Override
	public Collection<Feature> getActiveFeatures() {
		return activeFeatures;
	}

	@SuppressWarnings("unchecked")
	private <F extends Feature> void invokeInit(FeatureInitializer<F> init, Feature f) {
		init.initialize((F) f);
	}

	private <F extends Feature> void registerCommandsFor(
			FeatureInitializer<F> init,
			boolean announce
	) {
		if (!(init instanceof CommandFeatureInitializer cfi))
			return;

		commandRegistry.register(cfi.getCommands());

		Map<String, Class<? extends CommandBase>> executors = cfi.getExecutors();
		for (Map.Entry<String, CommandEntity> entry : cfi.getCommands().entrySet()) {
			String commandKey = entry.getKey();

			if (executors.containsKey(commandKey)) {
				Class<? extends CommandBase> commandClass = executors.get(commandKey);
				CommandBase commandInstance = injector.getInstance(commandClass);
				commandService.registerCommand(commandInstance);
			}
		}
		commandService.registerCommands();

		if (announce)
			Logger.info("Registered commands for feature: {}", init.getId());
	}
}
