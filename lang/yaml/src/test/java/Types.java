import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedParameterizedType;
import java.util.Arrays;
import java.util.List;

public class Types {
    @TypeAnnotation1
    private List<@TypeAnnotation1 @TypeAnnotation2 String> stringList;

    @Test
    public void getParams() throws NoSuchFieldException {
        AnnotatedParameterizedType type = (AnnotatedParameterizedType) getClass().getDeclaredField("stringList").getAnnotatedType();


        System.out.println("Type: " + type);
        System.out.println("Type class: " + type.getClass());


        System.out.println(Arrays.toString(type.getAnnotations()));

        System.out.println("Type argument 1 annotations: " + Arrays.toString(type.getAnnotatedActualTypeArguments()[0].getAnnotations()));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    public @interface TypeAnnotation1 {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    public @interface TypeAnnotation2 {

    }
}
