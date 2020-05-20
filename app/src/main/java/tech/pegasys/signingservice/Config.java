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
package tech.pegasys.signingservice;

import tech.pegasys.signingservice.tls.ClientTlsOptions;

import java.nio.file.Path;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.hyperledger.besu.plugin.services.metrics.MetricCategory;

public interface Config {

  Level getLogLevel();

  int getHttpListenPort();

  String getHttpListenHost();

  ClientTlsOptions getTlsOptions();

  Path getConfigDirectory();

  Boolean isMetricsEnabled();

  Integer getMetricsPort();

  String getMetricsNetworkInterface();

  Set<MetricCategory> getMetricCategories();

  Path getDataPath();
}
