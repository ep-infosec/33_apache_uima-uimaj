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

import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO: add javadoc here.
 */
public class CustomInformationControl
        implements IInformationControl, IInformationControlExtension2 {

  /** The m shell. */
  private Shell mShell;

  /** The m control. */
  private Control mControl;

  /** The m content handler. */
  private ICustomInformationControlContentHandler mContentHandler;

  /**
   * Initializes a new instance.
   *
   * @param parent
   *          the parent
   * @param contentHandler
   *          the content handler
   */
  public CustomInformationControl(Shell parent,
          ICustomInformationControlContentHandler contentHandler) {
    mContentHandler = contentHandler;

    mShell = new Shell(parent, SWT.NO_FOCUS | SWT.ON_TOP);
    mShell.setLayout(new FillLayout());

    Display display = mShell.getDisplay();
    mShell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
  }

  /**
   * Sets the viewer control.
   *
   * @param viewerControl
   *          the new control
   */
  public void setControl(Control viewerControl) {
    mControl = viewerControl;
    mShell.setSize(viewerControl.getSize());
  }

  /**
   * Retrieves the parent.
   *
   * @return the parent
   */
  public Composite getParent() {
    return mShell;
  }

  @Override
  public void setInformation(String information) {
    // this method is replaced by the extension interface
    // method setInput(...)
  }

  @Override
  public void setSizeConstraints(int maxWidth, int maxHeight) {
  }

  @Override
  public Point computeSizeHint() {
    return mShell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
  }

  @Override
  public void setVisible(boolean visible) {
    mControl.setVisible(visible);
    mShell.setVisible(visible);
  }

  @Override
  public void setSize(int width, int height) {
    // mShell.setSize(width, height);
  }

  @Override
  public void setLocation(Point location) {
    Rectangle trim = mShell.computeTrim(0, 0, 0, 0);

    Point textLocation = mControl.getLocation();
    location.x += trim.x - textLocation.x;
    location.y += trim.y - textLocation.y;

    mShell.setLocation(location);
  }

  @Override
  public void dispose() {
    if (mShell != null && !mShell.isDisposed()) {
      mShell.dispose();
    }
  }

  @Override
  public void addDisposeListener(DisposeListener listener) {
    mShell.addDisposeListener(listener);
  }

  @Override
  public void removeDisposeListener(DisposeListener listener) {
    mShell.removeDisposeListener(listener);
  }

  @Override
  public void setForegroundColor(Color foreground) {
    mShell.setForeground(foreground);
  }

  @Override
  public void setBackgroundColor(Color background) {
    mShell.setBackground(background);
  }

  @Override
  public boolean isFocusControl() {
    return mShell.isFocusControl();
  }

  @Override
  public void setFocus() {
    mShell.setFocus();
  }

  @Override
  public void addFocusListener(FocusListener listener) {
    mShell.addFocusListener(listener);
  }

  @Override
  public void removeFocusListener(FocusListener listener) {
    mShell.removeFocusListener(listener);
  }

  @Override
  public void setInput(Object input) {
    mContentHandler.setInput(this, input);
  }

  /**
   * Retrieves the control.
   *
   * @return the control
   */
  public Control getControl() {
    return mControl;
  }
}
