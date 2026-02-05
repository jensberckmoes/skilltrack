package com.sopra_steria.jens_berckmoes.bdd.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.sopra_steria.jens_berckmoes.bdd")
public class PrimaryTestRunner {
}
