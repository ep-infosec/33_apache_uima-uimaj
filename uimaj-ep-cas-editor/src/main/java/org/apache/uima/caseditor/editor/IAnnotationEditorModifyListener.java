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

import java.util.Collection;

import org.apache.uima.cas.Type;

/**
 * The listener interface for receiving IAnnotationEditorModify events. The class that is interested
 * in processing a IAnnotationEditorModify event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addIAnnotationEditorModifyListener</code> method. When the IAnnotationEditorModify event
 * occurs, that object's appropriate method is invoked.
 */
public interface IAnnotationEditorModifyListener {

  /**
   * Called when the editor annotation mode changed.
   *
   * @param newMode
   *          the new mode
   */
  void annotationModeChanged(Type newMode);

  /**
   * Called when the shown annotation types in the editor are changed.
   *
   * @param shownAnnotationTypes
   *          the shown annotation types
   */
  void showAnnotationsChanged(Collection<Type> shownAnnotationTypes);
}
