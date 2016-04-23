package se.akerstrom.spector.core.specification.location;

import org.junit.runner.RunWith;
import se.akerstrom.spector.core.testrunner.SpectorJunitTestRunner;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@RunWith(SpectorJunitTestRunner.class)
public @interface SpectorSpecificationPath
{
    /**
     * @return the path to the Spector specification file (*.yaml)
     */
    String value();
}
