error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11189.java
text:
```scala
R@@atingModel rating = (RatingModel)getDefaultModelObject();

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
package org.apache.wicket.examples.ajax.builtin;

import org.apache.wicket.IClusterable;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


/**
 * Demo page for the rating component.
 * 
 * @author Martijn Dashorst
 */
public class RatingsPage extends BasePage
{
	/**
	 * Star image for no selected star
	 */
	public static final ResourceReference WICKETSTAR0 = new ResourceReference(RatingsPage.class,
			"WicketStar0.png");

	/**
	 * Star image for selected star
	 */
	public static final ResourceReference WICKETSTAR1 = new ResourceReference(RatingsPage.class,
			"WicketStar1.png");

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Link to reset the ratings.
	 */
	private final class ResetRatingLink extends Link
	{
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            component id
		 * @param object
		 *            the model to reset.
		 */
		public ResetRatingLink(String id, IModel object)
		{
			super(id, object);
		}

		/**
		 * @see Link#onClick()
		 */
		public void onClick()
		{
			RatingModel rating = (RatingModel)getModelObject();
			rating.nrOfVotes = 0;
			rating.rating = 0;
			rating.sumOfRatings = 0;
		}
	}

	/**
	 * Rating model for storing the ratings, typically this comes from a database.
	 */
	public static class RatingModel implements IClusterable
	{
		private int nrOfVotes = 0;
		private int sumOfRatings = 0;
		private double rating = 0;

		/**
		 * Returns whether the star should be rendered active.
		 * 
		 * @param star
		 *            the number of the star
		 * @return true when the star is active
		 */
		public boolean isActive(int star)
		{
			return star < ((int)(rating + 0.5));
		}

		/**
		 * Gets the number of cast votes.
		 * 
		 * @return the number of cast votes.
		 */
		public Integer getNrOfVotes()
		{
			return nrOfVotes;
		}

		/**
		 * Adds the vote from the user to the total of votes, and calculates the rating.
		 * 
		 * @param nrOfStars
		 *            the number of stars the user has cast
		 */
		public void addRating(int nrOfStars)
		{
			nrOfVotes++;
			sumOfRatings += nrOfStars;
			rating = sumOfRatings / (1.0 * nrOfVotes);
		}

		/**
		 * Gets the rating.
		 * 
		 * @return the rating
		 */
		public Double getRating()
		{
			return rating;
		}

		/**
		 * Returns the sum of the ratings.
		 * 
		 * @return the sum of the ratings.
		 */
		public int getSumOfRatings()
		{
			return sumOfRatings;
		}
	}

	/**
	 * static models for the ratings, not thread safe, but in this case, we don't care.
	 */
	private static RatingModel rating1 = new RatingModel();

	/**
	 * static model for the ratings, not thread safe, but in this case, we don't care.
	 */
	private static RatingModel rating2 = new RatingModel();

	/**
	 * keeps track whether the user has already voted on this page, comes typically from the
	 * database, or is stored in a cookie on the client side.
	 */
	private Boolean hasVoted = Boolean.FALSE;

	/**
	 * Constructor.
	 */
	public RatingsPage()
	{
		add(new RatingPanel("rating1", new PropertyModel(rating1, "rating"), 5, new PropertyModel(
				rating1, "nrOfVotes"), true)
		{
			protected boolean onIsStarActive(int star)
			{
				return RatingsPage.rating1.isActive(star);
			}

			protected void onRated(int rating, AjaxRequestTarget target)
			{
				RatingsPage.rating1.addRating(rating);
			}
		});
		add(new RatingPanel("rating2", new PropertyModel(rating2, "rating"),
							new Model(5), new PropertyModel(rating2, "nrOfVotes"),
							new PropertyModel(this, "hasVoted"), true)
		{
			protected String getActiveStarUrl(int iteration)
			{
				return getRequestCycle().urlFor(WICKETSTAR1).toString();
			}

			protected String getInactiveStarUrl(int iteration)
			{
				return getRequestCycle().urlFor(WICKETSTAR0).toString();
			}

			protected boolean onIsStarActive(int star)
			{
				return RatingsPage.rating2.isActive(star);
			}

			protected void onRated(int rating, AjaxRequestTarget target)
			{
				// make sure the user can't vote again
				hasVoted = Boolean.TRUE;
				RatingsPage.rating2.addRating(rating);
			}
		});
		add(new ResetRatingLink("reset1", new Model(rating1)));
		add(new ResetRatingLink("reset2", new Model(rating2)));
	}

	/**
	 * Getter for the hasVoted flag.
	 * 
	 * @return <code>true</code> when the user has already voted.
	 */
	public Boolean getHasVoted()
	{
		return hasVoted;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11189.java