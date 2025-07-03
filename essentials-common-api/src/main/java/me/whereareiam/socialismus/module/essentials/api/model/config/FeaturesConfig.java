package me.whereareiam.socialismus.module.essentials.api.model.config;

import lombok.Getter;
import lombok.Setter;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;

import java.util.List;

@Getter
@Setter
public class FeaturesConfig {
	private List<? extends Feature> features;
}
