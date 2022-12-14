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

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.taeconfigurator.editors.ui.AbstractSection;
import org.apache.uima.taeconfigurator.editors.ui.CapabilitySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The Class AddCapabilityTypeDialog.
 */
public class AddCapabilityTypeDialog extends AbstractDialogMultiColTable {

  /** The types. */
  public String[] types; // this is the selection

  /** The inputs. */
  public boolean[] inputs;

  /** The outputs. */
  public boolean[] outputs;

  /** The Constant NAME. */
  private static final int NAME = 0;

  /** The Constant INPUT. */
  private static final int INPUT = 1;

  /** The Constant OUTPUT. */
  private static final int OUTPUT = 2;

  /** The Constant NAMESPACE. */
  private static final int NAMESPACE = 3;

  /** The capability section. */
  CapabilitySection capabilitySection;

  /** The capability. */
  private Capability capability;

  /** The existing. */
  private TreeItem existing = null;

  /** The excluded types. */
  private static List<String> excludedTypes = new ArrayList<>();
  {
    excludedTypes.add(CAS.TYPE_NAME_ARRAY_BASE);
    excludedTypes.add(CAS.TYPE_NAME_EMPTY_FS_LIST);
    excludedTypes.add(CAS.TYPE_NAME_EMPTY_FLOAT_LIST);
    excludedTypes.add(CAS.TYPE_NAME_EMPTY_INTEGER_LIST);
    excludedTypes.add(CAS.TYPE_NAME_EMPTY_STRING_LIST);
    excludedTypes.add(CAS.TYPE_NAME_FS_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_FS_LIST);
    excludedTypes.add(CAS.TYPE_NAME_FLOAT);
    excludedTypes.add(CAS.TYPE_NAME_FLOAT_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_FLOAT_LIST);
    excludedTypes.add(CAS.TYPE_NAME_INTEGER);
    excludedTypes.add(CAS.TYPE_NAME_INTEGER_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_INTEGER_LIST);
    excludedTypes.add(CAS.TYPE_NAME_BOOLEAN);
    excludedTypes.add(CAS.TYPE_NAME_BOOLEAN_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_BYTE);
    excludedTypes.add(CAS.TYPE_NAME_BYTE_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_SHORT);
    excludedTypes.add(CAS.TYPE_NAME_SHORT_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_LONG);
    excludedTypes.add(CAS.TYPE_NAME_LONG_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_DOUBLE);
    excludedTypes.add(CAS.TYPE_NAME_DOUBLE_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_LIST_BASE);
    excludedTypes.add(CAS.TYPE_NAME_NON_EMPTY_FS_LIST);
    excludedTypes.add(CAS.TYPE_NAME_NON_EMPTY_FLOAT_LIST);
    excludedTypes.add(CAS.TYPE_NAME_NON_EMPTY_INTEGER_LIST);
    excludedTypes.add(CAS.TYPE_NAME_NON_EMPTY_STRING_LIST);
    excludedTypes.add(CAS.TYPE_NAME_SOFA);
    excludedTypes.add(CAS.TYPE_NAME_STRING);
    excludedTypes.add(CAS.TYPE_NAME_STRING_ARRAY);
    excludedTypes.add(CAS.TYPE_NAME_STRING_LIST);
    excludedTypes.add(CAS.TYPE_NAME_TOP);
  }

  /**
   * Instantiates a new adds the capability type dialog.
   *
   * @param aSection
   *          the a section
   * @param c
   *          the c
   */
  public AddCapabilityTypeDialog(AbstractSection aSection, Capability c) {
    super(aSection, "Add Types to a Capability Set",
            "Mark one or more types as "
                    + ((aSection.isCasConsumerDescriptor()) ? "Input"
                            : (aSection.isCasInitializerDescriptor()
                                    || aSection.isCollectionReaderDescriptor()) ? "Output"
                                            : "Input and/or Output")
                    + " by clicking the mouse in the corresponding column, and press OK");
    capabilitySection = (CapabilitySection) aSection;
    capability = c;
    enableCol1 = !aSection.isCasInitializerDescriptor() && !aSection.isCollectionReaderDescriptor();
    enableCol2 = !aSection.isCasConsumerDescriptor();
  }

  /**
   * Instantiates a new adds the capability type dialog.
   *
   * @param aSection
   *          the a section
   * @param c
   *          the c
   * @param aExisting
   *          the a existing
   */
  public AddCapabilityTypeDialog(AbstractSection aSection, Capability c, TreeItem aExisting) {
    this(aSection, c);
    existing = aExisting;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#createDialogArea(org.eclipse.
   * swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent, existing);

    f_tree = newTree(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
    // ((GridData)table.getLayoutData()).heightHint = 100;
    f_tree.setHeaderVisible(true);
    f_tree.setLinesVisible(true);
    new TreeColumn(f_tree, SWT.NONE).setText("Type Name");
    new TreeColumn(f_tree, SWT.NONE).setText("Input");
    new TreeColumn(f_tree, SWT.NONE).setText("Output");
    new TreeColumn(f_tree, SWT.NONE).setText("Type Namespace");

    if (null == existing) {
      String[] allTypes = getAllTypesAsSortedArray();

      for (int i = 0; i < allTypes.length; i++) {
        if (!excludedTypes.contains(allTypes[i]) && !hasType(capability.getInputs(), allTypes[i])
                && !hasType(capability.getOutputs(), allTypes[i])) {
          TreeItem item = new TreeItem(f_tree, SWT.NONE);
          setGuiTypeName(item, allTypes[i]);
        }
      }
    } else { // existing item being edited - just show one item
      TreeItem item = new TreeItem(f_tree, SWT.NONE);
      item.setText(NAME, existing.getText(CapabilitySection.NAME_COL));
      item.setText(NAMESPACE, existing.getText(CapabilitySection.NAMESPACE_COL));
      TypeOrFeature tof = AbstractSection.getTypeOrFeature(capability.getInputs(),
              capabilitySection.getFullyQualifiedName(existing));
      setChecked(item, 1, null != tof);
      tof = AbstractSection.getTypeOrFeature(capability.getOutputs(),
              capabilitySection.getFullyQualifiedName(existing));
      setChecked(item, 2, null != tof);
    }

    section.packTree(f_tree);
    // can't use selection event because it doesn't return mouse position
    f_tree.removeListener(SWT.Selection, this);
    f_tree.addListener(SWT.MouseDown, this); // for i / o toggling
    newErrorMessage(composite);
    return composite;
  }

  /**
   * Checks for type.
   *
   * @param items
   *          the items
   * @param name
   *          the name
   * @return true, if successful
   */
  private boolean hasType(TypeOrFeature[] items, String name) {
    if (null == items)
      return false;
    for (int i = 0; i < items.length; i++) {
      if (items[i].isType() && items[i].getName().equals(name))
        return true;
    }
    return false;
  }

  /**
   * Return values having at least one of input or output selected.
   */
  @Override
  public void copyValuesFromGUI() {
    List<String> names = new ArrayList<>();
    List<Boolean> ins = new ArrayList<>();
    List<Boolean> outs = new ArrayList<>();

    for (int i = f_tree.getItemCount() - 1; i >= 0; i--) {
      TreeItem item = f_tree.getItem(i);
      if (item.getText(INPUT).equals(checkedIndicator(INPUT))
              || item.getText(OUTPUT).equals(checkedIndicator(OUTPUT))) {
        names.add(capabilitySection.getFullyQualifiedName(item.getText(NAMESPACE),
                item.getText(NAME)));
        ins.add(item.getText(INPUT).equals(checkedIndicator(INPUT)));
        outs.add(item.getText(OUTPUT).equals(checkedIndicator(OUTPUT)));
      }
    }

    types = (String[]) names.toArray(stringArray0);
    inputs = new boolean[types.length];
    outputs = new boolean[types.length];

    for (int i = 0; i < types.length; i++) {
      inputs[i] = (Boolean) ins.get(i);
      outputs[i] = (Boolean) outs.get(i);
    }
  }

  /**
   * Sets the gui type name.
   *
   * @param item
   *          the item
   * @param typeName
   *          the type name
   */
  // used by dialog
  public void setGuiTypeName(TreeItem item, String typeName) {
    item.setText(NAME, AbstractSection.getShortName(typeName));
    item.setText(NAMESPACE, AbstractSection.getNameSpace(typeName));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialogMultiColTable#isValid()
   */
  @Override
  public boolean isValid() {
    for (int i = 0; i < types.length; i++) {
      if (!inputs[i]) {
        if (someFeatureOnType(types[i], INPUT)) {
          setErrorMessage(
                  "This type has one or more features marked for input and can''t be removed as an Input.\nIf you want to do this, first remove the Input designation for all features on this type.");
          return false;
        }
        // Use case: You can have
        // a Type with output features, where the type itself is not output.
        // In this case, the type must be in an input. The meaning is that
        // the AE sets or populates features on existing types.
        if (!outputs[i] && someFeatureOnType(types[i], OUTPUT) && !inputs[i]) {
          setErrorMessage(
                  "This type has features which are output, and so must either be specified as INPUT (meaning features are populated or \"outputted\" on existing instances), or OUTPUT (meaning new instances of this type are created).  It can't be removed completely.\nIf you want to do this, first remove the features which are output for this type.");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * return true if the type has a feature (except all-features) marked as INPUT (OUTPUT).
   *
   * @param typeName
   *          the type name
   * @param IO
   *          the io
   * @return true if the type has a feature (except all-features) marked as INPUT (OUTPUT)
   */
  private boolean someFeatureOnType(String typeName, int IO) {
    // special case for all-features
    TypeOrFeature[] tofs = (IO == INPUT) ? capability.getInputs() : capability.getOutputs();
    // you can remove the "INPUT" or "OUTPUT" designation from a type
    // even if it has the all-features flag on.
    String typeNamePlusColon = typeName + ':';
    for (int i = 0; i < tofs.length; i++) {
      if (!tofs[i].isType() && tofs[i].getName().startsWith(typeNamePlusColon))
        return true;
    }
    return false;
  }

}
