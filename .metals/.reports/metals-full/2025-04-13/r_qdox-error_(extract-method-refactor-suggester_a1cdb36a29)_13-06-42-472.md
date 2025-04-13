error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17041.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17041.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17041.java
text:
```scala
.@@getToolkit(), editor.getExtXptFacade());

/**
 * 
 */
package org.eclipse.emf.editor.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.editor.EEditor;
import org.eclipse.emf.editor.ui.binding.EmfSwtBindingFactory;
import org.eclipse.emf.editor.ui.binding.MultipleFeatureControl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;


/**
 * @author Dennis Huebner
 * 
 */
public class GenericDetailsPage extends AbstractFormPart implements
		ISelectionProvider, IDetailsPage {
	private final class EStructuralfeatureComparator implements
			Comparator<EStructuralFeature> {
		public int compare(EStructuralFeature o1, EStructuralFeature o2) {
			return nullSafe(o1).compareTo(nullSafe(o2));
		}

		/**
		 * @param o1
		 * @return
		 */
		private String nullSafe(EStructuralFeature o) {
			String name = o.getName();
			return name != null ? name : new String();
		}
	}

	private EObjectImpl input;

	private Composite main;
	private EEditor editor;

	public GenericDetailsPage(EObjectImpl object, EEditor editor) {
		this.input = object;
		this.editor = editor;
	}

	public void createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		Section sec = getManagedForm().getToolkit().createSection(parent,
				ExpandableComposite.TITLE_BAR);
		sec.marginWidth = 10;
		sec.marginHeight = 5;
		sec.setText("Properties");
		main = getManagedForm().getToolkit().createComposite(sec);
		main.setLayout(new GridLayout(2, false));
		MenuManager mm = new MenuManager("#DetailsPartMenu");
		mm.setRemoveAllWhenShown(false);
		mm.add(new Action("Reset to default") {
			@Override
			public void run() {
				main.getDisplay().syncExec(new Runnable() {
					public void run() {
						if (getSelection() instanceof StructuredSelection) {
							Object o = ((StructuredSelection) getSelection())
									.getFirstElement();
							// just single selection is allowed (which has
							// focus)
							if (o instanceof EStructuralFeature) {
								EStructuralFeature feature = (EStructuralFeature) o;

								// Reusing already implemented logic
								IItemPropertyDescriptor desc = new ItemPropertyDescriptor(
										editor.getAdapterFactory(), null,
										feature.getName(), new String(),
										feature, true);
								if (desc.isPropertySet(input)) {
									desc.resetPropertyValue(input);
									if (feature.isMany()) {
										// TODO work around for ListViewer in
										// MultipleFeatureControl
										Control locatedControl = locateControl(
												EcorePackage.Literals.ESTRUCTURAL_FEATURE
														.getName(), feature);
										if (locatedControl instanceof MultipleFeatureControl) {
											MultipleFeatureControl mfc = (MultipleFeatureControl) locatedControl;
											mfc.quietClearSelection();
										}
									}
								}
							}
						}
					}
				});
			}
		});

		// IActionBars actionBars = editor.getActionBars();
		// IAction oldCopyAction = actionBars
		// .getGlobalActionHandler(ActionFactory.COPY.getId());
		// actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
		// new ActionWrapper(oldCopyAction));
		IWorkbenchWindow window = editor.getSite().getWorkbenchWindow();

		mm.add(ActionFactory.COPY.create(window));
		mm.add(new Separator("additions"));
		Menu menu = mm.createContextMenu(main);

		editor.getSite().registerContextMenu(mm.getId(), mm, this);
		createGenericPart(menu);
		sec.setClient(main);
	}

	private void createGenericPart(Menu menu) {
		if (input != null) {
			cleanUpMainComposite();
			EList<EStructuralFeature> allStructuralFeatures = new BasicEList<EStructuralFeature>(
					input.eClass().getEAllStructuralFeatures());
			Collections.sort(allStructuralFeatures,
					new EStructuralfeatureComparator());

			factory = new EmfSwtBindingFactory(editor.getAdapterFactory(),
					editor.getEditingDomain(), input, main, getManagedForm()
							.getToolkit(), editor.getOawFacade());
			final IActionBars actionBars = editor.getActionBars();

			final IAction ecoreCopy = actionBars
					.getGlobalActionHandler(ActionFactory.COPY.getId());
			final IAction ecoreCut = actionBars
					.getGlobalActionHandler(ActionFactory.CUT.getId());
			final IAction ecorePaste = actionBars
					.getGlobalActionHandler(ActionFactory.PASTE.getId());
			final IAction ecoreDelete = actionBars
					.getGlobalActionHandler(ActionFactory.DELETE.getId());

			for (final EStructuralFeature feature : allStructuralFeatures) {
				// derived, unchangeable, container and containment features
				// ignored
				if (feature.isChangeable()
						&& !feature.isDerived()
						&& !(feature instanceof EReference && (((EReference) feature)
								.isContainment() || ((EReference) feature)
								.isContainer()))) {
					createLabel(editor.getExtendedReflectiveItemProvider()
							.getTextForFeature(feature));

					final Control contr = factory.create(feature);

					contr.setMenu(menu);
					contr.addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(
								org.eclipse.swt.events.FocusEvent e) {
							// FIXME hook ecore's action contributor or create
							// an own
							if (contr instanceof Text) {
								TextActionHandler textHandlerh = new TextActionHandler(
										actionBars);
								Text t = (Text) contr;
								textHandlerh.addText(t);

								textHandlerh.setCopyAction(ecoreCopy);
								textHandlerh.setCutAction(ecoreCut);
								textHandlerh.setPasteAction(ecorePaste);
								textHandlerh.setDeleteAction(ecoreDelete);
								actionBars.updateActionBars();

							}
							setSelection(new StructuredSelection(feature));
						}

						@Override
						public void focusLost(FocusEvent e) {
							actionBars.setGlobalActionHandler(
									ActionFactory.COPY.getId(), ecoreCopy);
							actionBars.setGlobalActionHandler(ActionFactory.CUT
									.getId(), ecoreCut);
							actionBars.setGlobalActionHandler(
									ActionFactory.DELETE.getId(), ecoreDelete);
							actionBars.setGlobalActionHandler(
									ActionFactory.PASTE.getId(), ecorePaste);
						}
					});

				}
			}

			getManagedForm().getToolkit().paintBordersFor(main);
		}
	}

	/**
	 * 
	 */
	private void cleanUpMainComposite() {
		for (Control child : main.getChildren()) {
			child.dispose();
		}
	}

	private Label createLabel(String string) {
		Label lab = getManagedForm().getToolkit().createLabel(main, string);
		lab.setLayoutData(new GridData());
		return lab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	@Override
	public void dispose() {

		if (factory != null) {
			factory.dispose();
		}
		if (!main.isDisposed() && !main.getParent().isDisposed())
			main.getParent().dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	@Override
	public void setFocus() {
		main.setFocus();
	}

	public Control locateControl(String key, Object data) {
		for (Control c : main.getChildren()) {
			Object controlsData = c.getData(key);
			if (controlsData != null && controlsData.equals(data)) {
				return c;
			}
		}
		return null;
	}

	private List<ISelectionChangedListener> selListeners = new ArrayList<ISelectionChangedListener>();
	private ISelection partSelection = null;
	private EmfSwtBindingFactory factory;

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.selListeners.add(listener);
	}

	public ISelection getSelection() {
		return partSelection;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		this.selListeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		partSelection = selection;
		for (ISelectionChangedListener listener : selListeners) {
			listener
					.selectionChanged(new SelectionChangedEvent(this, selection));
		}
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		// TODO Auto-generated method stub

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17041.java