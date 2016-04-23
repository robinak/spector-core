package se.akerstrom.spector.core.specification.location;

import org.junit.runner.RunWith;
import se.akerstrom.spector.core.testrunner.SpectorJunitTestRunner;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@RunWith(SpectorJunitTestRunner.class)
public @interface SpectorSpecificationsRoot
{
    /**
     * @return the root folder path to scan for Spector specification files (*.yaml)
     */
    String value();
}
