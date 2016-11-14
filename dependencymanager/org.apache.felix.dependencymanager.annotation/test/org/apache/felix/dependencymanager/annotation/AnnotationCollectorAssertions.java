package org.apache.felix.dependencymanager.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.felix.dependencymanager.annotation.AnnotationCollectorTestComponents.AnnotatedComponentWithCustomProperties;
import org.apache.felix.dm.annotation.plugin.bnd.AnnotationCollector;
import org.apache.felix.dm.annotation.plugin.bnd.EntryParam;
import org.apache.felix.dm.annotation.plugin.bnd.EntryType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnnotationCollectorAssertions {

    private JSONObject m_componentJsonObject;

    public AnnotationCollectorAssertions(AnnotationCollector annotationCollector) throws IOException, JSONException {
        if (annotationCollector.finish()) {
            String jsonString = null;
            if (annotationCollector.finish()) {
                jsonString = writeJsonToString(annotationCollector);
            }

            List<JSONObject> jsonObjects = readJsonString(jsonString);
            m_componentJsonObject = jsonObjects.get(0);
        }
    }

    public void assertStringProperty(String expected, String property) throws JSONException {
        JSONObject jsonProperties = m_componentJsonObject.getJSONObject(EntryParam.properties.toString());
        assertEquals(property, expected, jsonProperties.getString(property));
    }

    public void assertStringArray(String[] expected, String property) throws JSONException {
        JSONObject jsonProperties = m_componentJsonObject.getJSONObject(EntryParam.properties.toString());

        for (int i = 0; i < expected.length; i++) {
            assertEquals(property + "["+ i + "]", expected[i], jsonProperties.getJSONArray(property).get(i));
        }
    }

    public void assertIntProperty(int expected, String property) throws JSONException {
        JSONObject jsonProperties = m_componentJsonObject.getJSONObject(EntryParam.properties.toString());
        assertEquals(Integer.class.getName(), jsonProperties.getJSONObject(property).getString("type"));
        assertEquals(property, expected, jsonProperties.getJSONObject(property).getInt("value"));
    }

    public  void assertIntArray(int[] expected, String property) throws JSONException {
        JSONObject jsonProperties = m_componentJsonObject.getJSONObject(EntryParam.properties.toString());
        assertEquals(Integer.class.getName(), jsonProperties.getJSONObject(property).getString("type"));

        for (int i = 0; i < expected.length; i++) {
            assertEquals(property + "["+ i + "]", expected[i], jsonProperties.getJSONObject(property).getJSONArray("value").get(i));
        }
    }

    public void assertImplementationClass(Class<AnnotatedComponentWithCustomProperties> class1) throws JSONException {
        assertEquals(AnnotationCollectorTestComponents.AnnotatedComponentWithCustomProperties.class.getName(), m_componentJsonObject.getString(EntryParam.impl.toString()));
    }

    public void assertEntryType(EntryType entryType) throws JSONException {
        assertEquals(entryType.toString(), m_componentJsonObject.getString("type"));
    }

    public void assertNoProvides() {
        assertFalse(m_componentJsonObject.has("provides"));
    }

    public void assertProvides(Class<?>... expectedProvides) throws JSONException {
        JSONArray jsonArray = m_componentJsonObject.getJSONArray("provides");
        Set<String> actual = new HashSet<>();
        for (int i =0; i < jsonArray.length(); i++ ) {
            actual.add(jsonArray.getString(i));
        }

        Set<String> expectedSet = Arrays.stream(expectedProvides).map(Class::getName).collect(Collectors.toSet());

        assertEquals(expectedSet, actual);
    }

    public void assertNotAComponent() {
        assertNull(m_componentJsonObject);
    }

    private String writeJsonToString(AnnotationCollector annotationCollector) throws IOException {
        String jsonString;
        try (StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter)){

            annotationCollector.writeTo(printWriter);
            jsonString = stringWriter.toString();
        }
        return jsonString;
    }

    private List<JSONObject> readJsonString(String jsonString) throws IOException, JSONException {
        List<JSONObject> result = new ArrayList<>();
        try (StringReader sr = new StringReader(jsonString);
                BufferedReader bufferedReader = new BufferedReader(sr)){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(new JSONObject(line));
            }

        }
        return result;
    }
}
