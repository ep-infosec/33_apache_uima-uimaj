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

package org.apache.uima.caseditor.editor.fsview;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * A combo box which contains UIMA Types.
 * 
 * @see Type
 * @see TypeSystem
 */
public class TypeCombo extends Composite {

  /** The listeners. */
  private Set<ITypePaneListener> listeners = new HashSet<>();

  /** The type system. */
  private TypeSystem typeSystem;

  /** The type combo. */
  private Combo typeCombo;

  /** The type name list. */
  private List<String> typeNameList;

  /**
   * Instantiates a new type combo.
   *
   * @param parent
   *          the parent
   */
  public TypeCombo(Composite parent) {
    super(parent, SWT.NONE);

    setLayout(new FillLayout());

    typeCombo = new Combo(this, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER);

    typeCombo.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        Type newType = getType();

        for (ITypePaneListener listener : listeners) {
          listener.typeChanged(newType);
        }
      }
    });
  }

  /**
   * Sets the input.
   *
   * @param superType
   *          the super type
   * @param typeSystem
   *          the type system
   * @param filterTypes
   *          the filter types
   */
  public void setInput(Type superType, TypeSystem typeSystem, Collection<Type> filterTypes) {
    this.typeSystem = typeSystem;

    typeNameList = new LinkedList<>();

    typeNameList.add(superType.getName());

    for (Type type : typeSystem.getProperlySubsumedTypes(superType)) {

      if (!filterTypes.contains(type)) {
        typeNameList.add(type.getName());
      }
    }

    typeCombo.setItems(typeNameList.toArray(new String[typeNameList.size()]));

    // select the super type, its the first element (and must be there)
    typeCombo.select(0);
  }

  /**
   * Sets the input.
   *
   * @param superType
   *          the super type
   * @param typeSystem
   *          the type system
   */
  public void setInput(Type superType, TypeSystem typeSystem) {
    setInput(superType, typeSystem, Collections.emptyList());
  }

  /**
   * Selects the given type or does nothing if the type is not listed.
   *
   * @param type
   *          the type
   */
  public void select(Type type) {
    Integer typeIndex = typeNameList.indexOf(type.getName());

    if (typeIndex != null) {
      typeCombo.select(typeIndex);
    }
  }

  /**
   * Retrieves the selected type. Behavior is undefined when called before setInput.
   * 
   * @return the selected type
   */
  public Type getType() {
    return typeSystem.getType(typeCombo.getText());
  }

  /**
   * Adds the listener.
   *
   * @param listener
   *          the listener
   */
  public void addListener(ITypePaneListener listener) {
    listeners.add(listener);
  }

  /**
   * Removes the listener.
   *
   * @param listener
   *          the listener
   */
  public void removeListener(ITypePaneListener listener) {
    listeners.remove(listener);
  }
}
