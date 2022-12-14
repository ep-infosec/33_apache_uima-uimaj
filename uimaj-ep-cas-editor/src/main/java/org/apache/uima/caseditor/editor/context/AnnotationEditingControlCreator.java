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

package org.apache.uima.caseditor.editor.context;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.caseditor.editor.CustomInformationControl;
import org.apache.uima.caseditor.editor.ICustomInformationControlContentHandler;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO: add javadoc here.
 */
public class AnnotationEditingControlCreator implements IInformationControlCreator {

  /**
   * Creates the information control.
   *
   * @param parent
   *          the parent
   * @return the new control
   */
  @Override
  public IInformationControl createInformationControl(Shell parent) {
    final ICustomInformationControlContentHandler contentHandler = new ICustomInformationControlContentHandler() {

      @Override
      public void setInput(CustomInformationControl control, Object input) {
        AnnotationEditingControl annotationEditControl = (AnnotationEditingControl) control
                .getControl();

        annotationEditControl.displayFeatureStructure((FeatureStructure) input);

        control.getParent().setSize(annotationEditControl.getSize());
      }

    };

    CustomInformationControl control = new CustomInformationControl(parent, contentHandler);

    AnnotationEditingControl annotationEditingControl = new AnnotationEditingControl(
            control.getParent());

    control.setControl(annotationEditingControl);

    return control;
  }
}
