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

package org.apache.uima.taeconfigurator.editors.ui.dialogs;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.uima.taeconfigurator.editors.ui.AbstractSection;

/**
 * The Class TypesWithNameSpaces.
 */
public class TypesWithNameSpaces {

  /** The sorted names. */
  public SortedMap sortedNames = new TreeMap();

  /**
   * Adds the.
   *
   * @param fullname
   *          the fullname
   */
  public void add(String fullname) {
    String key = AbstractSection.getShortName(fullname);
    String nameSpace = AbstractSection.getNameSpace(fullname);
    Set entry = (Set) sortedNames.get(key);
    if (null == entry) {
      entry = new TreeSet();
      entry.add(nameSpace);
      sortedNames.put(key, entry);
    } else {
      entry.add(nameSpace);
    }
  }
}