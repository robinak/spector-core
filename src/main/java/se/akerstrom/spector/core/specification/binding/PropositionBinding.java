package se.akerstrom.spector.core.specification.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.akerstrom.spector.core.specification.Proposition;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

public class PropositionBinding implements Proposition
{
    @JsonProperty private final String proposition;
    @JsonProperty private final Map<String, Object> given;
    @JsonProperty private final String action;
    @JsonProperty private final Object expected;

    public PropositionBinding(
            @JsonProperty("proposition") String proposition,
            @JsonProperty("given") Map<String, Object> given,
            @JsonProperty("action") String action,
            @JsonProperty("expected") Object expected)
    {
        this.proposition = requireNonNull(proposition);
        this.given = given == null ? emptyMap() : new HashMap<>(given);
        this.action = requireNonNull(action);
        this.expected = requireNonNull(expected);
    }

    @Override
    public String getProposition()
    {
        return proposition;
    }

    @Override
    public Map<String, Object> getGiven()
    {
        return unmodifiableMap(given);
    }

    @Override
    public String getAction()
    {
        return action;
    }

    public Object getExpected()
    {
        return expected;
    }

    @Override
    public String toString()
    {
        return "PropositionBinding{" +
                "proposition='" + proposition + '\'' +
                ", given=" + given +
                ", action='" + action + '\'' +
                ", expected=" + expected +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropositionBinding that = (PropositionBinding) o;

        if (!proposition.equals(that.proposition)) return false;
        if (given != null ? !given.equals(that.given) : that.given != null) return false;
        if (!action.equals(that.action)) return false;
        return expected.equals(that.expected);

    }

    @Override
    public int hashCode()
    {
        int result = proposition.hashCode();
        result = 31 * result + (given != null ? given.hashCode() : 0);
        result = 31 * result + action.hashCode();
        result = 31 * result + expected.hashCode();
        return result;
    }
}
