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

package org.apache.uima.caseditor.editor.outline;

import java.util.Collection;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.Images;
import org.apache.uima.caseditor.editor.AnnotationEditor;
import org.apache.uima.caseditor.editor.CasEditorError;
import org.apache.uima.caseditor.editor.IAnnotationEditorModifyListener;
import org.apache.uima.caseditor.editor.action.DeleteFeatureStructureAction;
import org.apache.uima.caseditor.editor.action.LowerLeftAnnotationSideAction;
import org.apache.uima.caseditor.editor.action.LowerRightAnnotationSideAction;
import org.apache.uima.caseditor.editor.action.MergeAnnotationAction;
import org.apache.uima.caseditor.editor.action.WideLeftAnnotationSideAction;
import org.apache.uima.caseditor.editor.action.WideRightAnnotationSideAction;
import org.apache.uima.caseditor.editor.util.AnnotationSelection;
import org.apache.uima.caseditor.editor.util.FeatureStructureTransfer;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * This outline view displays all <code>AnnotationFS</code>s of the current mode/type from the
 * binded editor.
 */
public final class AnnotationOutline extends ContentOutlinePage implements ISelectionListener {

  /**
   * This listener receive events from the bound editor.
   */
  protected class EditorListener implements IAnnotationEditorModifyListener {

    /**
     * Called if the editor annotation mode was changed.
     *
     * @param newMode
     *          the new mode
     */
    @Override
    public void annotationModeChanged(Type newMode) {
      changeAnnotationMode();
      mTableViewer.refresh();
    }

    @Override
    public void showAnnotationsChanged(Collection<Type> shownAnnotationTypes) {
      mTableViewer.refresh();
    }
  }

  /**
   * Selects all elements in the tree viewer.
   */
  private class SelectAllAction extends Action {

    /**
     * Selects all elements in the tree viewer.
     */
    @Override
    public void run() {
      mTableViewer.getTree().selectAll();
      mTableViewer.setSelection(mTableViewer.getSelection());
    }
  }

  /** The editor change listener. */
  private IAnnotationEditorModifyListener editorChangeListener;

  /** The style. */
  private OutlineStyles style = OutlineStyles.TYPE;

  /** The m outline composite. */
  private Composite mOutlineComposite;

  /** The m table viewer. */
  private TreeViewer mTableViewer;

  /**
   * The <code>AnnotationEditor</code> which is bound to this outline view.
   */
  private AnnotationEditor editor;

  /**
   * Creates a new <code>AnnotationOutline</code> object.
   *
   * @param editor
   *          - the editor to bind
   */
  public AnnotationOutline(AnnotationEditor editor) {
    this.editor = editor;
  }

  /**
   * Creates the outline table control.
   *
   * @param parent
   *          the parent
   */
  @Override
  public void createControl(Composite parent) {
    mOutlineComposite = new Composite(parent, SWT.NONE);
    mOutlineComposite.setLayout(new FillLayout());

    createTableViewer(mOutlineComposite);
    mOutlineComposite.layout(true);

    getSite().getPage().addSelectionListener(this);
    getSite().setSelectionProvider(mTableViewer);

    changeAnnotationMode();

    editorChangeListener = new EditorListener();
    editor.addAnnotationListener(editorChangeListener);

    DragSource source = new DragSource(mTableViewer.getTree(), DND.DROP_COPY);

    source.setTransfer(new Transfer[] { FeatureStructureTransfer.getInstance() });

    source.addDragListener(new DragSourceListener() {
      TreeItem dragSourceItem = null;

      @Override
      public void dragStart(DragSourceEvent event) {
        TreeItem[] selection = mTableViewer.getTree().getSelection();

        if (selection.length > 0) {
          event.doit = true;
          dragSourceItem = selection[0];
        } else {
          event.doit = false;
        }
      }

      @Override
      public void dragSetData(DragSourceEvent event) {
        IAdaptable adaptable = (IAdaptable) dragSourceItem.getData();

        event.data = adaptable.getAdapter(FeatureStructure.class);
      }

      @Override
      public void dragFinished(DragSourceEvent event) {
        // not needed
      }
    });
  }

  /**
   * Retrieves the control.
   *
   * @return the control
   */
  @Override
  public Control getControl() {
    return mOutlineComposite;
  }

  /**
   * Adds the these actions to the global action handler: {@link DeleteFeatureStructureAction}
   * SelectAllAction.
   *
   * @param actionBars
   *          the new action bars
   */
  @Override
  public void setActionBars(IActionBars actionBars) {
    DeleteFeatureStructureAction deleteAction = new DeleteFeatureStructureAction(editor);

    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

    getSite().getSelectionProvider().addSelectionChangedListener(deleteAction);

    actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new SelectAllAction());

    Action action = new SwitchStyleAction(this);

    IMenuManager dropDownMenu = actionBars.getMenuManager();
    dropDownMenu.add(action);

    IToolBarManager toolBarManager = actionBars.getToolBarManager();

    // wide left annotation side action
    WideLeftAnnotationSideAction wideLeftAnnotationSideAction = new WideLeftAnnotationSideAction(
            editor);
    wideLeftAnnotationSideAction.setActionDefinitionId(WideLeftAnnotationSideAction.ID);
    wideLeftAnnotationSideAction.setText("Wides the left annotation side");
    wideLeftAnnotationSideAction
            .setImageDescriptor(CasEditorPlugin.getTaeImageDescriptor(Images.WIDE_LEFT_SIDE));

    getSite().getSelectionProvider().addSelectionChangedListener(wideLeftAnnotationSideAction);
    actionBars.setGlobalActionHandler(WideLeftAnnotationSideAction.ID,
            wideLeftAnnotationSideAction);
    toolBarManager.add(wideLeftAnnotationSideAction);

    // lower left annotation side action
    LowerLeftAnnotationSideAction lowerLeftAnnotationSideAction = new LowerLeftAnnotationSideAction(
            editor);
    lowerLeftAnnotationSideAction.setActionDefinitionId(LowerLeftAnnotationSideAction.ID);
    lowerLeftAnnotationSideAction.setText("Lowers the left annotation side");
    lowerLeftAnnotationSideAction
            .setImageDescriptor(CasEditorPlugin.getTaeImageDescriptor(Images.LOWER_LEFT_SIDE));

    getSite().getSelectionProvider().addSelectionChangedListener(lowerLeftAnnotationSideAction);
    actionBars.setGlobalActionHandler(LowerLeftAnnotationSideAction.ID,
            lowerLeftAnnotationSideAction);
    toolBarManager.add(lowerLeftAnnotationSideAction);

    // lower right annotation side action
    LowerRightAnnotationSideAction lowerRightAnnotationSideAction = new LowerRightAnnotationSideAction(
            editor);
    lowerRightAnnotationSideAction.setActionDefinitionId(LowerRightAnnotationSideAction.ID);
    lowerRightAnnotationSideAction.setText("Lowers the right annotation side");
    lowerRightAnnotationSideAction
            .setImageDescriptor(CasEditorPlugin.getTaeImageDescriptor(Images.LOWER_RIGHT_SIDE));

    getSite().getSelectionProvider().addSelectionChangedListener(lowerRightAnnotationSideAction);
    actionBars.setGlobalActionHandler(LowerRightAnnotationSideAction.ID,
            lowerRightAnnotationSideAction);
    toolBarManager.add(lowerRightAnnotationSideAction);

    // wide right annotation side action
    WideRightAnnotationSideAction wideRightAnnotationSideAction = new WideRightAnnotationSideAction(
            editor);
    wideRightAnnotationSideAction.setActionDefinitionId(WideRightAnnotationSideAction.ID);
    wideRightAnnotationSideAction.setText("Wides the right annotation side");
    wideRightAnnotationSideAction
            .setImageDescriptor(CasEditorPlugin.getTaeImageDescriptor(Images.WIDE_RIGHT_SIDE));

    getSite().getSelectionProvider().addSelectionChangedListener(wideRightAnnotationSideAction);
    actionBars.setGlobalActionHandler(WideRightAnnotationSideAction.ID,
            wideRightAnnotationSideAction);
    toolBarManager.add(wideRightAnnotationSideAction);

    // merge action
    MergeAnnotationAction mergeAction = new MergeAnnotationAction(editor);
    getSite().getSelectionProvider().addSelectionChangedListener(mergeAction);
    mergeAction.setImageDescriptor(CasEditorPlugin.getTaeImageDescriptor(Images.MERGE));

    toolBarManager.add(mergeAction);

    // delete action
    toolBarManager.add(ActionFactory.DELETE.create(getSite().getWorkbenchWindow()));

    super.setActionBars(actionBars);
  }

  /**
   * Current style.
   *
   * @return the outline styles
   */
  OutlineStyles currentStyle() {
    return style;
  }

  /**
   * Switch style.
   *
   * @param style
   *          the style
   */
  void switchStyle(OutlineStyles style) {

    this.style = style;

    if (OutlineStyles.MODE.equals(style)) {
      mTableViewer.setContentProvider(new ModeSensitiveContentProvider(editor, mTableViewer));
    } else if (OutlineStyles.TYPE.equals(style)) {
      mTableViewer.setContentProvider(new TypeGroupedContentProvider(editor, mTableViewer));
    } else {
      throw new CasEditorError("Unknown style!");
    }

    mTableViewer.refresh();
  }

  /**
   * Sets the focus.
   */
  @Override
  public void setFocus() {
    mOutlineComposite.setFocus();
  }

  /**
   * Change annotation mode.
   */
  private void changeAnnotationMode() {
    mTableViewer.setInput(editor.getDocument());
    mTableViewer.refresh();
  }

  /**
   * Creates the table viewer.
   *
   * @param parent
   *          the parent
   */
  private void createTableViewer(Composite parent) {
    int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;

    if (mTableViewer != null) {
      mTableViewer.getTree().dispose();
    }

    mTableViewer = new TreeViewer(parent, style);

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalSpan = 3;
    mTableViewer.getTree().setLayoutData(gridData);

    mTableViewer.getTree().setLinesVisible(true);
    mTableViewer.getTree().setHeaderVisible(true);

    TreeColumn textColumn = new TreeColumn(mTableViewer.getTree(), SWT.LEFT);
    textColumn.setText("Text");
    textColumn.setWidth(130);

    // performance optimization, the table can contain many items
    mTableViewer.setUseHashlookup(false);

    // Note: The type style is considered as the default
    mTableViewer.setContentProvider(new TypeGroupedContentProvider(editor, mTableViewer));

    mTableViewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

      // TODO: Improve this filter ... it is to sloooooooooow
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element) {
        Collection<Type> shownTypes = editor.getShownAnnotationTypes();

        if (element instanceof AnnotationTypeTreeNode) {
          AnnotationTypeTreeNode typeNode = (AnnotationTypeTreeNode) element;

          return shownTypes.contains(typeNode.getType());
        } else if (element instanceof AnnotationTreeNode) {
          AnnotationTreeNode annotationNode = (AnnotationTreeNode) element;

          return shownTypes.contains(annotationNode.getAnnotation().getType());
        } else {
          throw new CasEditorError("Unexpected element type!");
        }
      }
    } });

    mTableViewer.setLabelProvider(new OutlineLabelProvider());

    mTableViewer.setSorter(new OutlineTableSorter());

    mTableViewer.setAutoExpandLevel(3);

    // set input element here ... this is the document
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {

    boolean isForeignSelection = true;

    if (part instanceof ContentOutline) {
      ContentOutline contentOutline = (ContentOutline) part;

      IPage currentPage = contentOutline.getCurrentPage();

      if (currentPage instanceof OutlinePageBook) {
        OutlinePageBook pageBook = (OutlinePageBook) currentPage;
        isForeignSelection = pageBook.getCasViewPage() != this;
      }
    }

    if (isForeignSelection && getSite().getPage().getActiveEditor() == editor) {
      if (selection instanceof StructuredSelection) {
        AnnotationSelection annotations = new AnnotationSelection((StructuredSelection) selection);

        if (!annotations.isEmpty()) {
          ISelection tableSelection = new StructuredSelection(
                  new AnnotationTreeNode(editor.getDocument(), annotations.getFirst()));

          mTableViewer.setSelection(tableSelection, true);
        }
      }
    }
  }

  @Override
  public void dispose() {
    getSite().setSelectionProvider(null);
    getSite().getPage().removeSelectionListener(this);
    editor.removeAnnotationListener(editorChangeListener);

    super.dispose();
  }

  /**
   * Gets the viewer.
   *
   * @return the viewer
   */
  Viewer getViewer() {
    return mTableViewer;
  }
}
