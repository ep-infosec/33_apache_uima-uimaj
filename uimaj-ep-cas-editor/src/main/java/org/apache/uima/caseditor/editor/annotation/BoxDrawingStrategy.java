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

package org.apache.uima.caseditor.editor.annotation;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Draws a box around an annotation.
 */
final class BoxDrawingStrategy implements IDrawingStrategy {

  /**
   * Draws a box around the given annotation.
   *
   * @param annotation
   *          the annotation
   * @param gc
   *          the gc
   * @param textWidget
   *          the text widget
   * @param offset
   *          the offset
   * @param length
   *          the length
   * @param color
   *          the color
   */
  @Override
  public void draw(Annotation annotation, GC gc, StyledText textWidget, int offset, int length,
          Color color) {
    if (length != 0) {
      if (gc != null) {
        Rectangle bounds = textWidget.getTextBounds(offset, offset + length - 1);

        gc.setForeground(color);

        int correctedHeight;

        if (bounds.height > 0) {
          correctedHeight = bounds.height - 1;
        } else {
          correctedHeight = bounds.height;
        }

        int correctedWidth;

        if (bounds.width > 0)
          correctedWidth = bounds.width - 1;
        else
          correctedWidth = bounds.width;

        gc.drawRectangle(bounds.x, bounds.y, correctedWidth, correctedHeight);
      } else {
        textWidget.redrawRange(offset, length, true);
      }
    }
  }
}
