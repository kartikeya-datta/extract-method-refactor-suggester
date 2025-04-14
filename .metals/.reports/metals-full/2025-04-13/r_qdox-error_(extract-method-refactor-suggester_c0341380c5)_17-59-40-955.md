error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5576.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5576.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5576.java
text:
```scala
F@@ilterCriteria criteria = rule.get(i);

package org.columba.mail.folder;

import java.util.Vector;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterCriteria;
import org.columba.mail.filter.FilterRule;
import org.columba.mail.folder.imap.IMAPFolder;
import org.columba.mail.folder.imap.IMAPRootFolder;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RemoteSearchEngine implements SearchEngineInterface {

	//protected IMAPProtocol imap;

	protected Folder folder;
	protected IMAPRootFolder rootFolder;

	public RemoteSearchEngine(Folder folder) {
		this.folder = folder;

		rootFolder = (IMAPRootFolder) ((IMAPFolder) folder).getRootFolder();
		//imap = rootFolder.getImapServerConnection();

	}

	protected String createSubjectString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("SUBJECT ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createToString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("TO ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createCcString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("CC ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createBccString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("BCC ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createFromString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("FROM ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createToCCString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");
	
		searchString.append("OR ");

		searchString.append("TO ");

		searchString.append(criteria.getPattern());
		
		searchString.append("CC ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createBodyString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("BODY ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createSizeString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		if (criteria.getCriteria() == FilterCriteria.SIZE_BIGGER)
			searchString.append("LARGER ");
		else
			searchString.append("SMALLER ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createDateString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		if (criteria.getCriteria() == FilterCriteria.DATE_BEFORE)
			searchString.append("SENTBEFORE ");
		else
			searchString.append("SENTAFTER ");

		searchString.append(criteria.getPattern());

		return searchString.toString();
	}

	protected String createFlagsString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		String headerField = criteria.getPattern();

		if (headerField.equalsIgnoreCase("Answered")) {
			searchString.append("ANSWERED ");
		} else if (headerField.equalsIgnoreCase("Deleted")) {
			searchString.append("DELETED ");
		} else if (headerField.equalsIgnoreCase("Flagged")) {
			searchString.append("FLAGGED ");
		} else if (headerField.equalsIgnoreCase("Recent")) {
			searchString.append("NEW ");
		} else if (headerField.equalsIgnoreCase("Draft")) {
			searchString.append("DRAFT ");
		} else if (headerField.equalsIgnoreCase("Seen")) {
			searchString.append("SEEN ");
		}

		return searchString.toString();
	}

	protected String createPriorityString(FilterCriteria criteria) {
		StringBuffer searchString = new StringBuffer();

		// we need to append "NOT"
		if (criteria.getCriteria() == FilterCriteria.CONTAINS_NOT)
			searchString.append("NOT ");

		searchString.append("X-Priority ");

		Integer searchPattern = null;
		String pattern = criteria.getPattern();
		if (pattern.equalsIgnoreCase("Highest")) {
			searchPattern = new Integer(1);
		} else if (pattern.equalsIgnoreCase("High")) {
			searchPattern = new Integer(2);
		} else if (pattern.equalsIgnoreCase("Normal")) {
			searchPattern = new Integer(3);
		} else if (pattern.equalsIgnoreCase("Low")) {
			searchPattern = new Integer(4);
		} else if (pattern.equalsIgnoreCase("Lowest")) {
			searchPattern = new Integer(5);
		}
		searchString.append(searchPattern.toString());

		return searchString.toString();
	}

	protected String generateSearchString(
		FilterRule rule,
		Vector ruleStringList) {
		StringBuffer searchString = new StringBuffer();

		if (rule.count() > 1) {

			int condition = rule.getConditionInt();
			String conditionString;
			if (condition == FilterRule.MATCH_ALL) {
				// match all
				conditionString = "OR";

			} else {
				// match any
				conditionString = "AND";
			}

			// concatenate all criteria together
			//  -> create one search-request string
			for (int i = 0; i < rule.count(); i++) {

				if (i != rule.count() - 1)
					searchString.append(conditionString + " ");

				searchString.append((String) ruleStringList.get(i));

				if (i != rule.count() - 1)
					searchString.append(" ");

			}
		} else {
			searchString.append((String) ruleStringList.get(0));
		}

		return searchString.toString();
	}
	
	protected String generateSearchString( Filter filter )
	{
		FilterRule rule = filter.getFilterRule();

		Vector ruleStringList = new Vector();

		for (int i = 0; i < rule.count(); i++) {
			FilterCriteria criteria = rule.getCriteria(i);
			String headerItem;
			//StringBuffer searchString = new StringBuffer();
			String searchString = null;

			switch (criteria.getHeaderItem()) {
				case FilterCriteria.SUBJECT :
					{
						searchString = createSubjectString(criteria);

						break;
					}
				case FilterCriteria.TO :
					{
						searchString = createToString(criteria);
						break;
					}
				case FilterCriteria.FROM :
					{
						searchString = createFromString(criteria);
						break;
					}
				case FilterCriteria.CC :
					{
						searchString = createCcString(criteria);
						break;
					}
				case FilterCriteria.BCC :
					{
						searchString = createBccString(criteria);
						break;
					}
				case FilterCriteria.TO_CC :
					{
						searchString = createToString(criteria);

						break;
					}
				case FilterCriteria.BODY :
					{
						searchString = createBodyString(criteria);
						break;
					}
				case FilterCriteria.SIZE :
					{
						searchString = createSizeString(criteria);

						break;
					}
				case FilterCriteria.DATE :
					{
						searchString = createDateString(criteria);

						break;
					}
				case FilterCriteria.FLAGS :
					{
						searchString = createFlagsString(criteria);

						break;
					}
				case FilterCriteria.PRIORITY :
					{
						searchString = createPriorityString(criteria);

						break;
					}

			}
			ruleStringList.add(searchString.toString());
		}

		String searchString = generateSearchString(rule, ruleStringList);

		/*
		if (searchString.length() == 0)
			searchString =
				"1:* OR HEADER SUBJECT test OR HEADER FROM fdietz@gmx.de HEADER FROM freddy@uni-mannheim.de";
		*/
		
		
		ColumbaLogger.log.info("searchString=" + searchString.toString());
		
		return searchString;
	}
	
	public Object[] searchMessages(
		Filter filter,
		WorkerStatusController worker)
		throws Exception
		{
			return ((IMAPFolder) folder)
			.getStore()
			.search(generateSearchString( filter) , ((IMAPFolder) folder).getImapPath(), worker)
			.toArray();
		}

	public Object[] searchMessages(
		Filter filter,
		Object[] uids,
		WorkerStatusController worker)
		throws Exception {
		

		return ((IMAPFolder) folder)
			.getStore()
			.search(uids, generateSearchString( filter), ((IMAPFolder) folder).getImapPath(), worker)
			.toArray();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5576.java