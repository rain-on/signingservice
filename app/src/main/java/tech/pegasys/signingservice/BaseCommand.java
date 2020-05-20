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

import static java.util.Collections.emptySet;
import static tech.pegasys.signingservice.DefaultCommandValues.MANDATORY_HOST_FORMAT_HELP;
import static tech.pegasys.signingservice.DefaultCommandValues.MANDATORY_PATH_FORMAT_HELP;
import static tech.pegasys.signingservice.DefaultCommandValues.MANDATORY_PORT_FORMAT_HELP;

import tech.pegasys.signingservice.tls.ClientTlsOptions;
import tech.pegasys.signingservice.tls.PicoCliClientTlsOptions;

import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Command;
import org.hyperledger.besu.plugin.services.metrics.MetricCategory;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;

@Command(
    description = "This command runs the Signing Service.\n",
    abbreviateSynopsis = true,
    name = "signingservice",
    sortOptions = false,
    // mixinStandardHelpOptions = true,
    // versionProvider = VersionProvider.class,
    header = "Usage:",
    synopsisHeading = "%n",
    descriptionHeading = "%nDescription:%n%n",
    optionListHeading = "%nOptions:%n",
    footerHeading = "%n",
    subcommands = {HelpCommand.class},
    footer = "EthSigner is licensed under the Apache License 2.0")
public class BaseCommand implements Runnable, Config {

  @Option(
      names = {"--logging", "-l"},
      paramLabel = "<LOG VERBOSITY LEVEL>",
      description =
          "Logging verbosity levels: OFF, FATAL, WARN, INFO, DEBUG, TRACE, ALL (default: INFO)")
  private final Level logLevel = Level.INFO;

  @Option(
      names = {"--data-path"},
      description = "The path to a directory to store temporary files",
      paramLabel = DefaultCommandValues.MANDATORY_PATH_FORMAT_HELP,
      arity = "1")
  private Path dataPath;

  @SuppressWarnings("FieldMayBeFinal") // Because PicoCLI requires Strings to not be final.
  @Option(
      names = {"--http-listen-host"},
      description = "Host for HTTP to listen on (default: ${DEFAULT-VALUE})",
      paramLabel = MANDATORY_HOST_FORMAT_HELP,
      arity = "1")
  private String httpListenHost = InetAddress.getLoopbackAddress().getHostAddress();

  @Option(
      names = {"--http-listen-port"},
      description = "Port for HTTP to listen on (default: ${DEFAULT-VALUE})",
      paramLabel = MANDATORY_PORT_FORMAT_HELP,
      arity = "1")
  private final Integer httpListenPort = 8545;

  @Option(
      names = {"-d", "--directory"},
      description = "The path to a directory containing signing metadata TOML files",
      required = true,
      paramLabel = MANDATORY_PATH_FORMAT_HELP,
      arity = "1")
  private Path directoryPath;

  @Option(
      names = {"--metrics-enabled"},
      description = "Set to start the metrics exporter (default: ${DEFAULT-VALUE})")
  private final Boolean metricsEnabled = false;

  @SuppressWarnings({"FieldCanBeFinal", "FieldMayBeFinal"}) // PicoCLI requires non-final Strings.
  @Option(
      names = {"--metrics-host"},
      paramLabel = MANDATORY_HOST_FORMAT_HELP,
      description = "Host for the metrics exporter to listen on (default: ${DEFAULT-VALUE})",
      arity = "1")
  private String metricsHost = InetAddress.getLoopbackAddress().getHostAddress();

  @Option(
      names = {"--metrics-port"},
      paramLabel = MANDATORY_PORT_FORMAT_HELP,
      description = "Port for the metrics exporter to listen on (default: ${DEFAULT-VALUE})",
      arity = "1")
  private final Integer metricsPort = 9001;

  /*
  @Option(
      names = {"--metrics-category", "--metrics-categories"},
      paramLabel = "<category name>",
      split = ",",
      arity = "1..*",
      description =
          "Comma separated list of categories to track metrics for (default: ${DEFAULT-VALUE}),",
      converter = Eth2SignerMetricCategoryConverter.class)
  private final Set<MetricCategory> metricCategories = DEFAULT_METRIC_CATEGORIES;
   */

  @ArgGroup(exclusive = false)
  private PicoCliClientTlsOptions tlsOptions;

  @Override
  public Level getLogLevel() {
    return logLevel;
  }

  @Override
  public int getHttpListenPort() {
    return httpListenPort;
  }

  @Override
  public String getHttpListenHost() {
    return httpListenHost;
  }

  @Override
  public ClientTlsOptions getTlsOptions() {
    return tlsOptions;
  }

  @Override
  public Path getConfigDirectory() {
    return directoryPath;
  }

  @Override
  public Boolean isMetricsEnabled() {
    return metricsEnabled;
  }

  @Override
  public Integer getMetricsPort() {
    return metricsPort;
  }

  @Override
  public String getMetricsNetworkInterface() {
    return metricsHost;
  }

  @Override
  public Set<MetricCategory> getMetricCategories() {
    return emptySet();
  }

  @Override
  public Path getDataPath() {
    return dataPath;
  }

  @Override
  public void run() {
    final Runner runner = new Runner(this);
    runner.run();
  }
}
