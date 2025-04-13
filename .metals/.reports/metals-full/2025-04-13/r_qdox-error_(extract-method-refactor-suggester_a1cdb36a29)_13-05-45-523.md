error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4472.java
text:
```scala
a@@ddTest(Test_org_eclipse_swt_widgets_DateTime.suite());

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for running all SWT test cases.
 */
public class AllTests extends TestSuite {

public static void main(String[] args) {
	SwtTestCase.unimplementedMethods = 0;
	TestRunner.run(suite());
	if (SwtTestCase.unimplementedMethods > 0) {
		System.out.println("\nCalls to warnUnimpl: " + SwtTestCase.unimplementedMethods);
	}
}
public static Test suite() {
	return new AllTests();
}

public AllTests() {
	super();
	/* The logical order to run the tests in is:
	 * - SWT, SWTError, SWTException
	 * - Display
	 * - graphics classes
	 * - items and Caret, etc
	 * - widgets
	 * - dialogs
	 * - layout
	 * - custom widgets
	 * - printing and program
	 * - events
	 * - drag & drop
	 * - accessibility
	 * - OLE
	 * - browser
	 */
	addTest(Test_org_eclipse_swt_SWT.suite());
	addTest(Test_org_eclipse_swt_SWTException.suite());
	addTest(Test_org_eclipse_swt_SWTError.suite());

	/* NOTE: If the Display test suite is run, it must be run
	 * before any other tests that need a display (i.e. graphics
	 * or widget tests, etc). Otherwise, an InvalidThreadAccess
	 * exception will be thrown for each Display test.
	 */
	addTest(Test_org_eclipse_swt_widgets_Display.suite());

	addTest(Test_org_eclipse_swt_graphics_Image.suite());
	addTest(Test_org_eclipse_swt_graphics_Cursor.suite());
	addTest(Test_org_eclipse_swt_graphics_DeviceData.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageLoaderEvent.suite());
	addTest(Test_org_eclipse_swt_graphics_RGB.suite());
	addTest(Test_org_eclipse_swt_graphics_Font.suite());
	addTest(Test_org_eclipse_swt_graphics_Rectangle.suite());
	addTest(Test_org_eclipse_swt_graphics_FontData.suite());
	addTest(Test_org_eclipse_swt_graphics_GC.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageData.suite());
	addTest(Test_org_eclipse_swt_graphics_Region.suite());
	addTest(Test_org_eclipse_swt_graphics_GCData.suite());
	addTest(Test_org_eclipse_swt_graphics_FontMetrics.suite());
	addTest(Test_org_eclipse_swt_graphics_Color.suite());
	addTest(Test_org_eclipse_swt_graphics_Point.suite());
	addTest(Test_org_eclipse_swt_graphics_PaletteData.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageLoader.suite());

	addTest(Test_org_eclipse_swt_widgets_ExpandItem.suite());
	addTest(Test_org_eclipse_swt_widgets_MenuItem.suite());
	addTest(Test_org_eclipse_swt_widgets_ToolItem.suite());
	//addTest(Test_org_eclipse_swt_widgets_CoolItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TabItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TableItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TableColumn.suite());
	addTest(Test_org_eclipse_swt_widgets_TreeItem.suite());
	addTest(Test_org_eclipse_swt_widgets_Caret.suite());
	addTest(Test_org_eclipse_swt_widgets_Event.suite());
	addTest(Test_org_eclipse_swt_widgets_TypedListener.suite());
	addTest(Test_org_eclipse_swt_widgets_Menu.suite());
	//addTest(Test_org_eclipse_swt_widgets_Tracker.suite());
	addTest(Test_org_eclipse_swt_widgets_Synchronizer.suite());
	
	addTest(Test_org_eclipse_swt_widgets_Shell.suite());
	//addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	addTest(Test_org_eclipse_swt_widgets_Label.suite());
	addTest(Test_org_eclipse_swt_widgets_Button.suite());
	addTest(Test_org_eclipse_swt_widgets_ExpandBar.suite());
	addTest(Test_org_eclipse_swt_widgets_List.suite());
	addTest(Test_org_eclipse_swt_widgets_Text.suite());
	addTest(Test_org_eclipse_swt_widgets_ScrollBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Sash.suite());
	addTest(Test_org_eclipse_swt_widgets_Tree.suite());
	addTest(Test_org_eclipse_swt_widgets_TabFolder.suite());
	addTest(Test_org_eclipse_swt_widgets_Combo.suite());
	addTest(Test_org_eclipse_swt_widgets_Group.suite());
	addTest(Test_org_eclipse_swt_widgets_ToolBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Table.suite());
	addTest(Test_org_eclipse_swt_widgets_Canvas.suite());
	addTest(Test_org_eclipse_swt_widgets_Scale.suite());
	addTest(Test_org_eclipse_swt_widgets_Slider.suite());
	addTest(Test_org_eclipse_swt_widgets_ProgressBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Composite.suite());
	addTest(Test_org_eclipse_swt_widgets_Link.suite());
	//addTest(Test_org_eclipse_swt_widgets_DateTime.suite());
	//addTest(Test_org_eclipse_swt_widgets_CoolBar.suite());

	addTest(Test_org_eclipse_swt_widgets_ColorDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_FileDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_DirectoryDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_FontDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_MessageBox.suite());
	addTest(Test_org_eclipse_swt_widgets_Monitor.suite());
	addTest(Test_org_eclipse_swt_layout_GridData.suite());
	addTest(Test_org_eclipse_swt_layout_RowData.suite());
	addTest(Test_org_eclipse_swt_layout_GridLayout.suite());
	addTest(Test_org_eclipse_swt_layout_FillLayout.suite());
	addTest(Test_org_eclipse_swt_layout_RowLayout.suite());
	addTest(Test_org_eclipse_swt_layout_FormLayout.suite());

	addTest(Test_org_eclipse_swt_custom_TableTree.suite());
	addTest(Test_org_eclipse_swt_custom_BidiSegmentEvent.suite());
	addTest(Test_org_eclipse_swt_custom_LineBackgroundEvent.suite());
	addTest(Test_org_eclipse_swt_custom_LineStyleEvent.suite());
	addTest(Test_org_eclipse_swt_custom_SashForm.suite());
	addTest(Test_org_eclipse_swt_custom_TableEditor.suite());
	addTest(Test_org_eclipse_swt_custom_AnimatedProgress.suite());
	addTest(Test_org_eclipse_swt_custom_StyleRange.suite());
	addTest(Test_org_eclipse_swt_custom_TextChangedEvent.suite());
	addTest(Test_org_eclipse_swt_custom_StackLayout.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolderEvent.suite());
	addTest(Test_org_eclipse_swt_custom_ExtendedModifyEvent.suite());
	addTest(Test_org_eclipse_swt_custom_CCombo.suite());
	addTest(Test_org_eclipse_swt_custom_TableTreeItem.suite());
	addTest(Test_org_eclipse_swt_custom_ViewForm.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolderAdapter.suite());
	addTest(Test_org_eclipse_swt_custom_CLabel.suite());
	addTest(Test_org_eclipse_swt_custom_TableTreeEditor.suite());
	addTest(Test_org_eclipse_swt_custom_TextChangingEvent.suite());
	addTest(Test_org_eclipse_swt_custom_ScrolledComposite.suite());
	addTest(Test_org_eclipse_swt_custom_TreeEditor.suite());
	addTest(Test_org_eclipse_swt_custom_PopupList.suite());
	addTest(Test_org_eclipse_swt_custom_CTabItem.suite());
	addTest(Test_org_eclipse_swt_custom_BusyIndicator.suite());
	addTest(Test_org_eclipse_swt_custom_StyledText.suite());
	addTest(Test_org_eclipse_swt_custom_ControlEditor.suite());
	addTest(Test_org_eclipse_swt_custom_ST.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolder.suite());

	addTest(Test_org_eclipse_swt_events_ControlEvent.suite());
	addTest(Test_org_eclipse_swt_events_ModifyEvent.suite());
	addTest(Test_org_eclipse_swt_events_ArmEvent.suite());
	addTest(Test_org_eclipse_swt_events_ShellEvent.suite());
	addTest(Test_org_eclipse_swt_events_TypedEvent.suite());
	addTest(Test_org_eclipse_swt_events_PaintEvent.suite());
	addTest(Test_org_eclipse_swt_events_VerifyEvent.suite());
	addTest(Test_org_eclipse_swt_events_KeyEvent.suite());
	addTest(Test_org_eclipse_swt_events_TraverseEvent.suite());
	addTest(Test_org_eclipse_swt_events_MouseTrackAdapter.suite());
	addTest(Test_org_eclipse_swt_events_DisposeEvent.suite());
	addTest(Test_org_eclipse_swt_events_SelectionEvent.suite());
	addTest(Test_org_eclipse_swt_events_HelpEvent.suite());
	addTest(Test_org_eclipse_swt_events_FocusEvent.suite());
	addTest(Test_org_eclipse_swt_events_MouseEvent.suite());
	addTest(Test_org_eclipse_swt_events_MenuEvent.suite());
	addTest(Test_org_eclipse_swt_events_TreeEvent.suite());
	
	addTest(Test_org_eclipse_swt_printing_PrintDialog.suite());
	addTest(Test_org_eclipse_swt_printing_PrinterData.suite());
	addTest(Test_org_eclipse_swt_printing_Printer.suite());

	addTest(Test_org_eclipse_swt_program_Program.suite());

//	addTest(Test_org_eclipse_swt_dnd_FileTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSourceAdapter.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTargetAdapter.suite());
//	addTest(Test_org_eclipse_swt_dnd_TextTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSourceEvent.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSource.suite());
//	addTest(Test_org_eclipse_swt_dnd_DND.suite());
//	addTest(Test_org_eclipse_swt_dnd_TransferData.suite());
//	addTest(Test_org_eclipse_swt_dnd_RTFTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_Clipboard.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTargetEvent.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTarget.suite());

	addTest(Test_org_eclipse_swt_accessibility_ACC.suite());
	addTest(Test_org_eclipse_swt_accessibility_Accessible.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleAdapter.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleControlAdapter.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleControlEvent.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleControlListener.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleEvent.suite());
	addTest(Test_org_eclipse_swt_accessibility_AccessibleListener.suite());

	addTest(Test_org_eclipse_swt_ole_win32_OLE.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleAutomation.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleClientSite.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleControlSite.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleEvent.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleFrame.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleFunctionDescription.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleListener.suite());
	addTest(Test_org_eclipse_swt_ole_win32_OleParameterDescription.suite());
	addTest(Test_org_eclipse_swt_ole_win32_Variant.suite());

	addTest(Test_org_eclipse_swt_browser_Browser.suite());
	addTest(Test_org_eclipse_swt_browser_CloseWindowListener.suite());
	addTest(Test_org_eclipse_swt_browser_LocationAdapter.suite());
	addTest(Test_org_eclipse_swt_browser_LocationListener.suite());
	addTest(Test_org_eclipse_swt_browser_OpenWindowListener.suite());
	addTest(Test_org_eclipse_swt_browser_ProgressAdapter.suite());
	addTest(Test_org_eclipse_swt_browser_ProgressListener.suite());
	addTest(Test_org_eclipse_swt_browser_StatusTextListener.suite());
	addTest(Test_org_eclipse_swt_browser_TitleListener.suite());
	addTest(Test_org_eclipse_swt_browser_VisibilityWindowAdapter.suite());
	addTest(Test_org_eclipse_swt_browser_VisibilityWindowListener.suite());
	addTest(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.suite());


}
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4472.java