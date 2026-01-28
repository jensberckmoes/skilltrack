package com.sopra_steria.jens_berckmoes.BDD.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.sopra_steria.jens_berckmoes.BDD.steps")
public class PrimaryTestRunner {
}
