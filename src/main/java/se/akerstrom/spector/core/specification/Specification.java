package se.akerstrom.spector.core.specification;

import java.util.Set;

public interface Specification
{
    String getDescription();
    String getUnit();
    Set<Fixture> getFixtures();
}
