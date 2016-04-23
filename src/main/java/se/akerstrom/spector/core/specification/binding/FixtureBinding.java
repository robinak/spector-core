package se.akerstrom.spector.core.specification.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.akerstrom.spector.core.specification.Fixture;
import se.akerstrom.spector.core.specification.Proposition;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

public class FixtureBinding implements Fixture
{
    private final String definition;
    private final Set<Proposition> propositions;

    private FixtureBinding(
            @JsonProperty("definition") String definition,
            @JsonProperty("propositions") Set<PropositionBinding> propositions
    )
    {
        this.definition = requireNonNull(definition);
        this.propositions = new HashSet<>(requireNonNull(propositions));
    }

    @Override
    public String getDefinition()
    {
        return definition;
    }

    @Override
    public Set<Proposition> getPropositions()
    {
        return unmodifiableSet(propositions);
    }


    @Override
    public String toString()
    {
        return "FixtureBinding{" +
                "definition='" + definition + '\'' +
                ", propositions=" + propositions +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FixtureBinding that = (FixtureBinding) o;

        if (!definition.equals(that.definition)) return false;
        return propositions.equals(that.propositions);

    }

    @Override
    public int hashCode()
    {
        int result = definition.hashCode();
        result = 31 * result + propositions.hashCode();
        return result;
    }
}
