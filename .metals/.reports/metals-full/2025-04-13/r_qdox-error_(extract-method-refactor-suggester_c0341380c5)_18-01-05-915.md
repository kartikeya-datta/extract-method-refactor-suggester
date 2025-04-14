error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3292.java
text:
```scala
l@@inkWithLabel.setBody(Model.of("A link that provides its body with Link.setBody(someModel)"));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.examples.linkomatic;

import org.apache.wicket.Component;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ClientSideImageMap;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.parser.filter.RelativePathPrefixHandler;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;


/**
 * Demonstrates different flavors of hyperlinks.
 * 
 * @author Jonathan Locke
 */
public class Home extends WicketExamplePage
{
	/** click count for Link. */
	private int linkClickCount = 0;

	/** click count for OnClickLink. */
	private int onClickLinkClickCount = 0;

	/**
	 * Constructor
	 */
	public Home()
	{
		// Action link counts link clicks
		final Link actionLink = new Link("actionLink")
		{
			@Override
			public void onClick()
			{
				linkClickCount++;
			}
		};
		actionLink.add(new Label("linkClickCount", new PropertyModel<Integer>(this,
			"linkClickCount")));
		add(actionLink);

		// Action link counts link clicks on works with onclick handler
		final Link actionOnClickLink = new Link("actionOnClickLink")
		{
			@Override
			public void onClick()
			{
				onClickLinkClickCount++;
			}
		};

		add(actionOnClickLink);
		add(new Label("onClickLinkClickCount", new PropertyModel<Integer>(this,
			"onClickLinkClickCount")));

		// Link to Page1 is a simple external page link
		add(new BookmarkablePageLink<Void>("page1Link", Page1.class));

		// Link to Page2 is automaticLink, so no code
		// Link to Page3 is an external link which takes a parameter
		add(new BookmarkablePageLink<Void>("page3Link", Page3.class).setParameter(
			"bookmarkparameter", "3++2 & 5 � >< space + �"));

		// Link to BookDetails page
		add(new Link<Void>("bookDetailsLink")
		{
			@Override
			public void onClick()
			{
				setResponsePage(new BookDetails(new Book("The Hobbit")));
			}
		});

		// Delayed link to BookDetails page
		add(new Link<Void>("bookDetailsLink2")
		{
			@Override
			public void onClick()
			{
				setResponsePage(new BookDetails(new Book("Inside The Matrix")));
			}
		});

		// Image map link example
		Image imageForMap = new Image("imageForMap", new PackageResourceReference(Home.class,
			"ImageMap.gif"));
		add(imageForMap);
		add(new ClientSideImageMap("imageMap", imageForMap).addRectangleArea(
			new BookmarkablePageLink<Page1>("page1", Page1.class), 0, 0, 100, 100)
			.addCircleArea(new BookmarkablePageLink<Page2>("page2", Page2.class), 160, 50, 35)
			.addPolygonArea(new BookmarkablePageLink<Page3>("page3", Page3.class), 212, 79, 241, 4,
				279, 54, 212, 79)
			.add(RelativePathPrefixHandler.RELATIVE_PATH_BEHAVIOR));

		// Popup example
		PopupSettings popupSettings = new PopupSettings("popuppagemap").setHeight(500)
			.setWidth(500);
		add(new BookmarkablePageLink<Void>("popupLink", Popup.class).setPopupSettings(popupSettings));

		// Popup example
		add(new BookmarkablePageLink<Void>("popupButtonLink", Popup.class).setPopupSettings(popupSettings));

		// External site link
		add(new ExternalLink("google", "http://www.google.com", "Click this link to go to Google"));

		// And that link as a popup
		PopupSettings googlePopupSettings = new PopupSettings(PopupSettings.RESIZABLE |
			PopupSettings.SCROLLBARS).setHeight(500).setWidth(700);
		add(new ExternalLink("googlePopup", "http://www.google.com",
			"Click this link to go to Google in a popup").setPopupSettings(googlePopupSettings));

		// Shared resource link
		add(new ResourceLink("cancelButtonLink", new SharedResourceReference("cancelButton")));

		// redirect to external url form
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);
		add(new RedirectForm("redirectForm"));

		Link linkToAnchor = new Link("linkToAnchor")
		{
			@Override
			public void onClick()
			{
			}
		};
		add(linkToAnchor);
		Link anotherlinkToAnchor = new Link("anotherlinkToAnchor")
		{
			@Override
			public void onClick()
			{
			}
		};
		add(anotherlinkToAnchor);
		Component anchorLabel = new Label("anchorLabel",
			"this label is here to function as an anchor for a link").setOutputMarkupId(true);
		add(anchorLabel);
		linkToAnchor.setAnchor(anchorLabel);

		Link<Void> linkWithLabel = new Link<Void>("linkWithLabel")
		{

			@Override
			public void onClick()
			{
			}
		};
		linkWithLabel.setBody(Model.of("A link that provides its body with Link.setBodyLabel(someModel)"));
		add(linkWithLabel);
	}

	/**
	 * Form that handles a redirect.
	 */
	private final class RedirectForm extends Form<RedirectForm>
	{
		/** receives form input. */
		private String redirectUrl = "http://www.theserverside.com";

		/**
		 * Construct.
		 * 
		 * @param id
		 *            component id
		 */
		public RedirectForm(String id)
		{
			super(id);
			setDefaultModel(new CompoundPropertyModel<RedirectForm>(this));
			add(new TextField<String>("redirectUrl"));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		protected void onSubmit()
		{
			setResponsePage(new RedirectPage(redirectUrl));
		}

		/**
		 * Gets the redirectUrl.
		 * 
		 * @return redirectUrl
		 */
		public String getRedirectUrl()
		{
			return redirectUrl;
		}

		/**
		 * Sets the redirectUrl.
		 * 
		 * @param redirectUrl
		 *            redirectUrl
		 */
		public void setRedirectUrl(String redirectUrl)
		{
			this.redirectUrl = redirectUrl;
		}
	}

	/**
	 * @return Returns the linkClickCount.
	 */
	public int getLinkClickCount()
	{
		return linkClickCount;
	}

	/**
	 * @param linkClickCount
	 *            The linkClickCount to set.
	 */
	public void setLinkClickCount(final int linkClickCount)
	{
		this.linkClickCount = linkClickCount;
	}

	/**
	 * Gets onClickLinkClickCount.
	 * 
	 * @return onClickLinkClickCount
	 */
	public int getOnClickLinkClickCount()
	{
		return onClickLinkClickCount;
	}

	/**
	 * Sets onClickLinkClickCount.
	 * 
	 * @param onClickLinkClickCount
	 *            onClickLinkClickCount
	 */
	public void setOnClickLinkClickCount(int onClickLinkClickCount)
	{
		this.onClickLinkClickCount = onClickLinkClickCount;
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3292.java