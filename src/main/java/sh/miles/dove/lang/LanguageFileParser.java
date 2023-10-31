package sh.miles.dove.lang;

import sh.miles.dove.collection.Pair;
import sh.miles.dove.lang.replacement.Replacer;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helps in building a LanguageFile class
 */
public final class LanguageFileParser {

    public static final String LIST_START_CHARACTER = "<=";
    public static final String LIST_END_CHARACTER = "!>";
    public static final String ASSIGNMENT_CHARACTER = "=";

    public static Map<String, LanguageComponent> parse(Path path) throws IOException {
        final Map<String, LanguageComponent> components = new HashMap<>();
        final BufferedReader reader = Files.newBufferedReader(path);
        String line;
        while ((line = reader.readLine()) != null) {
            final Pair<String, LanguageComponent> list;
            if (line.endsWith(LIST_START_CHARACTER)) {
                list = parseList(reader, line);
            } else {
                list = parseLine(line);
            }
            components.put(list.first(), list.second());
        }
        reader.close();
        return components;
    }

    private static Pair<String, LanguageComponent> parseLine(String line) {
        final String[] split = line.split(ASSIGNMENT_CHARACTER);
        if (split.length != 2) {
            throw new IllegalStateException("The provided line is formatted illegally format must be key %s value".formatted(ASSIGNMENT_CHARACTER));
        }
        final String key = split[0].stripTrailing();
        String value = split[1].stripLeading();
        if (value.equalsIgnoreCase("NULL")) {
            value = "";
        }

        return Pair.of(key, new LanguageComponent(new String[]{value}, false, Replacer.inStrings(value)));
    }

    private static Pair<String, LanguageComponent> parseList(final BufferedReader reader, String start) throws IOException {
        final List<String> contents = new ArrayList<>(2);
        String line;
        boolean end = false;
        while ((line = reader.readLine()) != null && !end) {
            if (line.startsWith(LIST_END_CHARACTER)) {
                end = true;
                continue;
            }

            contents.add(line);
        }

        if (!end) {
            throw new EOFException("End of file reached but list was not closed. Was this a mistake? Lists should be closed with \"%s\"".formatted(LIST_END_CHARACTER));
        }

        final String[] contentsArray = contents.toArray(String[]::new);
        return Pair.of(start.split(LIST_START_CHARACTER)[0], new LanguageComponent(contentsArray, true, Replacer.inStrings(contentsArray)));
    }

}
