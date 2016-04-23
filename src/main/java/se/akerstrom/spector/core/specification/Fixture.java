package se.akerstrom.spector.core.specification;

import java.util.Set;

public interface Fixture
{
    String getDefinition();
    Set<Proposition> getPropositions();
}
