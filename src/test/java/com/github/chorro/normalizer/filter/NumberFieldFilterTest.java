package com.github.chorro.normalizer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumberFieldFilterTest {
  @Rule public TestName name = new TestName();

  private static ObjectMapper objectMapper = new ObjectMapper();
  private static Map<String, String> testFilesRelationMap = new HashMap<>();
  private static ClassLoader classLoader;
  private Map<String, Object> properties;

  @BeforeClass
  public static void initTests() throws IOException {
    classLoader = Thread.currentThread().getContextClassLoader();
    File file = new File(classLoader.getResource("unitTestsFileRelation.json").getFile());

    testFilesRelationMap = objectMapper.readValue(file, Map.class);
  }

  @Before
  public void setup() throws IOException {
    File file = new File(classLoader.getResource((String) testFilesRelationMap.get(name.getMethodName())).getFile());
    properties = objectMapper.readValue(file, Map.class);
  }

  @Test
  public void givenSimpleFilter_whenFilter_thenCorrect() {
    NumberFieldFilter numberFieldFilter = new NumberFieldFilter();
    numberFieldFilter.prepare(properties, null);

    Map<String, Object> message = new HashMap<>();

    message.put("DIM-A", 4);
    assertTrue(numberFieldFilter.process(null, message));

    message.put("DIM-A", -1);
    assertFalse(numberFieldFilter.process(null, message));
  }

  @Test
  public void givenMultipleNumberFilters_whenFilter_thenCorrect() {
    NumberFieldFilter numberFieldFilter = new NumberFieldFilter();
    numberFieldFilter.prepare(properties, null);

    Map<String, Object> message = new HashMap<>();

    message.put("DIM-A", 4);
    assertTrue(numberFieldFilter.process(null, message));

    message.put("DIM-A", -1);
    assertFalse(numberFieldFilter.process(null, message));

    message.put("DIM-A", 11);
    assertFalse(numberFieldFilter.process(null, message));
  }

  @Test
  public void givenMultipleDimensionsAndFilters_whenFilter_thenCorrect() {
    NumberFieldFilter numberFieldFilter = new NumberFieldFilter();
    numberFieldFilter.prepare(properties, null);

    Map<String, Object> message = new HashMap<>();

    message.put("DIM-A", 1);
    message.put("DIM-B", 10);
    assertFalse(numberFieldFilter.process(null, message));

    message.put("DIM-A", 0);
    message.put("DIM-B", 0);
    assertFalse(numberFieldFilter.process(null, message));

    message.put("DIM-A", 7);
    message.put("DIM-B", 0);
    assertFalse(numberFieldFilter.process(null, message));

    message.put("DIM-A", 0);
    message.put("DIM-B", -1);
    assertTrue(numberFieldFilter.process(null, message));
  }

}
