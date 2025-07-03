package me.whereareiam.socialismus.module.essentials.feature.privatemessage;

import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;

import java.util.Map;

public class PrivateMessage implements FeatureInitializer<CommandFeature> {
	@Override
	public String getId() {
		return "privateMessages";
	}

	@Override
	public void initialize(CommandFeature feature) {

	}

	@Override
	public Map<String, CommandEntity> getCommands() {
		return Map.of();
	}
}
