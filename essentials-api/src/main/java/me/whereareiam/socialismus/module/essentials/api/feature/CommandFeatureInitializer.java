package me.whereareiam.socialismus.module.essentials.api.feature;

import me.whereareiam.commandant.model.CommandDefinition;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;

import java.util.Map;

/**
 * Like FeatureInitializer<CommandFeature> but also exposes the command class
 * that should be instantiated for each command key.
 */
public interface CommandFeatureInitializer extends FeatureInitializer<CommandFeature> {
	/**
	 * @return a map from command key → the CommandDefinition (metadata)
	 */
	Map<String, CommandDefinition> getCommands();

	/**
	 * @return a map from the same command key → the command class (POJOs with Cloud annotations)
	 */
	Map<String, Class<?>> getExecutors();
}
