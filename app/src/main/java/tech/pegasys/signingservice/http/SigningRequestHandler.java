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
package tech.pegasys.signingservice.http;

import tech.pegasys.ethsigner.core.signing.Signature;
import tech.pegasys.ethsigner.core.signing.TransactionSigner;
import tech.pegasys.ethsigner.core.signing.TransactionSignerProvider;
import tech.pegasys.signingservice.utils.JsonDecoder;

import java.util.Optional;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;

public class SigningRequestHandler implements Handler<RoutingContext> {

  public static final String SIGNER_PATH_REGEX = "/signer/(?<publicKey>.*)";
  private static final Logger LOG = LogManager.getLogger();
  private final TransactionSignerProvider signerProvider;
  private final JsonDecoder jsonDecoder;

  public SigningRequestHandler(
      final TransactionSignerProvider signerProvider, final JsonDecoder jsonDecoder) {
    this.signerProvider = signerProvider;
    this.jsonDecoder = jsonDecoder;
  }

  @Override
  public void handle(final RoutingContext context) {
    LOG.debug("Received a request for {}", context.normalisedPath());
    final String publicKey = getPublicKey(context);
    final Optional<TransactionSigner> signer = signerProvider.getSigner(publicKey);
    if (signer.isEmpty()) {
      LOG.error("Unable to find an appropriate signer for request: {}", publicKey);
      context.fail(404);
    } else {
      try {
        final Bytes dataToSign = getDataToSign(context);
        final Signature signature = signer.get().sign(dataToSign.toArrayUnsafe());
        context.response().end(signature.toString());
      } catch (final DecodeException e) {
        LOG.error("Invalid signing request format: {}", e.getMessage());
        context.fail(400);
      }
    }
  }

  private String getPublicKey(final RoutingContext context) {
    return context.pathParam("publicKey");
  }

  private Bytes getDataToSign(final RoutingContext context) {
    final Buffer body = context.getBody();
    final SigningRequestBody signingRequest =
        jsonDecoder.decodeValue(body, SigningRequestBody.class);
    return signingRequest.signingRoot();
  }
}
