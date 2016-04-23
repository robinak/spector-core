package se.akerstrom.spector.core.specification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public interface Proposition
{
    String getProposition();
    Map<String, Object> getGiven();
    String getAction();
    Object getExpected();
}
