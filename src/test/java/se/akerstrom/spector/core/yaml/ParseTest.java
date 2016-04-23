package se.akerstrom.spector.core.yaml;

import org.junit.Test;
import se.akerstrom.spector.core.specification.yaml.ParsedYamlSpecification;

import java.io.File;

public class ParseTest
{
    @Test
    public void specification_file_can_be_parsed()
    {
        String path = "se/akerstrom/spector/core/example/leap-year-example.yaml";
        File file = new File(this.getClass().getClassLoader().getResource(path).getFile());
        ParsedYamlSpecification parsedYamlSpecification = new ParsedYamlSpecification(file);
    }
}
