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

package org.apache.uima.caseditor.ide.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.SerialFormat;
import org.apache.uima.caseditor.core.TaeError;
import org.apache.uima.internal.util.XMLUtils;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.CasIOUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

/**
 */
final class DocumentImportStructureProvider implements IImportStructureProvider {

  private final String language;

  private final String importEncoding; // https://issues.apache.org/jira/browse/UIMA-1808

  private final SerialFormat casFormat;

  /**
   * Constructs a new DocumentImportStructureProvider object.
   *
   * @param containerFullPath
   */
  public DocumentImportStructureProvider(String language, String importEncoding,
          SerialFormat casFormat) {
    this.language = language;
    this.importEncoding = importEncoding; // https://issues.apache.org/jira/browse/UIMA-1808
    this.casFormat = casFormat;
  }

  private static String removeNonXmlChars(String input) {

    char inputChars[] = input.toCharArray();

    StringBuilder cleanedString = new StringBuilder(inputChars.length);

    int startIndex = 0;

    int offendingCharOsset;
    while ((offendingCharOsset = XMLUtils.checkForNonXmlCharacters(inputChars, startIndex,
            inputChars.length - startIndex, false)) != -1) {
      cleanedString.append(inputChars, startIndex, offendingCharOsset - startIndex);
      startIndex = offendingCharOsset + 1;
    }

    cleanedString.append(inputChars, startIndex, inputChars.length - startIndex);

    return cleanedString.toString();
  }

  @Override
  public List<Object> getChildren(Object element) {
    return null;
  }

  private static CAS createEmtpyCAS() {
    XMLInputSource xmlTypeSystemSource = new XMLInputSource(
            DocumentImportStructureProvider.class.getResourceAsStream("ts.xml"), new File(""));
    XMLParser xmlParser = UIMAFramework.getXMLParser();

    TypeSystemDescription typeSystemDesciptor;

    try {
      typeSystemDesciptor = (TypeSystemDescription) xmlParser.parse(xmlTypeSystemSource);
    } catch (InvalidXMLException e1) {
      throw new TaeError("Integrated ts.xml typesystem descriptor is not valid!");
    }

    try {
      return CasCreationUtils.createCas(typeSystemDesciptor, null, null);
    } catch (ResourceInitializationException e) {

      // should not happen
      throw new TaeError("Unexpected exception!");
    }
  }

  private InputStream getDocument(String fileName, String text, String language,
          SerialFormat format) {

    String failedToImportLine = "Failed to import: " + fileName + "\n\n";

    CAS cas = createEmtpyCAS();
    cas.setDocumentText(removeNonXmlChars(text));
    cas.setDocumentLanguage(language);

    ByteArrayOutputStream out = new ByteArrayOutputStream(40000);

    try {
      CasIOUtils.save(cas, out, format);
    } catch (IOException e) {
      throw new TaeError(failedToImportLine + e.getMessage(), e);
    }

    return new ByteArrayInputStream(out.toByteArray());
  }

  @Override
  public InputStream getContents(Object element) {
    File fileToImport = (File) element;

    String fileName = fileToImport.getName();

    if (fileName.endsWith(".rtf")) {
      try (InputStream in = new FileInputStream(fileToImport)) {
        String text = convert(in);
        return getDocument(fileToImport.getAbsolutePath(), text, language, casFormat);
      } catch (IOException e) {
        return null;
      }
    } else if (fileName.endsWith(".txt")) {
      try {
        String text = new String(Files.readAllBytes(fileToImport.toPath()), importEncoding);
        return getDocument(fileToImport.getAbsolutePath(), text, language, casFormat);
      } catch (IOException e) {
        return null;
      }
    } else {
      try {
        return new FileInputStream(fileToImport);
      } catch (FileNotFoundException e) {
        return null;
      }
    }
  }

  private String convert(InputStream rtfDocumentInputStream) throws IOException {
    RTFEditorKit aRtfEditorkit = new RTFEditorKit();

    StyledDocument styledDoc = new DefaultStyledDocument();

    String textDocument;

    try {
      aRtfEditorkit.read(rtfDocumentInputStream, styledDoc, 0);

      textDocument = styledDoc.getText(0, styledDoc.getLength());
    } catch (BadLocationException e) {
      throw new IOException("Error during parsing");
    }

    return textDocument;
  }

  @Override
  public String getFullPath(Object element) {
    return "";
  }

  @Override
  public String getLabel(Object element) {
    File fileToImport = (File) element;

    String fileName = fileToImport.getName();

    if (fileName.endsWith(".rtf") || fileName.endsWith(".txt")) {
      int nameWithouEndingLength = fileName.lastIndexOf(".");
      String nameWithouEnding = fileName.substring(0, nameWithouEndingLength);

      String ending = casFormat.getDefaultFileExtension();
      return nameWithouEnding + "." + ending;
    } else {
      return fileName;
    }
  }

  @Override
  public boolean isFolder(Object element) {
    return ((File) element).isDirectory();
  }
}
