package sh.miles.dove.lang;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageFileTest {

    @Test
    public void testLanguageParse() {
        final LanguageFile file = assertDoesNotThrow(() -> LanguageFile.create("test.lang", Path.of("src/test/resources/")));
        assertEquals(Set.of("list", "key"), file.getKeys());
    }

}
