package com.pharmapack.automation;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

/** Runs every feature in src/test/resources/features against the running API (http://localhost:8080). */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.pharmapack.automation.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json, junit:target/cucumber-reports/cucumber.xml")
public class RunCucumberTest {
}
