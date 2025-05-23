error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3165.java
text:
```scala
i@@nvalidate(); // force getting the resource on each request;

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
package wicket.examples.cdapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.IFeedback;
import wicket.contrib.data.model.PersistentObjectModel;
import wicket.contrib.data.model.hibernate.HibernateObjectModel;
import wicket.contrib.markup.html.image.resource.ThumbnailImageResource;
import wicket.examples.cdapp.model.CD;
import wicket.examples.cdapp.util.HibernateSessionDelegate;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.upload.FileUpload;
import wicket.markup.html.form.upload.FileUploadField;
import wicket.markup.html.form.upload.UploadForm;
import wicket.markup.html.form.validation.IntegerValidator;
import wicket.markup.html.form.validation.LengthValidator;
import wicket.markup.html.image.Image;
import wicket.markup.html.image.resource.DynamicImageResource;
import wicket.markup.html.image.resource.ImageResource;
import wicket.markup.html.image.resource.StaticImageResource;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import wicket.util.resource.IResource;


/**
 * Page for editing CD's.
 * 
 * @author Eelco Hillenius
 */
public final class EditPage extends CdAppBasePage
{
	/** Logger. */
	private static Log log = LogFactory.getLog(SearchPage.class);

	/** static image resource from this package; references image 'questionmark.gif'. */
	private static final StaticImageResource IMG_UNKNOWN = StaticImageResource.get(
			EditPage.class.getPackage(), "questionmark.gif", null, null);

	/** model for one cd. */
	private final PersistentObjectModel cdModel;

	/** search page to navigate back to. */
	private final SearchPage searchCDPage;

	/**
	 * form for detail editing.
	 */
	private final class DetailForm extends Form
	{
		/**
		 * Construct.
		 * 
		 * @param name component name
		 * @param validationErrorHandler error handler
		 * @param cdModel the model
		 */
		public DetailForm(String name, IFeedback validationErrorHandler,
				PersistentObjectModel cdModel)
		{
			super(name, cdModel, validationErrorHandler);
			RequiredTextField titleField = new RequiredTextField("title", new PropertyModel(cdModel, "title"));
			titleField.add(LengthValidator.max(50));
			add(titleField);
			RequiredTextField performersField = new RequiredTextField("performers", new PropertyModel(cdModel, "performers"));
			performersField.add(LengthValidator.max(50));
			add(performersField);
			TextField labelField = new TextField("label", new PropertyModel(cdModel, "label"));
			labelField.add(LengthValidator.max(50));
			add(labelField);
			RequiredTextField yearField = new RequiredTextField("year", new PropertyModel(cdModel, "year"));
			yearField.add(IntegerValidator.POSITIVE_INT);
			add(yearField);
			add(new Link("cancelButton")
			{
				public void onClick()
				{
					getRequestCycle().setResponsePage(searchCDPage);
				}
			});
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public void onSubmit()
		{
			CD cd = (CD)getModelObject();
			boolean isNew = (cd.getId() == null);
			// note that, as we used the Ognl property model, the fields are
			// allready updated
			getCdDao().save(cd);
			// set message for search page to display on next rendering
			searchCDPage.setInfoMessageForNextRendering("cd " + cd.getTitle() + " saved");

			if (isNew)
			{
				// if it was a new cd, set the search page to page 1
				searchCDPage.setCurrentResultPageToFirst();
			}
			setResponsePage(searchCDPage); // navigate back to search page
		}
	}

	/**
	 * Form for uploading an image and attaching that image to the cd.
	 */
	private final class ImageUploadForm extends UploadForm
	{
		private FileUploadField uploadField;
		
		/**
		 * Construct.
		 * @param name
		 * @param cdModel 
		 */
		public ImageUploadForm(String name, PersistentObjectModel cdModel)
		{
			super(name, cdModel, null);
			add(uploadField = new FileUploadField("file"));
		}

		protected void onSubmit()
		{
			// get the uploaded file
			FileUpload upload = uploadField.getFileUpload();
			CD cd = (CD)getModelObject();
			cd.setImage(upload.getBytes());
			getCdDao().save(cd);
		}
	}

	/**
	 * Deletes the cd image.
	 */
	private final class DeleteImageLink extends Link
	{
		/**
		 * Construct.
		 * @param name
		 * @param cdModel
		 */
		public DeleteImageLink(String name, IModel cdModel)
		{
			super(name, cdModel);
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			CD cd = (CD)getModelObject();
			cd.setImage(null);
			getCdDao().save(cd);
		}

		/**
		 * @see wicket.Component#isVisible()
		 */
		public boolean isVisible()
		{
			// only set visible when there is an image set
			return ((CD)getModelObject()).getImage() != null;
		}
	}

	/**
	 * Constructor.
	 * @param searchCDPage the search page to navigate back to
	 * @param id the id of the cd to edit
	 */
	public EditPage(final SearchPage searchCDPage, Long id)
	{
		super();
		cdModel = new HibernateObjectModel(id, CD.class, new HibernateSessionDelegate());
		this.searchCDPage = searchCDPage;
		add(new Label("cdTitle", new TitleModel(cdModel)));
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new DetailForm("detailForm", feedback, cdModel));
		add(new ImageUploadForm("imageUpload", cdModel));

		// create an image resource that displays a question mark when no image is
		// set on the cd, or displays a thumbnail of the cd's image when there is one
		ImageResource thumbImgResource = new ImageResource()
		{
			public IResource getResource()
			{
				final CD cd = (CD)cdModel.getObject(null);
				if (cd.getImage() == null)
				{
					return IMG_UNKNOWN.getResource();
				}
				else
				{
					DynamicImageResource img = new DynamicImageResource()
					{
						protected byte[] getImageData()
						{
							return cd.getImage();
						}
					};
					ThumbnailImageResource res =
						new ThumbnailImageResource(img, 100);
					return res.getResource();
				}
			}

			public String getPath()
			{
				reset(); // force getting the resource on each request;
				return super.getPath();
			}
		};

		// create a link that displays the full image in a popup page
		ImagePopupLink popupImageLink = new ImagePopupLink("popupImageLink", cdModel);

		// create an image using the image resource
		popupImageLink.add(new Image("cdimage", thumbImgResource));

		// add the link to the original image
		add(popupImageLink);

		// add link for deleting the image
		add(new DeleteImageLink("deleteImageLink", cdModel));
	}

	/**
	 * @see wicket.Component#initModel()
	 */
	protected IModel initModel()
	{
		return cdModel;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3165.java