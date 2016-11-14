package org.apache.felix.dependencymanager.annotation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;

import org.apache.felix.dm.annotation.plugin.bnd.AnnotationCollector;
import org.apache.felix.dm.annotation.plugin.bnd.EntryType;
import org.apache.felix.dm.annotation.plugin.bnd.MetaType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import aQute.bnd.osgi.Builder;
import aQute.bnd.osgi.Clazz;
import aQute.bnd.osgi.Clazz.QUERY;

@RunWith(JUnit4.class)
public class AnnotationCollectorTest {

    private Builder m_builder;
    private AnnotationCollector m_annotationCollector;

    @Before
    public void before() throws Exception {
        m_builder = new Builder();
        m_builder.addClasspath(new File("bin_test"));
        m_builder.setPrivatePackage("org.apache.felix.dependencymanager.annotation");
        m_builder.build();

        m_annotationCollector = new AnnotationCollector(new ConsoleLogger(), m_builder, new MetaType());
    }

    @After
    public void after() {
        if (m_builder != null) {
            m_builder.close();
        }
    }

    @Test
    public void notAComponent() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.NotAComponent.class);
        assertions.assertNotAComponent();
    }

    @Test
    public void annotatedComponent() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponent.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertNoProvides();
    }

    @Test
    public void annotatedComponentProvidesObject() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentProvidesObject.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(Object.class);
    }

    @Test
    public void annotatedComponentImplementingInterface() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentImplementingInterface.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(AnnotationCollectorTestComponents.TestInterfaceOne.class);
    }

    @Test
    public void annotatedComponentImplementingInterfaceProvidesObject() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentImplementingInterfaceProvidesObject.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(Object.class);
    }

    @Test
    public void annotatedComponentImplementingMultipleInterfaces() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentImplementingMultipleInterfaces.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(AnnotationCollectorTestComponents.TestInterfaceOne.class, AnnotationCollectorTestComponents.TestInterfaceTwo.class);
    }

    @Test
    public void annotatedComponentImplementingMultipleInterfacesProvidesObject() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentImplementingMultipleInterfacesProvidesObject.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(Object.class);
    }

    @Test
    public void annotatedComponentImplementingMultipleInterfacesProvidesTestInterfaceOne() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentImplementingMultipleInterfacesProvidesTestInterfaceOne.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertProvides(AnnotationCollectorTestComponents.TestInterfaceOne.class);
    }

    @Test
    public void annotatedComponentWithCustomProperties() throws Exception {
        AnnotationCollectorAssertions assertions = parseClass(AnnotationCollectorTestComponents.AnnotatedComponentWithCustomProperties.class);

        assertions.assertEntryType(EntryType.Component);
        assertions.assertImplementationClass(AnnotationCollectorTestComponents.AnnotatedComponentWithCustomProperties.class);

        assertions.assertStringProperty("string value", "string.property");
        assertions.assertStringArray(new String[]{"string", "array"}, "string.array.property");

        assertions.assertIntProperty(Integer.MAX_VALUE, "int.property");
        assertions.assertIntArray(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, "int.array.property");

        assertions.assertStringProperty("Just a normal property", "dm.property");
    }

    private AnnotationCollectorAssertions parseClass(Class<?> parseClass) throws Exception {
        Clazz bndClazz = getClazz(parseClass);
        bndClazz.parseClassFileWithCollector(m_annotationCollector);

        return new AnnotationCollectorAssertions(m_annotationCollector);
    }

    private Clazz getClazz(Class<?> clazz) throws Exception {
        Collection<Clazz> expanded = m_builder.getClasses("",
            // Parse everything
            QUERY.NAMED.toString(), clazz.getName().replace("$", "."));

        assertEquals(String.format("Cass '%s' not found", clazz), 1, expanded.size());
        return expanded.iterator().next();
    }
}
