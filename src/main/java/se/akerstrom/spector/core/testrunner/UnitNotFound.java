package se.akerstrom.spector.core.testrunner;

public class UnitNotFound extends RuntimeException
{
    public UnitNotFound(Throwable cause)
    {
        super(cause);
    }
}
