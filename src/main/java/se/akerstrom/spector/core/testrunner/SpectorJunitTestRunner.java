package se.akerstrom.spector.core.testrunner;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import se.akerstrom.spector.core.specification.provider.Specifications;
import se.akerstrom.spector.core.specification.Fixture;
import se.akerstrom.spector.core.specification.Proposition;
import se.akerstrom.spector.core.specification.Specification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class SpectorJunitTestRunner extends Runner
{
    private final Class<?> testClass;

    public SpectorJunitTestRunner(Class<?> testClass) throws InitializationError
    {
        this.testClass = testClass;
    }

    public Description getDescription()
    {
        return Description.createSuiteDescription(testClass);
    }

    public void run(RunNotifier notifier)
    {
        Set<Specification> specifications = Specifications.findForClass(testClass);

        if (specifications.isEmpty()) {
            throw new SpecificationNotFound();
        }
        runAllTests(specifications, notifier);
    }

    void runAllTests(Collection<Specification> specifications, RunNotifier notifier)
    {
        specifications.stream()
                .forEach((spec) -> runForSpecification(spec, notifier));
    }

    void runForSpecification(Specification specification, RunNotifier notifier)
    {
        specification.getFixtures().stream()
                .forEach((fixture) -> runForFixture(specification, fixture, notifier));
    }

    void runForFixture(Specification specification, Fixture fixture, RunNotifier notifier)
    {
        fixture.getPropositions().stream()
                .peek((proposition) -> notifyTestStart(specification, fixture, proposition, notifier))
                .peek((proposition) -> runForProposition(specification, fixture, proposition, notifier))
                .forEach((proposition) -> notifyTestEnd(specification, fixture, proposition, notifier));
    }

    void runForProposition(Specification specification, Fixture fixture, Proposition proposition, RunNotifier notifier)
    {

        try {
            Class<?> unit = Class.forName(specification.getUnit());
            Object instance = unit.getDeclaredConstructor(int.class).newInstance((Integer) proposition.getGiven().entrySet().iterator().next().getValue());

            Arrays.stream(unit.getMethods())
                    .filter((method) -> method.getName().equals(proposition.getAction()))
                    .findFirst()
                    .map((method) -> invoke(instance, method))
                    .filter((returnValue) -> !proposition.getOutput().equals(returnValue))
                    .ifPresent((returnValue) -> notifyTestFailure(specification, fixture, proposition, returnValue, notifier));

        } catch (ClassNotFoundException e) {
            throw new UnitNotFound(e);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RunFailure(e);
        }

    }

    void notifyTestStart(Specification specification, Fixture fixture, Proposition proposition, RunNotifier notifier)
    {
        notifier.fireTestStarted(describe(specification, fixture, proposition));
    }

    void notifyTestEnd(Specification specification, Fixture fixture, Proposition proposition, RunNotifier notifier)
    {
        notifier.fireTestFinished(describe(specification, fixture, proposition));
    }

    void notifyTestFailure(Specification specification, Fixture fixture, Proposition proposition, Object returnValue, RunNotifier notifier)
    {
        Description description = describe(specification, fixture, proposition);
        AssertionError assertionError = new AssertionError("Expected: " + proposition.getOutput() + ", but was: " + returnValue);
        Failure failure = new Failure(description, assertionError);
        notifier.fireTestFailure(failure);
    }

    Description describe(Specification specification, Fixture fixture, Proposition proposition)
    {
        return Description.createTestDescription(
                testClass,
                createPropositionDescription(specification, fixture, proposition));
    }

    String createPropositionDescription(Specification specification, Fixture fixture, Proposition proposition)
    {
        return truncatePackages(
                specification.getUnit()) + ": "
                + specification.getDescription() + ": "
                + fixture.getDefinition() + ": "
                + proposition.getProposition();
    }

    String truncatePackages(String clazz)
    {
        return clazz.substring(clazz.lastIndexOf(".") + 1);
    }

    Object invoke(Object instance, Method method) {
        try {
            return method.invoke(instance);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RunFailure(e);
        }
    }
}
