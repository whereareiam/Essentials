package me.whereareiam.socialismus.module.essentials.api.output;

import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;

import java.util.Map;

public interface FeatureInitializer<T extends Feature> {
	String getId();

	void initialize(T feature);

	Map<String, CommandEntity> getCommands();
}
