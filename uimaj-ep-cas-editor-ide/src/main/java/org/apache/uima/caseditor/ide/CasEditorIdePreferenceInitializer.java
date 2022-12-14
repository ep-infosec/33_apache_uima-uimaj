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

package org.apache.uima.caseditor.ide;

import java.nio.charset.Charset;

import org.apache.uima.cas.CAS;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class CasEditorIdePreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = CasEditorIdePlugin.getDefault().getPreferenceStore();
    store.setDefault(CasEditorIdePreferenceConstants.CAS_IMPORT_WIZARD_LAST_USED_LANG,
            CAS.DEFAULT_LANGUAGE_NAME);

    store.setDefault(CasEditorIdePreferenceConstants.CAS_IMPORT_WIZARD_LAST_USED_ENCODINGS,
            Charset.defaultCharset().displayName());

    store.setDefault(CasEditorIdePreferenceConstants.CAS_EDITOR_REMEMBER_TYPESYSTEM, true);

  }
}
