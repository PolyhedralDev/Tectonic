import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;

public class PreprocessorTest {
    @Test
    public void preprocessor() {
        ConfigLoader loader = new ConfigLoader();
        loader.registerPreprocessor(TestAnnotation.class, new TestPreprocessor());
        loader.registerPreprocessor(TestOverwrite.class, new TestOverwritePreprocessor());
        TestConfig config = new TestConfig();
        loader.load(config, new YamlConfiguration(PreprocessorTest.class.getResourceAsStream("/preprocessor.yml")));
        System.out.println("overwritten things: " + config.overwritten);
    }

    private static final class TestConfig implements ConfigTemplate {
        @Value("thing")
        public String thing;

        @Value("annotated-thing")

        public @TestAnnotation String annotatedThing;

        @Value("annotated-list")
        public List<@TestAnnotation String> annotatedList;

        @Value("overwritten-list")
        public List<@TestAnnotation @TestOverwrite String> overwritten;
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {

    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestOverwrite {

    }

    private static final class TestPreprocessor implements ValuePreprocessor<TestAnnotation> {
        @Override
        @NotNull
        public <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, TestAnnotation annotation) {
            System.out.println("Transforming value: " + c + " of type " + t.getType().getTypeName());
            return Result.noOp();
        }
    }

    private static final class TestOverwritePreprocessor implements ValuePreprocessor<TestOverwrite> {
        @SuppressWarnings("unchecked")
        @Override
        @NotNull
        public <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, TestOverwrite annotation) {
            return (Result<T>) Result.overwrite(c + ":overwritten");
        }
    }
}
