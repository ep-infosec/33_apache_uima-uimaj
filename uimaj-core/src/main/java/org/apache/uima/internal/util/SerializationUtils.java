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

package org.apache.uima.internal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Serialize and Deserialize arbitrary objects to/from byte arrays, using standard Java object
 * serialization/deserialization support.
 * 
 * Used in the Vinci transport to serialize/deserialize CASSerializer objects or
 * CASCompleteSerializer objects (includes type system and index definitions).
 * 
 * This class is abstract only to prevent instantiation. All the methods are static.
 */
public abstract class SerializationUtils {

  /**
   * Serializes an object to a byte array.
   * 
   * @param aObject
   *          object to serialize
   * 
   * @return <code>aObject</code> encoded as a byte array. If <code>aObject</code> is
   *         <code>null</code>, <code>null</code> is returned.
   * 
   * @throws IOException
   *           if an I/O error occurs
   */
  public static byte[] serialize(Serializable aObject) throws IOException {
    if (aObject == null) {
      return null;
    }

    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream)) {
      objStream.writeObject(aObject);
      objStream.flush();
      return byteStream.toByteArray();
    }
  }

  /**
   * Deserializes an object from a byte array.
   * 
   * @param aBytes
   *          byte array to read from
   * 
   * @return The <code>Object</code> deserialized from <code>aBytes</code>. If <code>aBytes</code>
   *         is <code>null</code>, <code>null</code> is returned.
   * 
   * @throws IOException
   *           if an I/O error occurs
   * @throws ClassNotFoundException
   *           if a required class could not be found
   */
  public static Object deserialize(byte[] aBytes) throws IOException, ClassNotFoundException {
    if (aBytes == null) {
      return null;
    }

    try (ByteArrayInputStream byteStream = new ByteArrayInputStream(aBytes);
            ObjectInputStream objStream = new ObjectInputStream(byteStream)) {
      return objStream.readObject();
    }
  }
}
