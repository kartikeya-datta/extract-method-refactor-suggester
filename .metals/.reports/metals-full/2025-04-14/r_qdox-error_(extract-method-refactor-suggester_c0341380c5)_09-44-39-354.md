error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2247.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2247.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2247.java
text:
```scala
N@@ode keep = (Node)Objects.cloneObject(currentStep);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.wizard.framework;

import wicket.AttributeModifier;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import wicket.util.lang.Objects;
import wicket.version.undo.Change;

/**
 * The main wizard panel.
 *
 * @author Eelco Hillenius
 */
public class WizardPanel extends Panel
{
	/** state of the wizard. */
	final WizardState state;

	/**
	 * @param id component id
	 * @param configuration wizard configuration object
	 */
	public WizardPanel(final String id, WizardConfiguration configuration)
	{
		super(id);

		if (configuration == null)
		{
			throw new NullPointerException("configuration must not be null");
		}

		this.state = configuration.begin();

		WizardForm form = new WizardForm("form");
		add(form);
	}

	/**
	 * Gets the wizard state object.
	 * @return the wizard state object
	 */
	protected final WizardState getState()
	{
		return state;
	}

	/**
	 * Gets the editor for the given node.
	 * @param editorId the id that must be used to create the editor
	 * @return the editor panel
	 */
	protected Panel newEditor(String editorId)
	{
		Node node = state.getCurrentNode();
		if(node != null)
		{
			Panel editor = node.newEditor(editorId);
			if (editor != null)
			{
				return editor;
			}
		}

		return new EmptyPanel(editorId);
	}

	/**
	 * Form for wizard node.
	 */
	private final class WizardForm extends Form
	{
		/**
		 * Construct.
		 * @param id component id
		 */
		public WizardForm(String id)
		{
			super(id);
			Panel editor = newEditor("editor");
			add(editor);

			Button previousButton = new Button("previous")
			{
				protected void onSubmit()
				{
					Node current = state.getCurrentNode();
					if(current instanceof Step)
					{
						record(current);
						TransitionLabel result = ((Step)current).previous(WizardForm.this);
						state.move(result);
						WizardForm.this.replace(newEditor("editor"));
					}
				}

				public boolean isVisible()
				{
					Node current = state.getCurrentNode();
					Transitions transitions = state.getTransitions();
					return transitions.exists(current, TransitionLabel.PREVIOUS);
				}
			};
			add(previousButton);

			Button nextButton = new Button("next")
			{
				protected void onSubmit()
				{
					Node current = state.getCurrentNode();
					if(current instanceof Step)
					{
						record(current);
						TransitionLabel result = ((Step)current).next(WizardForm.this);
						state.move(result);
						WizardForm.this.replace(newEditor("editor"));
					}
				}

				public boolean isVisible()
				{
					Node current = state.getCurrentNode();
					Transitions transitions = state.getTransitions();
					return transitions.exists(current, TransitionLabel.NEXT);
				}
			};
			add(nextButton);

			Button exitButton = new Button("exit")
			{
				protected void onSubmit()
				{
					Node current = state.getCurrentNode();
					if (current instanceof Exit)
					{
						record(current);
						TransitionLabel result = ((Step)current).next(WizardForm.this);
						((Exit)current).exit(getRequestCycle());
					}
				}

				public boolean isVisible()
				{
					return ( state.getCurrentNode() instanceof Exit );
				}
			};
			exitButton.add(new AttributeModifier("value", new Model()
			{
				public Object getObject(wicket.Component component)
				{
					Node current = state.getCurrentNode();
					if(current instanceof Exit)
					{
						return ((Exit)current).getLabel();
					}
					return null;
				}
			}));
			add(exitButton);
		}
	}

	/**
	 * Record current state.
	 * @param currentStep step to record for undoing
	 */
	protected void record(final Node currentStep)
	{
		addStateChange(new Change()
		{
			Node keep = (Node)Objects.clone(currentStep);

			public void undo()
			{
				state.setCurrentNode(keep);
			}
		});
	}

	/**
	 * An empty do-nothing panel.
	 */
	private final class EmptyPanel extends Panel
	{
		/**
		 * Construct.
		 * @param id component id
		 */
		public EmptyPanel(String id)
		{
			super(id);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2247.java