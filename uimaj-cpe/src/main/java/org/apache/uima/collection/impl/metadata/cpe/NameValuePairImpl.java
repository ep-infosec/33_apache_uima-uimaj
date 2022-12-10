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

package org.apache.uima.collection.impl.metadata.cpe;

import org.apache.uima.collection.metadata.NameValuePair;

/**
 * The Class NameValuePairImpl.
 */
public class NameValuePairImpl implements NameValuePair {

  /** The name. */
  private String name;

  /** The value. */
  private Object value;

  /**
   * Instantiates a new name value pair impl.
   */
  public NameValuePairImpl() {
  }

  /**
   * Instantiates a new name value pair impl.
   *
   * @param aName
   *          the a name
   * @param aValue
   *          the a value
   */
  public NameValuePairImpl(String aName, Object aValue) {
    name = aName;
    value = aValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.collection.metadata.NameValuePair#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.collection.metadata.NameValuePair#setName(java.lang.String)
   */
  @Override
  public void setName(String aName) {
    name = aName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.collection.metadata.NameValuePair#getValue()
   */
  @Override
  public Object getValue() {
    return value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.collection.metadata.NameValuePair#setValue(java.lang.Object)
   */
  @Override
  public void setValue(Object aValue) {
    value = aValue;
  }

}