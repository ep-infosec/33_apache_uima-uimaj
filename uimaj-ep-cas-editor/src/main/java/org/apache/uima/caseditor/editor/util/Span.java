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

package org.apache.uima.caseditor.editor.util;

/**
 * This class usually specifies an continous are of text. This area has an start and end index. The
 * difference of the end and start is the length of the area.
 */
public class Span implements Comparable<Span> {
  /**
   * The start index of the text.
   */
  private int mStart;

  /**
   * The length of the text.
   */
  private int mLength;

  /**
   * Initializes a new <code>Span</code> instance.
   *
   * @param start
   *          the start
   * @param length
   *          the length
   */
  public Span(int start, int length) {
    mStart = start;
    mLength = length;
  }

  /**
   * Returns the start index.
   *
   * @return - start index
   */
  public int getStart() {
    return mStart;
  }

  /**
   * Returns the length.
   *
   * @return - length
   */
  public int getLength() {
    return mLength;
  }

  /**
   * Returns the end index.
   *
   * @return - end index
   */
  public int getEnd() {
    return mStart + mLength;
  }

  /**
   * Returns true if the given span is a subset of this span.
   *
   * @param containingSpan
   *          - the span to compare
   * @return - true if containingSpan is a subset.
   */
  public boolean isContaining(Span containingSpan) {
    boolean isStartContaining = getStart() <= containingSpan.getStart();
    if (!isStartContaining) {
      return false;
    }

    return getEnd() >= containingSpan.getEnd();
  }

  /**
   * Checks if is intersecting.
   *
   * @param s
   *          the s
   * @return true, if is intersecting
   */
  public boolean isIntersecting(Span s) {
    int sstart = s.getStart();
    return this.isContaining(s) || s.isContaining(this) || getStart() <= sstart && sstart < getEnd()
            || sstart <= getStart() && getStart() < s.getEnd();
  }

  /**
   * Compares the current instance to another {@link Span} object.
   *
   * @param span
   *          the span
   * @return the int
   */
  @Override
  public int compareTo(Span span) {
    return span.getStart() - getStart();
  }

  /**
   * Tests if the current instance is equal to o;.
   *
   * @param o
   *          the o
   * @return true, if successful
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (!(o instanceof Span)) {
      return false;
    }

    Span span = (Span) o;

    if (getStart() != span.getStart()) {
      return false;
    }

    return getLength() == span.getLength();
  }

  /**
   * Represents the current object as a <code>String</code>.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "Span: " + getStart() + "-" + getEnd() + " Length: " + getLength();
  }

  /**
   * Generates a hash code of the current object.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}