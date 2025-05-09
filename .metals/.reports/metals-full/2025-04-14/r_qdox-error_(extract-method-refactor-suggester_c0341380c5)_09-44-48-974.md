error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5431.java
text:
```scala
final S@@tring jarResourceUrl = getRequestCycle().urlFor(jarResourceReference).toString();

/*
<<<<<<< Applet.java
 * $Id$ $Revision$
 * $Date$
=======
 * $Id$ $Revision:
 * 1.71 $ $Date$
>>>>>>> 1.5
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.applet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import wicket.IResourceListener;
import wicket.Resource;
import wicket.ResourceReference;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.markup.html.form.IFormSubmitListener;
import wicket.markup.parser.XmlTag;
import wicket.model.IModel;
import wicket.protocol.http.IMultipartWebRequest;
import wicket.protocol.http.WebRequest;
import wicket.protocol.http.WebResponse;
import wicket.resource.ByteArrayResource;
import wicket.resource.JarResourceFactory;
import wicket.util.lang.Bytes;
import wicket.util.lang.Objects;
import wicket.util.string.Strings;
import wicket.util.upload.FileItem;

/**
 * This component integrates Swing tightly with Wicket by automatically
 * generating Swing applets on demand. An Applet is initialized on the client
 * side by the class passed to the Applet constructor. This class must implement
 * the IApplet interface.
 * <p>
 * The JAR file for a given Applet is automatically created by finding the
 * closure of class files statically referenced by the IApplet class. If any
 * classes are dynamically loaded by an Applet, they can be added to the JAR by
 * calling Applet.addClass(Class). The IApplet class will only be
 * (automatically) instantiated in the client browser's VM by the host applet
 * container implementation HostApplet. The structure and implementation of
 * HostApplet, however, is an internal implementation detail to Wicket and might
 * be changed in the future.
 * <p>
 * Once the JAR file for a given Applet subclass has been created, it is reused
 * for all future instances of that Applet class. The auto-created JAR is
 * referenced by each instance of the Applet by automatically modifying the
 * APPLET tag that the Applet is attached to. The result is a fully automatic
 * applet. Any additional attributes you set on the APPLET tag in your HTML such
 * as width and height will be left unchanged.
 * <p>
 * To add Swing behavior to an Applet, the user's implementation of IApplet
 * should populate the Container passed to the IApplet.init() method with Swing
 * components. Those components should edit the model passed into the same
 * init() method. The model can be pushed back to the server at any time (via an
 * internally executed form POST over HTTP) by manually calling the
 * setModel(Object) method on the IAppletServer interface passed to the
 * IApplet.init() method. Such a manual update is not necessary if the Applet
 * component is contained in a Form. For Applets nested within Forms, the form
 * submit will result in an automatic call via JavaScript to the
 * IAppletServer.setModel() method before the Form itself POSTs. Therefore, the
 * Applet's model will be updated by the time Form.onSubmit() is called. This
 * allows users to augment HTML forms with Applet based Swing components in a
 * modular and reusable fashion.
 * 
 * @author Jonathan Locke
 */
public class Applet extends WebComponent implements IResourceListener, IFormSubmitListener
{
	private static final long serialVersionUID = 1L;

	/** Root class for applet JAR */
	private Class appletClass;

	/** Extra root classes for applet JAR to handle dynamic loading */
	private List<Class> classes = new ArrayList<Class>(2);

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param appletClass
	 *            The class that implement's this applet
	 */
	public Applet(final String id, final Class appletClass)
	{
		super(id);
		addAppletClass(appletClass);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param model
	 *            The wicket model
	 * @param appletClass
	 *            The class that implements this applet's initialization
	 */
	public Applet(final String id, final IModel model, final Class appletClass)
	{
		super(id, model);
		addAppletClass(appletClass);
	}

	/**
	 * Add class root to applet. It should only be necessary to call this method
	 * in the rare case that your applet does dynamic class loading.
	 * 
	 * @param c
	 *            The class to add
	 */
	public void addClass(final Class c)
	{
		classes.add(c);
	}

	/**
	 * By default, an Applet's model is the Wicket model. In the case of some
	 * Sprockets, the model may be something entirely different that depends on
	 * the communication needs of the individual component.
	 * 
	 * @return The model for this applet
	 */
	public Object getAppletModel()
	{
		return getModelObject();
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Called when model data is posted back from the client applet. This method
	 * reads the posted object from the form data and calls setAppletModel()
	 * with that object.
	 */
	public void onFormSubmitted()
	{
		// Get request
		final WebRequest webRequest = ((WebRequest)getRequest()).newMultipartWebRequest(Bytes.MAX);
		getRequestCycle().setRequest(webRequest);

		// Get the item for the path
		final FileItem item = ((IMultipartWebRequest)webRequest).getFile("model");
		try
		{
			final InputStream in = item.getInputStream();
			try
			{
				final Object model = new ObjectInputStream(in).readObject();
				System.out.println("Setting model to " + model);
				setAppletModel(model);
				setRedirect(false);
			}
			finally
			{
				in.close();
			}
		}
		catch (ClassNotFoundException e)
		{
			((WebResponse)getResponse()).setHeader("STATUS", "417");
		}
		catch (IOException e)
		{
			((WebResponse)getResponse()).setHeader("STATUS", "417");
		}
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Called when model data is retrieved by the client applet. This method
	 * writes the model retrieved by getAppletModel() to the client as an
	 * application/x-wicket-model binary resource. The HostApplet implementation
	 * on the client then reads the object and updates the sprocket's model
	 * appropriately.
	 * 
	 * @see wicket.IResourceListener#onResourceRequested()
	 */
	public void onResourceRequested()
	{
		new ByteArrayResource("application/x-wicket-model", Objects
				.objectToByteArray(getAppletModel())).onResourceRequested();
	}

	/**
	 * By default, an Applet's model is the Wicket model. In the case of some
	 * Sprockets, the model may be something entirely different that depends on
	 * the communication needs of the individual component.
	 * 
	 * @param model
	 *            The model to set
	 */
	public void setAppletModel(final Object model)
	{
		setModelObject(model);
	}

	/**
	 * @return Height of applet, or -1 to use default provided in html tag
	 */
	protected int getHeight()
	{
		return -1;
	}

	/**
	 * @return Width of applet, or -1 to use default provided in html tag
	 */
	protected int getWidth()
	{
		return -1;
	}

	/**
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		checkComponentTag(tag, "applet");
		tag.put("code", HostApplet.class.getName());
		final String jarName = appletClass.getName() + ".jar";
		final ResourceReference jarResourceReference = new ResourceReference(jarName)
		{
			protected Resource newResource()
			{
				// Create JAR resource
				final JarResourceFactory factory = new JarResourceFactory();
				factory.addClassClosures(classes);
				return factory.getResource();
			}
		};

		final String jarResourceUrl = getRequestCycle().urlFor(jarResourceReference);
		final String codebase = Strings.beforeLastPathComponent(jarResourceUrl, '/') + '/';
		tag.put("codebase", codebase);
		tag.put("archive", jarName);
		tag.put("name", getPageRelativePath().replace(':', '_'));
		final int width = getWidth();
		if (width != -1)
		{
			tag.put("width", width);
		}
		final int height = getHeight();
		if (height != -1)
		{
			tag.put("height", height);
		}
		tag.setType(XmlTag.OPEN);
		super.onComponentTag(tag);
	}

	/**
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		replaceComponentTagBody(markupStream, openTag, "\n<param name=\"getModelUrl\" value=\""
				+ urlFor(IResourceListener.INTERFACE) + "\"/>"
				+ "\n<param name=\"setModelUrl\" value=\"" + urlFor(IFormSubmitListener.INTERFACE)
				+ "\"/>" + "\n<param name=\"appletClassName\" value=\"" + appletClass.getName()
				+ "\"/>\n");
	}

	/**
	 * @param appletClass
	 *            The class to add
	 */
	private void addAppletClass(final Class appletClass)
	{
		// Applet code must implement IAppletCode interface
		if (!IApplet.class.isAssignableFrom(appletClass))
		{
			throw new IllegalArgumentException("Applet class " + appletClass.getName()
					+ " must implement " + IApplet.class.getName());
		}
		this.appletClass = appletClass;
		addClass(appletClass);
		addClass(HostApplet.class);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5431.java