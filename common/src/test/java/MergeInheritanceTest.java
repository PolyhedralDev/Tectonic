import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Merge;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicTemplate;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicValue;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.InvalidTemplateException;
import com.dfsek.tectonic.api.exception.ValueMissingException;
import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.api.preprocessor.Result;
import com.dfsek.tectonic.api.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.impl.MapConfiguration;
import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.impl.loading.loaders.generic.HashMapLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MergeInheritanceTest {
    // ---- config helpers -------------------------------------------------------------------------------------------

    private static Configuration config(String id, boolean isAbstract, List<String> extend, Map<String, Integer> m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        if(isAbstract) map.put("abstract", true);
        if(!extend.isEmpty()) map.put("extends", new ArrayList<>(extend));
        if(m != null) map.put("m", new LinkedHashMap<>(m));
        return new MapConfiguration(map, id);
    }

    private static AbstractConfiguration resolve(String id, Configuration... configs) {
        return new AbstractConfigLoader()
            .loadConfigs(Arrays.asList(configs))
            .stream()
            .filter(c -> c.getID().equals(id))
            .findFirst()
            .orElseThrow();
    }

    /** grandparent {a:1, b:1} &lt;- parent {b:2, c:2} &lt;- child {c:3} */
    private static AbstractConfiguration chain() {
        return resolve("child",
            config("grandparent", true, List.of(), Map.of("a", 1, "b", 1)),
            config("parent", true, List.of("grandparent"), Map.of("b", 2, "c", 2)),
            config("child", false, List.of("parent"), Map.of("c", 3)));
    }

    private static Map<String, Integer> map(Object... kv) {
        Map<String, Integer> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], (Integer) kv[i + 1]);
        return m;
    }

    // ---- templates ------------------------------------------------------------------------------------------------

    private static final class Merged implements ConfigTemplate {
        @Value("m")
        @Merge
        private Map<String, Integer> m;
    }


    private static final class Replaced implements ConfigTemplate {
        @Value("m")
        private Map<String, Integer> m;
    }


    private static final class MergedTree implements ConfigTemplate {
        @Value("m")
        @Merge
        private TreeMap<String, Integer> m;
    }


    private static final class MergedDefault implements ConfigTemplate {
        @Value("missing")
        @Merge
        @Default
        private Map<String, Integer> m = null;
    }


    private static final class MergedMissing implements ConfigTemplate {
        @Value("missing")
        @Merge
        private Map<String, Integer> m;
    }


    private static final class MergedFinal implements ConfigTemplate {
        @Value("m")
        @Merge
        @Final
        private Map<String, Integer> m;
    }


    private static final class MergedScalar implements ConfigTemplate {
        @Value("id")
        @Merge
        private String id;
    }


    private static final class MergedList implements ConfigTemplate {
        @Value("l")
        @Merge
        private List<Integer> l;
    }


    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Counted {
    }


    private static final class MergedCounted implements ConfigTemplate {
        @Value("m")
        @Merge
        private @Counted Map<String, Integer> m;
    }


    /** Records every raw value it sees, and adds key "pre" to each, to prove its result is what gets merged. */
    private static final class CountingPreprocessor implements ValuePreprocessor<Counted> {
        private final List<Object> seen = new ArrayList<>();

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Counted annotation, DepthTracker tracker) {
            seen.add(new LinkedHashMap<>((Map<String, Object>) c));
            Map<String, Object> copy = new LinkedHashMap<>((Map<String, Object>) c);
            copy.put("pre", seen.size());
            return (Result<T>) Result.overwrite(copy, tracker);
        }
    }

    // ---- getAll ---------------------------------------------------------------------------------------------------

    @Test
    public void getAllOrdersFarthestFirstBaseLast() {
        assertEquals(List.of(Map.of("a", 1, "b", 1), Map.of("b", 2, "c", 2), Map.of("c", 3)), chain().getAll("m"));
    }

    @Test
    public void getAllSkipsLayersWithoutKey() {
        AbstractConfiguration config = resolve("child",
            config("grandparent", true, List.of(), Map.of("a", 1)),
            config("parent", true, List.of("grandparent"), null),
            config("child", false, List.of("parent"), Map.of("c", 3)));
        assertEquals(List.of(Map.of("a", 1), Map.of("c", 3)), config.getAll("m"));
        assertEquals(List.of(), config.getAll("missing"));
    }

    // ---- brief tests ----------------------------------------------------------------------------------------------

    @Test
    public void mergesAcrossThreeLayersNearestWins() {
        Merged merged = new ConfigLoader().load(new Merged(), chain());
        assertEquals(map("a", 1, "b", 2, "c", 3), merged.m);
    }

    @Test
    public void sameDepthFirstParentWins() {
        AbstractConfiguration config = resolve("child",
            config("p1", true, List.of(), Map.of("shared", 1, "only1", 1)),
            config("p2", true, List.of(), Map.of("shared", 2, "only2", 2)),
            config("child", false, List.of("p1", "p2"), Map.of("own", 3)));

        // get() picks the first prototype in a layer; the merge must agree.
        AbstractConfiguration withoutOwn = resolve("child",
            config("p1", true, List.of(), Map.of("shared", 1, "only1", 1)),
            config("p2", true, List.of(), Map.of("shared", 2, "only2", 2)),
            config("child", false, List.of("p1", "p2"), null));
        assertEquals(Map.of("shared", 1, "only1", 1), withoutOwn.get("m"));

        Merged merged = new ConfigLoader().load(new Merged(), config);
        assertEquals(map("shared", 1, "only1", 1, "only2", 2, "own", 3), merged.m);
    }

    @Test
    public void childWithoutExtendsBehavesLikeToday() {
        AbstractConfiguration config = resolve("child", config("child", false, List.of(), Map.of("a", 1, "b", 2)));
        Merged merged = new ConfigLoader().load(new Merged(), config);
        Replaced replaced = new ConfigLoader().load(new Replaced(), config);
        assertEquals(replaced.m, merged.m);
        assertEquals(map("a", 1, "b", 2), merged.m);
    }

    @Test
    public void withoutMergeChildReplacesWholeMap() {
        Replaced replaced = new ConfigLoader().load(new Replaced(), chain());
        assertEquals(map("c", 3), replaced.m);

        AbstractConfiguration inheritsOnly = resolve("child",
            config("parent", true, List.of(), Map.of("b", 2, "c", 2)),
            config("child", false, List.of("parent"), null));
        assertEquals(map("b", 2, "c", 2), new ConfigLoader().load(new Replaced(), inheritsOnly).m);
    }

    @Test
    public void preprocessorRunsOncePerLayer() {
        CountingPreprocessor preprocessor = new CountingPreprocessor();
        ConfigLoader loader = new ConfigLoader().registerPreprocessor(Counted.class, preprocessor);

        MergedCounted merged = loader.load(new MergedCounted(), chain());

        assertEquals(List.of(Map.of("a", 1, "b", 1), Map.of("b", 2, "c", 2), Map.of("c", 3)), preprocessor.seen);
        // Each layer's preprocessed map is what gets merged: the child's ("pre" = 3) wins.
        assertEquals(map("a", 1, "b", 2, "pre", 3, "c", 3), merged.m);
    }

    @Test
    public void mergeWithFinalThrowsAtLoad() {
        assertThrows(InvalidTemplateException.class, () -> new ConfigLoader().load(new MergedFinal(), chain()));
        // Also on a plain, non-abstract configuration: the template itself is invalid.
        assertThrows(InvalidTemplateException.class,
            () -> new ConfigLoader().load(new MergedFinal(), config("child", false, List.of(), Map.of("c", 3))));
    }

    // ---- further semantics ----------------------------------------------------------------------------------------

    @Test
    public void mergeOnNonMapOrListThrowsAtLoad() {
        assertThrows(InvalidTemplateException.class, () -> new ConfigLoader().load(new MergedScalar(), chain()));
    }

    // ---- lists ----------------------------------------------------------------------------------------------------

    private static Configuration listConfig(String id, boolean isAbstract, List<String> extend, List<Integer> l) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        if(isAbstract) map.put("abstract", true);
        if(!extend.isEmpty()) map.put("extends", new ArrayList<>(extend));
        if(l != null) map.put("l", new ArrayList<>(l));
        return new MapConfiguration(map, id);
    }

    @Test
    public void listsConcatenateFarthestFirst() {
        AbstractConfiguration config = resolve("child",
            listConfig("grandparent", true, List.of(), List.of(1)),
            listConfig("parent", true, List.of("grandparent"), List.of(2, 3)),
            listConfig("child", false, List.of("parent"), List.of(4)));

        List<Integer> merged = new ConfigLoader().load(new MergedList(), config).l;

        assertEquals(List.of(1, 2, 3, 4), merged);
        assertInstanceOf(ArrayList.class, merged);
    }

    /** Same order as maps: the first prototype in {@code extends} counts as nearest, so its elements come later. */
    @Test
    public void listsAtSameDepthPutTheFirstParentLast() {
        AbstractConfiguration config = resolve("child",
            listConfig("p1", true, List.of(), List.of(1)),
            listConfig("p2", true, List.of(), List.of(2)),
            listConfig("child", false, List.of("p1", "p2"), List.of(3)));

        assertEquals(List.of(2, 1, 3), new ConfigLoader().load(new MergedList(), config).l);
    }

    @Test
    public void listDiamondAncestorContributesOnce() {
        AbstractConfiguration config = resolve("child",
            listConfig("root", true, List.of(), List.of(0)),
            listConfig("left", true, List.of("root"), List.of(1)),
            listConfig("right", true, List.of("root"), List.of(2)),
            listConfig("child", false, List.of("left", "right"), null));

        assertEquals(List.of(0, 2, 1), new ConfigLoader().load(new MergedList(), config).l);
    }

    @Test
    public void listWithoutInheritanceLoadsNormally() {
        assertEquals(List.of(5, 6),
            new ConfigLoader().load(new MergedList(), listConfig("child", false, List.of(), List.of(5, 6))).l);
    }

    @Test
    public void interfaceFieldGetsLinkedHashMapAndConcreteFieldKeepsItsClass() {
        assertInstanceOf(LinkedHashMap.class, new ConfigLoader().load(new Merged(), chain()).m);
        HashMapLoader hashMapLoader = new HashMapLoader();
        ConfigLoader loader = new ConfigLoader()
            .registerLoader(TreeMap.class, (TypeLoader<TreeMap<Object, Object>>) (t, c, l, d) -> new TreeMap<>(hashMapLoader.load(t, c, l, d)));
        MergedTree tree = loader.load(new MergedTree(), chain());
        assertInstanceOf(TreeMap.class, tree.m);
        assertEquals(map("a", 1, "b", 2, "c", 3), tree.m);
    }

    @Test
    public void missingKeyUsesDefaultOrThrows() {
        assertNull(new ConfigLoader().load(new MergedDefault(), chain()).m);
        assertThrows(ValueMissingException.class, () -> new ConfigLoader().load(new MergedMissing(), chain()));
    }

    @Test
    public void diamondAncestorLoadsOnce() {
        CountingPreprocessor preprocessor = new CountingPreprocessor();
        ConfigLoader loader = new ConfigLoader().registerPreprocessor(Counted.class, preprocessor);
        AbstractConfiguration config = resolve("child",
            config("root", true, List.of(), Map.of("r", 0)),
            config("left", true, List.of("root"), Map.of("l", 1)),
            config("right", true, List.of("root"), Map.of("r", 2)),
            config("child", false, List.of("left", "right"), null));

        MergedCounted merged = loader.load(new MergedCounted(), config);

        assertEquals(3, preprocessor.seen.size());
        assertEquals(2, merged.m.get("r")); // right (depth 1) beats root (depth 2)
        assertEquals(1, merged.m.get("l"));
    }

    @Test
    public void plainConfigurationLoadsNormally() {
        Merged merged = new ConfigLoader().load(new Merged(), config("child", false, List.of(), Map.of("c", 3)));
        assertEquals(map("c", 3), merged.m);
    }

    @Test
    public void dynamicValueMerge() throws NoSuchFieldException {
        AnnotatedType type = Merged.class.getDeclaredField("m").getAnnotatedType();
        @SuppressWarnings({ "rawtypes", "unchecked" })
        DynamicTemplate template = DynamicTemplate.builder()
            .value("merged", DynamicValue.builder("m", (Class<Map>) (Class) Map.class).annotatedType(type).merge().build())
            .value("replaced", DynamicValue.builder("m", (Class<Map>) (Class) Map.class).annotatedType(type).build())
            .build();

        new ConfigLoader().load(template, chain());

        assertEquals(map("a", 1, "b", 2, "c", 3), template.get("merged", Map.class));
        assertEquals(map("c", 3), template.get("replaced", Map.class));
    }

    @Test
    public void dynamicValueMergeWithFinalThrows() {
        assertThrows(IllegalStateException.class, () -> DynamicValue.builder("m", Map.class).setFinal().merge().build());
    }
}
