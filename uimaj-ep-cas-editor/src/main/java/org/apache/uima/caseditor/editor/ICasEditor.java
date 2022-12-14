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

package org.apache.uima.caseditor.editor;

import org.apache.uima.cas.CAS;
import org.eclipse.ui.IEditorPart;

/**
 * A Cas Editor is an extension to the {@link IEditorPart} interface and is responsible to view and
 * edit a {@link CAS} object.
 */
public interface ICasEditor extends IEditorPart {

  // TODO: Add a method to get the document provider, could needed
  // by various views to store configuration linked to the ts!

  /**
   * Gets the document.
   *
   * @return the document
   */
  ICasDocument getDocument();

  /**
   * Gets the cas document provider.
   *
   * @return the cas document provider
   */
  CasDocumentProvider getCasDocumentProvider();

  /**
   * Reopen editor with new type system.
   */
  void reopenEditorWithNewTypeSystem();

  /**
   * Adds the cas editor input listener.
   *
   * @param listener
   *          the listener
   */
  void addCasEditorInputListener(ICasEditorInputListener listener);

  /**
   * Removes the cas editor input listener.
   *
   * @param listener
   *          the listener
   */
  void removeCasEditorInputListener(ICasEditorInputListener listener);

}
