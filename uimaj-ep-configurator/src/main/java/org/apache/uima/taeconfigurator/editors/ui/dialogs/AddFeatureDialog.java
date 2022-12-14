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

import org.apache.uima.cas.Type;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.taeconfigurator.editors.ui.AbstractSection;
import org.apache.uima.taeconfigurator.editors.ui.TypeSection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * The Class AddFeatureDialog.
 */
public class AddFeatureDialog extends AbstractDialogKeyVerify {

  /** The Constant ONLY_NON_PRIMITIVE_TYPES. */
  private static final int ONLY_NON_PRIMITIVE_TYPES = 0;

  /** The Constant ALL_TYPES. */
  private static final int ALL_TYPES = 1;

  /** The feature name UI. */
  private StyledText featureNameUI;

  /** The feature range name UI. */
  private Text featureRangeNameUI;

  /** The description UI. */
  private Text descriptionUI;

  /** The feature name. */
  public String featureName;

  /** The original feature name. */
  private String originalFeatureName;

  /** The feature range name. */
  public String featureRangeName;

  /** The description. */
  public String description;

  /** The element range name. */
  public String elementRangeName;

  /** The multi ref. */
  public Boolean multiRef;

  /** The type section. */
  private TypeSection typeSection;

  /** The td. */
  private TypeDescription td;

  /** The existing fd. */
  private FeatureDescription existingFd;

  /** The all types list. */
  private TypesWithNameSpaces allTypesList;

  /** The multi ref composite. */
  private Composite multiRefComposite;

  /** The multi ref UI. */
  private CCombo multiRefUI;

  /** The element type composite. */
  private Composite elementTypeComposite;

  /** The element range name UI. */
  private Text elementRangeNameUI;

  /** The type filter. */
  private int typeFilter;

  /**
   * Instantiates a new adds the feature dialog.
   *
   * @param aSection
   *          the a section
   * @param aTd
   *          the a td
   * @param aExistingFd
   *          the a existing fd
   */
  public AddFeatureDialog(AbstractSection aSection, TypeDescription aTd,
          FeatureDescription aExistingFd) {
    super(aSection, "Add a Feature", "Use this panel to add or edit a feature");
    typeSection = (TypeSection) aSection;
    td = aTd;
    existingFd = aExistingFd;
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
    Composite mainArea = (Composite) super.createDialogArea(parent, existingFd);
    createWideLabel(mainArea, "The feature name must be unique within this type");

    // This part of the form looks like this sketch
    //
    // Feature Name: Text field << in 2 grid composite
    // Range Type: CCombo << in 2 grid composite
    // checkbox: multiple references allowed (hidden if not applicable)
    // Element Type: same as above (hidden if not applicable)
    // description: Text field << in 2 grid composite

    Composite twoCol = new2ColumnComposite(mainArea);

    featureNameUI = newLabeledSingleLineStyledText(twoCol, "Feature Name", S_);

    typeFilter = ALL_TYPES;
    featureRangeNameUI = newLabeledTypeInput(section, twoCol, "Range Type:",
            "The range type specifies the type of value this feature can hold.");

    multiRefComposite = new2ColumnComposite(twoCol);
    ((GridData) multiRefComposite.getLayoutData()).horizontalSpan = 2;
    multiRefUI = newLabeledCCombo(multiRefComposite, "References:",
            "Specify if this reference is the only reference to the collection object");
    multiRefUI.add("Not Specified - defaults to multiple references not allowed");
    multiRefUI.add("Multiple references not allowed");
    multiRefUI.add("Multiple references allowed");
    multiRefUI.select(0);

    elementTypeComposite = new2ColumnComposite(twoCol);
    ((GridData) elementTypeComposite.getLayoutData()).horizontalSpan = 2;
    typeFilter = ONLY_NON_PRIMITIVE_TYPES;
    elementRangeNameUI = newLabeledTypeInput(section, elementTypeComposite, "Element Type:",
            "The element type of each element in the Array or List object");

    descriptionUI = newDescription(twoCol, S_);
    newErrorMessage(twoCol, 2);

    if (null != existingFd) {
      descriptionUI.setText(convertNull(existingFd.getDescription()));
      featureNameUI.setText(originalFeatureName = existingFd.getName());
      featureRangeNameUI.setText(existingFd.getRangeTypeName());
      Boolean mra = existingFd.getMultipleReferencesAllowed();
      multiRefUI.select((null == mra) ? 0 : (mra) ? 2 : 1);
      String ert = existingFd.getElementType();
      elementRangeNameUI.setText((null == ert) ? "" : ert);

    }
    manageVisibleFields();
    return mainArea;
  }

  /**
   * Manage visible fields.
   */
  private void manageVisibleFields() {
    String range = featureRangeNameUI.getText();
    if (AbstractSection.isArrayOrListType(range)) {
      multiRefComposite.setVisible(true);
      if (AbstractSection.isFSArrayOrListType(range)) {
        elementTypeComposite.setVisible(true);
      } else {
        elementTypeComposite.setVisible(false);
      }
    } else {
      multiRefComposite.setVisible(false);
      elementTypeComposite.setVisible(false);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#handleEvent(org.eclipse.swt.
   * widgets.Event)
   */
  @Override
  public void handleEvent(Event event) {
    super.handleEvent(event);
    if (event.type == SWT.Modify && event.widget == featureRangeNameUI) {
      manageVisibleFields();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#getTypeSystemInfoList()
   */
  @Override
  public TypesWithNameSpaces getTypeSystemInfoList() {
    TypesWithNameSpaces result = super.getTypeSystemInfoList();
    Type[] allTypes = (Type[]) editor.allTypes.get().values().toArray(new Type[0]);
    /*
     * Arrays.sort(allTypes, new Comparator() {
     * 
     * public int compare(Object o1, Object o2) { Type t1 = (Type) o1; Type t2 = (Type) o2; return
     * t1.getShortName().compareTo(t2.getShortName()); } });
     */
    for (int i = 0; i < allTypes.length; i++) {
      Type type = allTypes[i];
      if (typeFilter == ONLY_NON_PRIMITIVE_TYPES) {
        if (!type.isPrimitive()) {
          result.add(type.getName());
        }
      } else {
        result.add(type.getName());
      }
    }
    if (typeFilter == ALL_TYPES) {
      allTypesList = result;
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#copyValuesFromGUI()
   */
  @Override
  public void copyValuesFromGUI() {
    featureName = featureNameUI.getText();
    description = nullIf0lengthString(descriptionUI.getText());
    featureRangeName = featureRangeNameUI.getText();
    multiRef = (1 == multiRefUI.getSelectionIndex()) ? Boolean.FALSE
            : (2 == multiRefUI.getSelectionIndex()) ? Boolean.TRUE : null;
    if (AbstractSection.isFSArrayOrListType(featureRangeName)) {
      elementRangeName = elementRangeNameUI.getText();
      if ("".equals(elementRangeName)) {
        elementRangeName = null;
      }
    } else {
      elementRangeName = null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialogKeyVerify#verifyKeyChecks(org.
   * eclipse.swt.events.VerifyEvent)
   */
  @Override
  public boolean verifyKeyChecks(VerifyEvent event) {
    if (event.keyCode == SWT.CR || event.keyCode == SWT.TAB)
      return true;
    if (Character.isJavaIdentifierPart(event.character))
      return true;
    if (event.widget == featureRangeNameUI && event.character == '.')
      return true;
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#isValid()
   */
  @Override
  public boolean isValid() {
    if (featureName.length() == 0 || featureRangeName.length() == 0) {
      return false;
    }
    if (!featureName.equals(originalFeatureName)) {
      String errMsg = typeSection.checkFeature(this, td, existingFd);
      if (null != errMsg) {
        setErrorMessage(errMsg);
        return false;
      }
    }
    if (!typeContainedInTypeSystemInfoList(featureRangeName, allTypesList)) {
      setErrorMessage("RangeType '" + featureRangeName
              + "' is unknown. If this is intended, please define it first.");
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#textModifyCallback(org.
   * eclipse.swt.widgets.Event)
   */
  @Override
  public void textModifyCallback(Event e) {
    manageVisibleFields();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.taeconfigurator.editors.ui.dialogs.AbstractDialog#enableOK()
   */
  @Override
  public void enableOK() {
    copyValuesFromGUI();
    okButton.setEnabled(featureName.length() > 0 && featureRangeName.length() > 0);
  }

}
