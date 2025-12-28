package me.whereareiam.socialismus.module.essentials.api.model.feature;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.whereareiam.configura.annotation.Polymorphic;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@Polymorphic(
		inferBy = {
				@Polymorphic.Infer(field = "registerCommands", target = CommandFeature.class)
		},
		defaultTarget = Feature.class
)
public class Feature {
	private boolean enabled;
}
