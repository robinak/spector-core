package se.akerstrom.spector.core.specification.yaml;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import se.akerstrom.spector.core.specification.Fixture;
import se.akerstrom.spector.core.specification.Specification;
import se.akerstrom.spector.core.specification.binding.SpecificationBinding;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ParsedYamlSpecification implements Specification
{
    private final Specification specification;

    public ParsedYamlSpecification(File yamlSpecification) {
        requireNonNull(yamlSpecification);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Specification specification = mapper.readValue(yamlSpecification, SpecificationBinding.class);
            this.specification = specification;

        } catch (IOException e) {
            throw new YamlSpecificationParseFailure("Could not parse specification file: " + yamlSpecification.getPath(), e);
        }
    }


    @Override
    public String getDescription()
    {
        return specification.getDescription();
    }

    @Override
    public String getUnit()
    {
        return specification.getUnit();
    }

    @Override
    public Set<Fixture> getFixtures()
    {
        return specification.getFixtures();
    }

    @Override
    public String toString()
    {
        return "ParsedYamlSpecification{" +
                "specification=" + specification +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedYamlSpecification that = (ParsedYamlSpecification) o;

        return specification.equals(that.specification);

    }

    @Override
    public int hashCode()
    {
        return specification.hashCode();
    }
}
