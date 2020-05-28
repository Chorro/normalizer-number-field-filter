package com.github.chorro.normalizer.filter;

import io.wizzie.metrics.MetricsManager;
import io.wizzie.normalizer.funcs.FilterFunc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberFieldFilter extends FilterFunc {
  Map<String, Map<NumberComparisonOperator, Number>> rules = new HashMap<>();

  private enum NumberComparisonOperator implements NumberComparison {
    EQ {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() == second.doubleValue();
      }
    },
    NE {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() != second.doubleValue();
      }
    },
    GT {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() > second.doubleValue();
      }
    },
    GE {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() >= second.doubleValue();
      }
    },
    LT {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() < second.doubleValue();
      }
    },
    LE {
      @Override
      public boolean compare(Number first, Number second) {
        return first.doubleValue() <= second.doubleValue();
      }
    }
  }

  @Override public void prepare(Map<String, Object> properties, MetricsManager metricsManager) {
    List<Map<String, Object>> rules = (List<Map<String, Object>>) properties.get("rules");
    rules.stream().forEach(rule -> {
      if (rule.containsKey("dimension") && rule.containsKey("comparisonRules")) {
           String dimension = (String) rule.get("dimension");
           Map<NumberComparisonOperator, Number> comparisonRules = new HashMap<>();

          ((Map<String, Number>) rule.get("comparisonRules"))
            .entrySet()
            .forEach(entry -> comparisonRules.put(NumberComparisonOperator.valueOf(entry.getKey().toUpperCase()), entry.getValue()));

          this.rules.put(dimension, comparisonRules);
      }
    });
  }

  @Override public Boolean process(String key, Map<String, Object> value) {
    if (value != null) {

      return this.rules.entrySet().stream().allMatch(
        entry -> {
          Object currentValue = value.get(entry.getKey());

          return currentValue instanceof Number
                 && entry
                      .getValue()
                      .entrySet()
                      .stream()
                      .allMatch(entry2 -> entry2.getKey().compare((Number) currentValue, entry2.getValue()));
        }
      );
    }

    return false;
  }

  @Override public void stop() {
    // Nothing to do
  }
}
