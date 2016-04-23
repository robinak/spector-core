package se.akerstrom.spector.core.testrunner;

public class RunFailure extends RuntimeException
{
    public RunFailure(Throwable cause)
    {
        super(cause);
    }
}
