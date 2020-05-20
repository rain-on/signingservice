/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.signingservice.metrics;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.hyperledger.besu.metrics.StandardMetricCategory;

public enum MetricCategory implements org.hyperledger.besu.plugin.services.metrics.MetricCategory {
  HTTP("http"),
  SIGNING("signing");

  private final String name;

  public static final Set<org.hyperledger.besu.plugin.services.metrics.MetricCategory>
      DEFAULT_METRIC_CATEGORIES;

  static {
    DEFAULT_METRIC_CATEGORIES =
        ImmutableSet.<org.hyperledger.besu.plugin.services.metrics.MetricCategory>builder()
            .addAll(EnumSet.allOf(MetricCategory.class))
            .addAll(EnumSet.allOf(StandardMetricCategory.class))
            .build();
  }

  MetricCategory(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Optional<String> getApplicationPrefix() {
    return Optional.empty();
  }
}
