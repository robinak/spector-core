package se.akerstrom.spector.core.specification.provider;

import se.akerstrom.spector.core.specification.location.SpectorSpecificationPath;
import se.akerstrom.spector.core.specification.location.SpectorSpecificationsRoot;
import se.akerstrom.spector.core.specification.Specification;
import se.akerstrom.spector.core.specification.yaml.ParsedYamlSpecification;
import se.akerstrom.spector.core.specification.yaml.YamlSpecificationParseFailure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Specifications
{
    private static final Predicate<String> isYamlFilename = (filename) ->
            filename.toUpperCase().endsWith("YML")
                    || filename.toUpperCase().endsWith("YAML");

    private static final Predicate<Path> isYamlPath = (path) ->
            isYamlFilename.test(path.toString());


    public static Set<Specification> findForClass(Class<?> testClass) {
        return Stream.concat(
                        streamForPathAnnotation(testClass),
                        streamForRootAnnotation(testClass))
                .collect(Collectors.toSet());
    }

    static Stream<Specification> streamForPathAnnotation(Class<?> testClass)
    {
        return findSpectorPathAnnotation(testClass)
                .map(Specifications::parseSpecificationFromPathAnnotation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Stream::of)
                .orElse(Stream.empty());
    }

    static Stream<Specification> streamForRootAnnotation(Class<?> testClass)
    {
        return findSpectorRootAnnotation(testClass)
                .map(Specifications::streamPathsFromRootAnnotation)
                .map((stream) -> stream
                        .map((path) -> path.toFile())
                        .map(ParsedYamlSpecification::new)
                        .map(Specification.class::cast))
                .orElse(Stream.empty());
    }

    static Optional<Specification> parseSpecificationFromPathAnnotation(SpectorSpecificationPath annotation)
    {
        return Optional.of(annotation)
                .map(SpectorSpecificationPath::value)
                .filter(isYamlFilename)
                .map(Specifications::resourcePathToFile)
                .map(ParsedYamlSpecification::new);
    }

    static Optional<SpectorSpecificationPath> findSpectorPathAnnotation(Class<?> testClass)
    {
        return Optional.ofNullable(testClass.getAnnotation(SpectorSpecificationPath.class));
    }

    static Stream<Path> streamPathsFromRootAnnotation(SpectorSpecificationsRoot annotation)
    {
        return Optional.of(annotation)
                .map(SpectorSpecificationsRoot::value)
                .map(Specifications::pathsFromRoot)
                .get()
                .filter(Files::isRegularFile)
                .filter(isYamlPath);
    }

    static Optional<SpectorSpecificationsRoot> findSpectorRootAnnotation(Class<?> testClass)
    {
        return Optional.ofNullable(testClass.getAnnotation(SpectorSpecificationsRoot.class));
    }

    static Stream<Path> pathsFromRoot(String root)
    {
        File rootDirectory = resourcePathToFile(root);
        try {
            return Files.walk(rootDirectory.toPath());

        } catch (IOException e) {
            throw new YamlSpecificationParseFailure("Unable to parse specifications in root: " + root, e);
        }
    }

    static File resourcePathToFile(String path)
    {
        return new File(Specifications.class.getClassLoader().getResource(path).getFile());
    }

}
