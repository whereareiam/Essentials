package me.whereareiam.socialismus.module.essentials;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.registry.base.Registry;

@RequiredArgsConstructor
public class EssentialsInjectorConfiguration extends AbstractModule {
	private final Injector parentInjector;
	private final Registry<Reloadable> reloadableRegistry;

	@Override
	protected void configure() {
		bind(new TypeLiteral<Registry<Reloadable>>() {}).toInstance(reloadableRegistry);
		
		// Request injection from parent injector for services we need
		requestInjection(parentInjector);
	}
}
