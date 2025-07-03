package me.whereareiam.socialismus.module.essentials.api.model.feature;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
public class Feature {
	private String id;
	private boolean enabled;
}
