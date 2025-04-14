error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4804.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4804.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4804.java
text:
```scala
d@@efinitions[i].setEnabled(checked);

package org.eclipse.ui.internal.dialogs;

/**********************************************************************
Copyright (c) 2000, 2002 IBM Corp.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v0.5
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v05.html
 
Contributors:
**********************************************************************/
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.decorators.*;
import org.eclipse.ui.internal.misc.Sorter;

/**
 * The DecoratorsPreferencePage is the preference page for enabling and disabling
 * the decorators in the image and for giving the user a description of the decorator.
 */
public class DecoratorsPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	private Text descriptionText;
	private CheckboxTableViewer checkboxViewer;

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		
		Font font = parent.getFont();
		
		WorkbenchHelp.setHelp(parent, IHelpContextIds.DECORATORS_PREFERENCE_PAGE);

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mainComposite.setFont(font);

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 10;
		mainComposite.setLayout(layout);

		Label topLabel = new Label(mainComposite, SWT.NONE);
		topLabel.setText(
			WorkbenchMessages.getString("DecoratorsPreferencePage.explanation")); //$NON-NLS-1$
		topLabel.setFont(font);
		
		createDecoratorsArea(mainComposite);
		createDescriptionArea(mainComposite);
		populateDecorators();

		return mainComposite;
	}

	/** 
	 * Creates the widgets for the list of decorators.
	 */
	private void createDecoratorsArea(Composite mainComposite) {
		
		Font mainFont = mainComposite.getFont();
		Composite decoratorsComposite = new Composite(mainComposite, SWT.NONE);
		decoratorsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout decoratorsLayout = new GridLayout();
		decoratorsLayout.marginWidth = 0;
		decoratorsLayout.marginHeight = 0;
		decoratorsComposite.setLayout(decoratorsLayout);
		decoratorsComposite.setFont(mainFont);
		
		Label decoratorsLabel = new Label(decoratorsComposite, SWT.NONE);
		decoratorsLabel.setText(
			WorkbenchMessages.getString("DecoratorsPreferencePage.decoratorsLabel")); //$NON-NLS-1$
		decoratorsLabel.setFont(mainFont);
		
		// Checkbox table viewer of decorators
		checkboxViewer =
			CheckboxTableViewer.newCheckList(
				decoratorsComposite,
				SWT.SINGLE | SWT.TOP | SWT.BORDER);
		checkboxViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		checkboxViewer.getTable().setFont(decoratorsComposite.getFont());
		checkboxViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((DecoratorDefinition) element).getName();
			}
		});
		checkboxViewer.getTable().setFont(mainFont);
		
		checkboxViewer.setContentProvider(new IStructuredContentProvider() {
			
			Sorter sorter = new Sorter(){
				/*
				 * @see Sorter.compare(element,element)
				 */
				public boolean compare(Object elementOne, Object elementTwo){
					return ((DecoratorDefinition) elementTwo).getName().compareTo(((DecoratorDefinition) elementOne).getName()) > 0;
				}
			};
			
			public void dispose() {
				//Nothing to do on dispose
			}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			public Object[] getElements(Object inputElement) {
				//Make an entry for each decorator definition
				return sorter.sort((Object[]) inputElement);
			}
		
		});
		
		checkboxViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) event.getSelection();
					DecoratorDefinition definition = 
						(DecoratorDefinition) sel.getFirstElement();
					if (definition == null)
						clearDescription();
					else
						showDescription(definition);
				}
			}
		});
		
		checkboxViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				checkboxViewer.setSelection(
					new StructuredSelection(event.getElement()));
			}
		});
	}

	/** 
	 * Creates the widgets for the description.
	 */
	private void createDescriptionArea(Composite mainComposite) {
		
		Font mainFont = mainComposite.getFont();
		Composite textComposite = new Composite(mainComposite, SWT.NONE);
		textComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout textLayout = new GridLayout();
		textLayout.marginWidth = 0;
		textLayout.marginHeight = 0;
		textComposite.setLayout(textLayout);
		textComposite.setFont(mainFont);
		
		Label descriptionLabel = new Label(textComposite, SWT.NONE);
		descriptionLabel.setText(
			WorkbenchMessages.getString("DecoratorsPreferencePage.description")); //$NON-NLS-1$
		descriptionLabel.setFont(mainFont);
		
		descriptionText =
			new Text(textComposite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.setFont(mainFont);
	}

	/**
	 * Populates the list of decorators.
	 */
	private void populateDecorators() {
		DecoratorDefinition[] definitions = getAllDefinitions();
		checkboxViewer.setInput(definitions);
		for (int i = 0; i < definitions.length; i++) {
			checkboxViewer.setChecked(definitions[i], definitions[i].isEnabled());
		}
	}

	/**
	 * Show the selected description in the text.
	 */
	private void showDescription(DecoratorDefinition definition) {
		if (descriptionText == null || descriptionText.isDisposed()) {
			return;
		}
		String text = definition.getDescription();
		if (text == null || text.length() == 0)
			descriptionText.setText(
				WorkbenchMessages.getString(
					"PreferencePage.noDescription")); //$NON-NLS-1$
		else
			descriptionText.setText(text);
	}

	/**
	 * Clear the selected description in the text.
	 */
	private void clearDescription() {
		if (descriptionText == null || descriptionText.isDisposed()) {
			return;
		}
		descriptionText.setText(""); //$NON-NLS-1$
	}

	/**
	 * @see PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		super.performDefaults();
		DecoratorManager manager = (DecoratorManager) WorkbenchPlugin.getDefault().getDecoratorManager();
		DecoratorDefinition[] definitions = manager.getAllDecoratorDefinitions();
		for (int i = 0; i < definitions.length; i++) {
			checkboxViewer.setChecked(definitions[i],definitions[i].getDefaultValue());
		}
	}

	/**
	 * @see IPreferencePage#performOk()
	 */
	public boolean performOk() {
		if (super.performOk()) {
			DecoratorManager manager = getDecoratorManager();
			DecoratorDefinition[] definitions = manager.getAllDecoratorDefinitions();
			for (int i = 0; i < definitions.length; i++) {
				boolean checked = checkboxViewer.getChecked(definitions[i]);
				definitions[i].setEnabledWithErrorHandling(checked);
				
			}
			manager.reset();
			return true;
		}
		return false;
	}

	/**
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Get the decorator definitions for the workbench.
	 */
	private DecoratorDefinition[] getAllDefinitions() {
		return getDecoratorManager().getAllDecoratorDefinitions();
	}
	
	/**
	 * Get the DecoratorManager being used for this
	 */
	
	private DecoratorManager getDecoratorManager(){
		return (DecoratorManager) WorkbenchPlugin
			.getDefault()
			.getDecoratorManager();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4804.java