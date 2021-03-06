package org.interledger.ildcp;

/*-
 * ========================LICENSE_START=================================
 * Interledger DCP Core
 * %%
 * Copyright (C) 2017 - 2019 Hyperledger and its contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import org.interledger.core.DateUtils;
import org.interledger.core.Immutable;
import org.interledger.core.InterledgerAddress;
import org.interledger.core.InterledgerAddressPrefix;
import org.interledger.core.InterledgerCondition;
import org.interledger.core.InterledgerPreparePacket;

import com.google.common.primitives.UnsignedLong;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Derived;

import java.time.Instant;
import java.util.Optional;

/**
 * An extension of {@link InterledgerPreparePacket} that can be used as an IL-DCP request over Interledger.
 */
public interface IldcpRequestPacket extends InterledgerPreparePacket {

  InterledgerAddress PEER_DOT_CONFIG = InterledgerAddress.of(InterledgerAddressPrefix.PEER.with("config").getValue());

  InterledgerCondition EXECUTION_CONDITION = IldcpResponsePacket.EXECUTION_FULFILLMENT.getCondition();

  byte[] EMPTY_DATA = new byte[0];

  /**
   * Get the default builder.
   *
   * @return a {@link IldcpRequestPacketBuilder} instance.
   */
  static IldcpRequestPacketBuilder builder() {
    return new IldcpRequestPacketBuilder();
  }

  /**
   * The destination of an ILP packet for IL-DCP is {@code 0} by default, but can be adjusted.
   */
  @Override
  default UnsignedLong getAmount() {
    return UnsignedLong.ZERO;
  }

  /**
   * The execution_condition of an ILP packet for IL-DCP is always {@code Zmh6rfhivXdsj8GLjp+OIAiXFIVu4jOzkCpZHQ1fKSU=}
   * in Base64 format, which is the SHA-256 hash of a 32-byte array with all 0 values.
   */
  @Override
  default InterledgerCondition getExecutionCondition() {
    return EXECUTION_CONDITION;
  }

  /**
   * The destination of an ILP packet for IL-DCP is always {@code peer.config}.
   */
  @Override
  default InterledgerAddress getDestination() {
    return PEER_DOT_CONFIG;
  }

  /**
   * The Date and time when the packet expires. Each connector changes the value of this field to set the expiry to an
   * earlier time, before forwarding the packet.
   *
   * @return The {@link Instant} this packet should be considered to be expired.
   */
  @Override
  default Instant getExpiresAt() {
    return DateUtils.now().plusSeconds(30);
  }

  /**
   * The data of an ILP packet for IL-DCP requests is always empty (size: 0).
   */
  @Override
  default byte[] getData() {
    return EMPTY_DATA;
  }

  @Override
  default Optional<Object> typedData() {
    return Optional.empty();
  }

  /**
   * Exists to satisfy Immutables.
   */
  @Immutable
  abstract class AbstractIldcpRequestPacket implements IldcpRequestPacket {

    @Override
    @Derived
    public InterledgerAddress getDestination() {
      return PEER_DOT_CONFIG;
    }

    @Override
    @Default
    public UnsignedLong getAmount() {
      return UnsignedLong.ZERO;
    }

    @Override
    @Derived
    public InterledgerCondition getExecutionCondition() {
      return EXECUTION_CONDITION;
    }

    @Override
    @Default
    public Instant getExpiresAt() {
      return DateUtils.now().plusSeconds(30);
    }

    @Override
    @Derived
    public byte[] getData() {
      return EMPTY_DATA;
    }

  }

}
