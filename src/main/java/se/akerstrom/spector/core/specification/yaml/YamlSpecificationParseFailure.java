package se.akerstrom.spector.core.specification.yaml;

public class YamlSpecificationParseFailure extends RuntimeException
{
    public YamlSpecificationParseFailure(String message, Throwable cause)
    {
        super(message, cause);
    }
}
