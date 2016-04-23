package se.akerstrom.spector.core.specification.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.akerstrom.spector.core.specification.Fixture;
import se.akerstrom.spector.core.specification.Specification;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;


public class SpecificationBinding implements Specification
{
    private final String description;
    private final String unit;
    private final Set<Fixture> fixtures;

    private SpecificationBinding(
            @JsonProperty("description") String description,
            @JsonProperty("unit") String unit,
            @JsonProperty("fixtures") Set<FixtureBinding> fixtures)
    {
        this.description = requireNonNull(description);
        this.unit = requireNonNull(unit);
        this.fixtures = new HashSet<>(requireNonNull(fixtures));
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getUnit()
    {
        return unit;
    }

    @Override
    public Set<Fixture> getFixtures()
    {
        return unmodifiableSet(fixtures);
    }

    @Override
    public String toString()
    {
        return "SpecificationBinding{" +
                "description='" + description + '\'' +
                ", unit=" + unit +
                ", fixtures=" + fixtures +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecificationBinding that = (SpecificationBinding) o;

        if (!description.equals(that.description)) return false;
        if (!unit.equals(that.unit)) return false;
        return fixtures.equals(that.fixtures);

    }

    @Override
    public int hashCode()
    {
        int result = description.hashCode();
        result = 31 * result + unit.hashCode();
        result = 31 * result + fixtures.hashCode();
        return result;
    }

}
