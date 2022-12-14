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

package org.apache.uima.caseditor.editor.editview.validator;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * This {@link ICellEditorValidator} validates {@link String} values which represents a
 * {@link Float}.
 *
 * For validation {@link Float#parseFloat(String)} is used.
 */
class FloatCellEditorValidator implements ICellEditorValidator {

  /**
   * Checks if the given value is a valid {@link Float}.
   *
   * @param value
   *          the value
   * @return null if valid otherwise an error message
   */
  @Override
  public String isValid(Object value) {
    assert value instanceof String;

    try {
      Float.parseFloat((String) value);
    } catch (NumberFormatException e) {
      return "Not a float!";
    }

    return null;
  }
}
