package se.akerstrom.spector.core.example;

import org.junit.runner.RunWith;
import se.akerstrom.spector.core.specification.location.SpectorSpecificationPath;
import se.akerstrom.spector.core.testrunner.SpectorJunitTestRunner;

@RunWith(SpectorJunitTestRunner.class)
@SpectorSpecificationPath("se/akerstrom/spector/core/example/leap-year-example.yaml")
public class SingleSpecification
{
}
