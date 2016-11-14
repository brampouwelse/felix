package org.apache.felix.dependencymanager.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Property;

public class AnnotationCollectorTestComponents {

    public interface TestInterfaceOne {}

    public interface TestInterfaceTwo { }

    public static class NotAComponent {}

    @Component
    public static class AnnotatedComponent { }

    @Component(provides = Object.class)
    public static class AnnotatedComponentProvidesObject { }

    @Component
    public static class AnnotatedComponentImplementingInterface implements TestInterfaceOne{}

    @Component(provides = Object.class)
    public static class AnnotatedComponentImplementingInterfaceProvidesObject implements TestInterfaceOne{}

    @Component
    public static class AnnotatedComponentImplementingMultipleInterfaces implements TestInterfaceOne, TestInterfaceTwo{}

    @Component(provides = Object.class)
    public static class AnnotatedComponentImplementingMultipleInterfacesProvidesObject implements TestInterfaceOne, TestInterfaceTwo {}

    @Component(provides = TestInterfaceOne.class)
    public static class AnnotatedComponentImplementingMultipleInterfacesProvidesTestInterfaceOne implements TestInterfaceOne, TestInterfaceTwo {}

    @Retention(RetentionPolicy.CLASS)
    public @interface CustomProperties {

        @Property(name = "string.property")
        String stringProperty();

        @Property(name = "string.array.property")
        String[] stringArrayProperty();

        @Property(name = "int.property")
        int intProperty();

        @Property(name = "int.array.property")
        int[] intArrayProperty();

    }

    @Component
    @CustomProperties(
        stringProperty = "string value",
        stringArrayProperty = { "string", "array" },
        intProperty = Integer.MAX_VALUE,
        intArrayProperty = { Integer.MAX_VALUE, Integer.MIN_VALUE }
        )
    @Property(name="dm.property", value = "Just a normal property")
    public static class AnnotatedComponentWithCustomProperties { }

}
