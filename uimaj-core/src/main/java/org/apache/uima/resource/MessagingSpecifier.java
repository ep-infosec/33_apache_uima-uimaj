/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.resource;

public interface MessagingSpecifier extends ResourceServiceSpecifier {

  /**
   * Returns the hostAddress.
   * 
   * @return String
   */
  String getHostAddress();

  /**
   * Returns the hostMessagingServer.
   * 
   * @return String
   */
  String getHostMessagingServer();

  /**
   * Returns the hostPassword.
   * 
   * @return String
   */
  String getHostPassword();

  /**
   * Returns the hostUser.
   * 
   * @return String
   */
  String getHostUser();

  /**
   * Returns the messagingType.
   * 
   * @return String
   */
  String getMessagingType();

  /**
   * Returns the targetAddress.
   * 
   * @return String
   */
  String getTargetAddress();

  /**
   * Returns the targetMessagingServer.
   * 
   * @return String
   */
  String getTargetMessagingServer();

  /**
   * Returns the targetPassword.
   * 
   * @return String
   */
  String getTargetPassword();

  /**
   * Returns the targetUser.
   * 
   * @return String
   */
  String getTargetUser();

  /**
   * Sets the hostAddress.
   * 
   * @param hostAddress
   *          The hostAddress to set
   */
  void setHostAddress(String hostAddress);

  /**
   * Sets the hostMessagingServer.
   * 
   * @param hostMessagingServer
   *          The hostMessagingServer to set
   */
  void setHostMessagingServer(String hostMessagingServer);

  /**
   * Sets the hostPassword.
   * 
   * @param hostPassword
   *          The hostPassword to set
   */
  void setHostPassword(String hostPassword);

  /**
   * Sets the hostUser.
   * 
   * @param hostUser
   *          The hostUser to set
   */
  void setHostUser(String hostUser);

  /**
   * Sets the messagingType.
   * 
   * @param messagingType
   *          The messagingType to set
   */
  void setMessagingType(String messagingType);

  /**
   * Sets the targetAddress.
   * 
   * @param targetAddress
   *          The targetAddress to set
   */
  void setTargetAddress(String targetAddress);

  /**
   * Sets the targetMessagingServer.
   * 
   * @param targetMessagingServer
   *          The targetMessagingServer to set
   */
  void setTargetMessagingServer(String targetMessagingServer);

  /**
   * Sets the targetPassword.
   * 
   * @param targetPassword
   *          The targetPassword to set
   */
  void setTargetPassword(String targetPassword);

  /**
   * Sets the targetUser.
   * 
   * @param targetUser
   *          The targetUser to set
   */
  void setTargetUser(String targetUser);

  /**
   * Gets the timeout period in milliseconds. If a call takes longer than this amount of time, an
   * exception will be thrown.
   * 
   * @return the timeout period in milliseconds. A null value indicates that the transport layer's
   *         default value will be used.
   */
  Integer getTimeout();

  /**
   * Sets the timeout period in milliseconds. If a call takes longer than this amount of time, an
   * exception will be thrown.
   * 
   * @param aTimeout
   *          the timeout period in milliseconds. A null value indicates that the transport layer's
   *          default value will be used.
   */
  void setTimeout(Integer aTimeout);
}
