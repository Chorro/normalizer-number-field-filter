package com.github.chorro.normalizer.filter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class NumberFieldFilterTest {

  @Test
  public void test() {

    Map<String, Object> properties = new HashMap<>();
    properties.put("dimension", "timestamp");

    Map<String, Object> comparisonRules = new HashMap<>();
    comparisonRules.put("ne", 0);

    properties.put("comparisonRules", comparisonRules);

    NumberFieldFilter numberFieldFilter = new NumberFieldFilter();
    numberFieldFilter.prepare(properties, null);

    Map<String, Object> message = new HashMap<>();
    message.put("timestamp", 0L);

    assertTrue(numberFieldFilter.process(null, message));

  }

}
