package se.akerstrom.spector.core.yaml;

import org.junit.Test;
import se.akerstrom.spector.core.specification.yaml.ParsedYamlSpecification;

import java.io.File;

public class YamlSpecificationParser
{
    @Test
    public void can_parse_specification_file()
    {
        String path = "se/akerstrom/spector/core/example/leap-year-example.yaml";
        File file = new File(this.getClass().getClassLoader().getResource(path).getFile());
        ParsedYamlSpecification parsedYamlSpecification = new ParsedYamlSpecification(file);
    }
}
