package se.akerstrom.spector.core.example;

public class Year
{
    private final int year;

    public Year(int year)
    {
        this.year = year;
    }

    public boolean isLeapYear()
    {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
