package me.whereareiam.socialismus.module.essentials;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;

import java.util.Set;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DefaultFeatureManager implements FeatureManager {
	private final Injector injector;

	private Set<FeatureInitializer<?>> featureInitializers;

	@Override
	public void initializeFeatures() {
		featureInitializers = injector.getInstance(Key.get(new TypeLiteral<>() {}));
	}
}
