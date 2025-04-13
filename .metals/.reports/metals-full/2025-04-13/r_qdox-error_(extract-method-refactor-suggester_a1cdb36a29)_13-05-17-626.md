error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/885.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/885.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/885.java
text:
```scala
t@@rue,				// nullability

/*

   Derby - Class org.apache.derby.impl.sql.catalog.SYSVIEWSRowFactory

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.apache.derby.impl.sql.catalog;

import org.apache.derby.iapi.types.DataValueDescriptor;

import org.apache.derby.iapi.types.TypeId;
import org.apache.derby.iapi.sql.dictionary.SystemColumn;
import org.apache.derby.catalog.TypeDescriptor;

import org.apache.derby.iapi.types.DataValueFactory;
import org.apache.derby.iapi.types.RowLocation;

import org.apache.derby.iapi.sql.dictionary.CatalogRowFactory;
import org.apache.derby.iapi.sql.dictionary.ConglomerateDescriptor;
import org.apache.derby.iapi.sql.dictionary.DataDescriptorGenerator;
import org.apache.derby.iapi.sql.dictionary.DataDictionary;
import org.apache.derby.iapi.sql.dictionary.DataDictionaryContext;
import org.apache.derby.iapi.sql.dictionary.TupleDescriptor;
import org.apache.derby.iapi.sql.dictionary.ViewDescriptor;

import org.apache.derby.iapi.sql.execute.ExecutionContext;
import org.apache.derby.iapi.sql.execute.ExecIndexRow;
import org.apache.derby.iapi.sql.execute.ExecRow;
import org.apache.derby.iapi.sql.execute.ExecutionFactory;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.services.uuid.UUIDFactory;
import org.apache.derby.catalog.UUID;
import org.apache.derby.catalog.IndexDescriptor;

import java.util.Properties;

/**
 * Factory for creating a SYSVIEWS row.
 *
 * @author jerry
 */

public class SYSVIEWSRowFactory extends CatalogRowFactory
{
	private  static final String	TABLENAME_STRING = "SYSVIEWS";

	protected static final int		SYSVIEWS_COLUMN_COUNT = 4;
	protected static final int		SYSVIEWS_TABLEID = 1;
	protected static final int		SYSVIEWS_VIEWDEFINITION = 2;
	protected static final int		SYSVIEWS_CHECKOPTION = 3;
	protected static final int		SYSVIEWS_COMPILATION_SCHEMAID = 4;

	// Column widths
	protected static final int		SYSVIEWS_TABLEID_WIDTH = 36;

	protected static final int		SYSVIEWS_INDEX1_ID = 0;

	private static final int[][] indexColumnPositions =
	{
		{SYSVIEWS_TABLEID}
	};


	// if you add a non-unique index allocate this array.
    private	static	final	boolean[]	uniqueness = null;

	private	static	final	String[]	uuids =
	{
		 "8000004d-00d0-fd77-3ed8-000a0a0b1900"	// catalog UUID
		,"80000050-00d0-fd77-3ed8-000a0a0b1900"	// heap UUID
		,"8000004f-00d0-fd77-3ed8-000a0a0b1900"	// SYSVIEWS_INDEX1
	};

	/////////////////////////////////////////////////////////////////////////////
	//
	//	CONSTRUCTORS
	//
	/////////////////////////////////////////////////////////////////////////////

    public	SYSVIEWSRowFactory(UUIDFactory uuidf, ExecutionFactory ef, DataValueFactory dvf,
                               boolean convertIdToLower)
	{
		super(uuidf,ef,dvf,convertIdToLower);
		initInfo(SYSVIEWS_COLUMN_COUNT, TABLENAME_STRING, 
				 indexColumnPositions, uniqueness, uuids );
	}

	/////////////////////////////////////////////////////////////////////////////
	//
	//	METHODS
	//
	/////////////////////////////////////////////////////////////////////////////

  /**
	 * Make a SYSVIEWS row
	 *
	 * @return	Row suitable for inserting into SYSVIEWS.
	 *
	 * @exception   StandardException thrown on failure
	 */
	public ExecRow makeRow(TupleDescriptor td, TupleDescriptor parent)
		throws StandardException 
	{
		DataValueDescriptor		col;
		ExecRow    				row;
		String					tableID = null;
		String					compSchemaId = null;
		String					viewText = null;
		String	   				checkSType = null;
		int	   					checkIType;

		if (td != null)
		{
			UUID	tableUUID;
			ViewDescriptor vd = (ViewDescriptor)td;

			/*
			** We only allocate a new UUID if the descriptor doesn't already have one.
			** For descriptors replicated from a Source system, we already have an UUID.
			*/
			tableUUID = vd.getUUID();
			if ( tableUUID == null )
		    {
				tableUUID = getUUIDFactory().createUUID();
				vd.setUUID(tableUUID);
			}
			tableID = tableUUID.toString();
			viewText = vd.getViewText();

			/* RESOLVE - check constraints not supported yet */
			checkIType = vd.getCheckOptionType();

			if (SanityManager.DEBUG)
			{
				if (checkIType != ViewDescriptor.NO_CHECK_OPTION)
				{
					SanityManager.THROWASSERT("checkIType expected to be " + 
						ViewDescriptor.NO_CHECK_OPTION +
						", not " + checkIType);
				}
			}
			checkSType = "N";

			UUID tmpId = vd.getCompSchemaId();
			compSchemaId = (tmpId == null) ? null : tmpId.toString();
		}

		/* Insert info into sysviews */

		/* RESOLVE - It would be nice to require less knowledge about sysviews
		 * and have this be more table driven.
		 */

		/* Build the row to insert  */
		row = getExecutionFactory().getValueRow(SYSVIEWS_COLUMN_COUNT);

		/* 1st column is TABLEID (UUID - char(36)) */
		row.setColumn(SYSVIEWS_TABLEID, dvf.getCharDataValue(tableID));

		/* 2nd column is VIEWDEFINITION */
		row.setColumn(SYSVIEWS_VIEWDEFINITION,
				dvf.getLongvarcharDataValue(viewText));

		/* 3rd column is CHECKOPTION (char(1)) */
		row.setColumn(SYSVIEWS_CHECKOPTION, dvf.getCharDataValue(checkSType));

		/* 4th column is COMPILATIONSCHEMAID (UUID - char(36)) */
		row.setColumn(SYSVIEWS_COMPILATION_SCHEMAID, dvf.getCharDataValue(compSchemaId));

		return row;
	}
    
	///////////////////////////////////////////////////////////////////////////
	//
	//	ABSTRACT METHODS TO BE IMPLEMENTED BY CHILDREN OF CatalogRowFactory
	//
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Make a ViewDescriptor out of a SYSVIEWS row
	 *
	 * @param row a SYSVIEWS row
	 * @param parentTupleDescriptor	Null for this kind of descriptor.
	 * @param dd dataDictionary
	 *
	 * @exception   StandardException thrown on failure
	 */
	public TupleDescriptor buildDescriptor(
		ExecRow					row,
		TupleDescriptor			parentTupleDescriptor,
		DataDictionary 			dd )
					throws StandardException
	{
		ViewDescriptor vd = null;

		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(
				row.nColumns() == SYSVIEWS_COLUMN_COUNT, 
				"Wrong number of columns for a SYSVIEWS row");
		}

		DataValueDescriptor	col;
		DataDescriptorGenerator ddg;
		int					checkIType;
		String				checkSType;
		String				tableID;
		String				compSchemaId;
		String				viewDefinition;
		UUID				tableUUID;
		UUID				compSchemaUUID = null;

		ddg = dd.getDataDescriptorGenerator();

		/* 1st column is TABLEID (UUID - char(36)) */
		col = row.getColumn(SYSVIEWS_TABLEID);
		tableID = col.getString();
		tableUUID = getUUIDFactory().recreateUUID(tableID);

		/* 2nd column is VIEWDEFINITION */
		col = row.getColumn(SYSVIEWS_VIEWDEFINITION);
		viewDefinition = col.getString();

		/* 3rd column is CHECKOPTION (char(1)) */
		col = row.getColumn(SYSVIEWS_CHECKOPTION);
		checkSType = col.getString();

		if (SanityManager.DEBUG)
		{
			if (!checkSType.equals("N"))
			{
				SanityManager.THROWASSERT("checkSType expected to be 'N', not " + checkSType);
			}
		}

		/* RESOLVE - no check options for now */
		checkIType = ViewDescriptor.NO_CHECK_OPTION;

		/* 4th column is COMPILATIONSCHEMAID (UUID - char(36)) */
		col = row.getColumn(SYSVIEWS_COMPILATION_SCHEMAID);
		compSchemaId = col.getString();
		if (compSchemaId != null)
		{
			compSchemaUUID = getUUIDFactory().recreateUUID(compSchemaId);
		}

		/* now build and return the descriptor */
		vd = ddg.newViewDescriptor(tableUUID, null, viewDefinition, 
				checkIType, compSchemaUUID);
		return vd;
	}

	/**
	 * Builds a list of columns suitable for creating this Catalog.
	 *
	 *
	 * @return array of SystemColumn suitable for making this catalog.
	 */
	public SystemColumn[]	buildColumnList()
	{
		SystemColumn[]			columnList = new SystemColumn[SYSVIEWS_COLUMN_COUNT];

		columnList[0] = new SystemColumnImpl(	
							convertIdCase( "TABLEID"),			// name 
							SYSVIEWS_TABLEID,	// column number
							0,					// precision
							0,					// scale
							false,				// nullability
							"CHAR",				// dataType
							true,				// built-in type
							36					// maxLength
			                );
		// describe columns

		columnList[1] = 
					new SystemColumnImpl(	
							convertIdCase( "VIEWDEFINITION"),		// column name
							SYSVIEWS_VIEWDEFINITION,	// column number
							0,					// precision
							0,					// scale
							false,				// nullability
							"LONG VARCHAR",	    // dataType
							true,				// built-in type
							TypeId.LONGVARCHAR_MAXWIDTH // maxLength
			               );
		columnList[2] = 
					new SystemColumnImpl(	
							convertIdCase( "CHECKOPTION"),		// column name
							SYSVIEWS_CHECKOPTION,// column number
							0,					// precision
							0,					// scale
							false,				// nullability
							"CHAR",				// dataType
							true,				// built-in type
							1					// maxLength
		                   );

		columnList[3] = new SystemColumnImpl(	
							convertIdCase( "COMPILATIONSCHEMAID"),	// name 
							SYSVIEWS_COMPILATION_SCHEMAID,	// column number
							0,					// precision
							0,					// scale
							false,				// nullability
							"CHAR",				// dataType
							true,				// built-in type
							36					// maxLength
			                );
		return	columnList;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/885.java