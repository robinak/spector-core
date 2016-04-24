package se.akerstrom.spector.core.specification.provider;

import se.akerstrom.spector.core.specification.configuration.SpectorConfiguration;
import se.akerstrom.spector.core.specification.configuration.SpectorSpecificationPath;
import se.akerstrom.spector.core.specification.Specification;
import se.akerstrom.spector.core.specification.yaml.ParsedYamlSpecification;
import se.akerstrom.spector.core.specification.yaml.YamlSpecificationParseFailure;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ParsedYamlSpecification::new);
    }

    static Optional<SpectorSpecificationPath> findSpectorPathAnnotation(Class<?> testClass)
    {
        return Optional.ofNullable(testClass.getAnnotation(SpectorSpecificationPath.class));
    }

    static Stream<Path> streamPathsFromRootAnnotation(SpectorConfiguration annotation)
    {
        return Optional.of(annotation)
                .map(SpectorConfiguration::specificationsRoot)
                .map(Specifications::pathsFromRoot)
                .get()
                .filter(Files::isRegularFile)
                .filter(isYamlPath);
    }

    static Optional<SpectorConfiguration> findSpectorRootAnnotation(Class<?> testClass)
    {
        return Optional.ofNullable(testClass.getAnnotation(SpectorConfiguration.class));
    }

    static Stream<Path> pathsFromRoot(String root)
    {
        Optional<File> rootDirectory = resourcePathToFile(root);

        if (!rootDirectory.isPresent()) {
            return Stream.empty();
        }

        try {
            return Files.walk(rootDirectory.get().toPath());

        } catch (IOException e) {
            throw new YamlSpecificationParseFailure("Unable to parse specifications in root: " + root, e);
        }
    }

    static Optional<File> resourcePathToFile(String path)
    {
        URL url = Specifications.class.getClassLoader().getResource(path);

        if (url == null) {
            return Optional.empty();
        }
        File file = new File(url.getFile());
        return Optional.of(file);
    }

}
