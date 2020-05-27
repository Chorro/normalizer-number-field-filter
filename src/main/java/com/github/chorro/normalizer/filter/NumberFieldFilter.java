package com.github.chorro.normalizer.filter;

import io.wizzie.metrics.MetricsManager;
import io.wizzie.normalizer.funcs.FilterFunc;

import java.util.HashMap;
import java.util.Map;

public class NumberFieldFilter extends FilterFunc {
  String dimension;
  Map<NumberComparisonOperator, Number> rules = new HashMap<>();

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
    dimension = (String) properties.get("dimension");
    Map<String, Number> comparisonRules = (Map<String, Number>) properties.get("comparisonRules");
    comparisonRules
      .entrySet()
      .forEach( entry -> {
        rules.put(NumberComparisonOperator.valueOf(entry.getKey().toUpperCase()), entry.getValue());
      });

  }

  @Override public Boolean process(String key, Map<String, Object> value) {
    if (value != null) {
      Object currentValue = value.get(dimension);

      if (currentValue instanceof Number) {
        Number currentValueNumber = (Number) currentValue;
        return rules
          .entrySet()
          .stream()
          .allMatch(entry -> entry.getKey().compare(currentValueNumber, entry.getValue()));
      }
    }

    return false;
  }

  @Override public void stop() {
    // Nothing to do
  }
}
