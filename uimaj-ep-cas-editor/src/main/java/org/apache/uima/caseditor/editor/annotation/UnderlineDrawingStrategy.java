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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Draws an line under an annotation.
 */
public class UnderlineDrawingStrategy implements IDrawingStrategy {

  /**
   * Draws a line under under a given annotation.
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
    if (gc != null) {
      Rectangle bounds;

      if (length > 0) {
        bounds = textWidget.getTextBounds(offset, offset + length - 1);
      } else {
        Point location = textWidget.getLocationAtOffset(offset);
        bounds = new Rectangle(location.x, location.y, 1, textWidget.getLineHeight());
      }

      int y = bounds.y + bounds.height - 1;

      gc.setForeground(color);

      gc.drawLine(bounds.x, y, bounds.x + bounds.width, y);
    } else {
      textWidget.redrawRange(offset, length, true);
    }
  }
}
