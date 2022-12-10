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

import org.apache.uima.resource.metadata.NameValuePair;

/**
 * A type of <code>ResourceSpecifier</code> that locate an installed pear <code>Resource</code>.
 * 
 */
public interface PearSpecifier extends ResourceServiceSpecifier {

  /**
   * Retrieves the PEAR path at which the pear Resource is located.
   * 
   * @return a string
   */
  String getPearPath();

  /**
   * Sets the PEAR path at which a Resource is located.
   * 
   * @param aPearPath
   *          a pear path string
   */
  void setPearPath(String aPearPath);

  /**
   * Gets legacy string-valued parameters that may be read by the pear resource class when it is
   * initialized. These parameters are represented as follows in the PEAR specifier XML:
   * 
   * <pre>
   * {@code
   * <parameters>
   *   <parameter name="param1" value="val1"/>
   * </parameters>  
   * }
   * </pre>
   * 
   * @return an array of parameters. This will never return <code>null</code>.
   * 
   * @deprecated These parameters only support string values. Better use {@link #getPearParameters}.
   */
  @Deprecated
  Parameter[] getParameters();

  /**
   * Sets legacy string-valued parameters that may be read by the pear resource class when it is
   * initialized.
   * 
   * @param parameters
   *          the Parameters to set.
   * 
   * @see #getParameters()
   * @deprecated These parameters only support string values. Better use {@link #setPearParameters}.
   */
  @Deprecated
  void setParameters(Parameter... parameters);

  /**
   * Gets parameters that may be read by the pear resource class when it is initialized. These
   * parameters are represented as follows in the PEAR specifier XML:
   * 
   * <pre>
   * {@code
   * <pearParameters>
   *   <nameValuePair>
   *    <name>param1</name>
   *    <value><string>val1</string></value>
   *   </nameValuePair>
   * </pearParameters>
   * }
   * </pre>
   * 
   * @return an array of pearParameters. This will never return <code>null</code>.
   */
  NameValuePair[] getPearParameters();

  /**
   * Sets pearParameters that may be read by the pear resource class when it is initialized.
   * 
   * @param pearParameters
   *          the pearParameters to set.
   * 
   * @see #getPearParameters()
   */
  void setPearParameters(NameValuePair... pearParameters);
}