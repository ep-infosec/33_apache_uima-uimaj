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

package org.apache.uima.tools.cvd.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.uima.tools.cvd.MainFrame;

/**
 * The Class SetDataPathHandler.
 */
public class SetDataPathHandler implements ActionListener {

  /** The main. */
  private final MainFrame main;

  /**
   * Instantiates a new sets the data path handler.
   *
   * @param frame
   *          the frame
   */
  public SetDataPathHandler(MainFrame frame) {
    this.main = frame;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    String result = (String) JOptionPane.showInputDialog(this.main, "Specify the data path",
            "Set data path", JOptionPane.PLAIN_MESSAGE, null, null, this.main.getDataPathName());
    if (result != null) {
      this.main.setDataPath(result);
    }
  }

}
