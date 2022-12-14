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

package org.apache.uima.ep_launcher;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.PackageAdmin;

// TODO: Auto-generated Javadoc
/**
 * The Class LauncherPlugin.
 */
public class LauncherPlugin extends Plugin {

  /** The Constant ID. */
  public static final String ID = "org.apache.uima.launcher";

  /** The plugin. */
  private static LauncherPlugin plugin;

  /** The bundle context. */
  private BundleContext bundleContext;

  /**
   * Instantiates a new launcher plugin.
   */
  public LauncherPlugin() {
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    bundleContext = context;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    plugin = null;
  }

  /**
   * Gets the bundles.
   *
   * @param bundleName
   *          the bundle name
   * @param version
   *          the version
   * @return the bundles
   */
  public Bundle[] getBundles(String bundleName, String version) {

    Bundle[] bundles = Platform.getBundles(bundleName, version);
    if (bundles != null) {
      return bundles;
    }

    // Accessing bundle which is not resolved
    PackageAdmin admin = (PackageAdmin) bundleContext
            .getService(bundleContext.getServiceReference(PackageAdmin.class.getName()));
    bundles = admin.getBundles(bundleName, version);
    if (bundles != null && bundles.length > 0) {
      return bundles;
    }

    return null;
  }

  /**
   * Gets the bundle.
   *
   * @param bundleName
   *          the bundle name
   * @return the bundle
   */
  public Bundle getBundle(String bundleName) {
    Bundle[] bundles = getBundles(bundleName, null);
    if (bundles != null && bundles.length > 0) {
      // return fist bundle, if multiple
      return bundles[0];
    }

    return null;
  }

  /**
   * Returns the shared instance.
   *
   * @return the default
   */
  public static LauncherPlugin getDefault() {
    return plugin;
  }
}
