package me.whereareiam.socialismus.module.essentials.api.output;

import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;

import java.util.Map;

/**
 * Like FeatureInitializer<CommandFeature> but also exposes the CommandBase class
 * that should be instantiated for each command key.
 */
public interface CommandFeatureInitializer extends FeatureInitializer<CommandFeature> {
	/**
	 * @return a map from command key → the CommandEntity (metadata)
	 */
	Map<String, CommandEntity> getCommands();

	/**
	 * @return a map from the same command key → the CommandBase class
	 */
	Map<String, Class<? extends CommandBase>> getExecutors();
}
