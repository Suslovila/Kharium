package tech.funkyra;

import io.github.crucible.grimoire.common.api.grimmix.Grimmix;
import io.github.crucible.grimoire.common.api.grimmix.GrimmixController;
import io.github.crucible.grimoire.common.api.grimmix.lifecycle.IConfigBuildingEvent;
import io.github.crucible.grimoire.common.api.mixin.ConfigurationType;

@Grimmix(id = "Grimmix", name = "Mod name Grimmix")
public class AnticraftGrimmix extends GrimmixController {
	public void buildMixinConfigs(IConfigBuildingEvent event) {
		event.createBuilder("modName.mixin.refmap.json")
			.mixinPackage("tech.funkyra.mixins")
			.serverMixins("server.**")
			.clientMixins("client.**")
			.commonMixins("common.**")
			.setConfigurationPlugin("tech.funkyra.ConfigPlugin")
			.configurationType(ConfigurationType.CORE)
			.refmap("@MIXIN_REFMAP@")
			.verbose(true)
			.required(true)
			.build();
	}
}
