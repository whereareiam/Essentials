package me.whereareiam.socialismus.module.essentials.api.input;

import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;

import java.util.Collection;

public interface FeatureManager {
	void initializeFeatures();

	Collection<Feature> getActiveFeatures();
}
