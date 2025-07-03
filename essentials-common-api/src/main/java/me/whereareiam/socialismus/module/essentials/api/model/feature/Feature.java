package me.whereareiam.socialismus.module.essentials.api.model.feature;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class Feature {
	private boolean enabled;
}
