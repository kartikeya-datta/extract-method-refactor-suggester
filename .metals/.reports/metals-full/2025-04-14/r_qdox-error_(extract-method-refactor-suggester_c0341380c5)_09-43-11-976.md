error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2379.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2379.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2379.java
text:
```scala
d@@d.pushDataDictionaryContext(cm);

/*

   Derby - Class org.apache.derby.impl.sql.conn.GenericLanguageConnectionFactory

   Copyright 1997, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.sql.conn;

import org.apache.derby.iapi.reference.JDBC20Translation;
import org.apache.derby.iapi.reference.JDBC30Translation;

import org.apache.derby.iapi.sql.conn.LanguageConnectionFactory;
import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;
import org.apache.derby.iapi.sql.compile.CompilerContext;

import org.apache.derby.iapi.sql.LanguageFactory;
import org.apache.derby.impl.sql.GenericStatement;

import org.apache.derby.impl.sql.conn.CachedStatement;

import org.apache.derby.iapi.services.uuid.UUIDFactory;
import org.apache.derby.iapi.services.compiler.JavaFactory;
import org.apache.derby.iapi.services.loader.ClassFactory;

import org.apache.derby.iapi.db.Database;

import org.apache.derby.iapi.store.access.TransactionController;

import org.apache.derby.iapi.sql.compile.TypeCompilerFactory;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.sql.compile.NodeFactory;
import org.apache.derby.iapi.sql.compile.Parser;

import org.apache.derby.iapi.sql.Activation;

import org.apache.derby.iapi.store.access.AccessFactory;
import org.apache.derby.iapi.services.property.PropertyFactory;

import org.apache.derby.iapi.sql.Statement;
import org.apache.derby.iapi.sql.compile.OptimizerFactory;
import org.apache.derby.iapi.sql.dictionary.DataDictionary;
import org.apache.derby.iapi.types.DataValueFactory;
import org.apache.derby.iapi.sql.execute.ExecutionFactory;
import org.apache.derby.iapi.sql.dictionary.SchemaDescriptor;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.iapi.services.monitor.ModuleControl;
import org.apache.derby.iapi.services.monitor.ModuleSupportable;
import org.apache.derby.iapi.services.monitor.ModuleFactory;
import org.apache.derby.iapi.services.context.ContextManager;

import org.apache.derby.iapi.services.cache.CacheFactory;
import org.apache.derby.iapi.services.cache.CacheManager;
import org.apache.derby.iapi.services.cache.CacheableFactory;
import org.apache.derby.iapi.services.cache.Cacheable;

import org.apache.derby.iapi.services.property.PropertyUtil;
import org.apache.derby.iapi.services.property.PropertySetCallback;

import org.apache.derby.iapi.services.i18n.LocaleFinder;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.reference.Property;
import org.apache.derby.iapi.reference.EngineType;

import java.util.Properties;
import java.util.Locale;
import java.util.Dictionary;
import java.io.Serializable;
import org.apache.derby.iapi.util.IdUtil;
import org.apache.derby.iapi.services.daemon.Serviceable;
import org.apache.derby.iapi.util.StringUtil;

/**
 * LanguageConnectionFactory generates all of the items
 * a language system needs that is specific to a particular
 * connection. Alot of these are other factories.
 *
 * @author ames
 */
public class GenericLanguageConnectionFactory
	implements LanguageConnectionFactory, CacheableFactory, PropertySetCallback, ModuleControl, ModuleSupportable {

	/*
		fields
	 */
	protected 	DataDictionary	dd;
	private 	ExecutionFactory		ef;
	private 	OptimizerFactory		of;
	private		TypeCompilerFactory		tcf;
	private 	DataValueFactory		dvf;
	private 	UUIDFactory				uuidFactory;
	private 	JavaFactory				javaFactory;
	private 	ClassFactory			classFactory;
	private 	NodeFactory				nodeFactory;
	private 	AccessFactory			af;
	private 	PropertyFactory			pf;

	private		int						nextLCCInstanceNumber;

	/*
	  for caching prepared statements 
	*/
	private int cacheSize = org.apache.derby.iapi.reference.Property.STATEMENT_CACHE_SIZE_DEFAULT;
	private CacheManager singleStatementCache;

	/*
	   constructor
	*/
	public GenericLanguageConnectionFactory() {
	}

	/*
	   LanguageConnectionFactory interface
	*/

	/*
		these are the methods that do real work, not just look for factories
	 */

	/**
		Get a Statement for the connection
		@param compilationSchema schema
		@param statementText the text for the statement
		@param forReadOnly if concurrency is CONCUR_READ_ONLY
		@return	The Statement
	 */
        public Statement getStatement(SchemaDescriptor compilationSchema, String statementText, boolean forReadOnly)
        {
	    return new GenericStatement(compilationSchema, statementText, forReadOnly);
	}


	/**
		Get a LanguageConnectionContext. this holds things
		we want to remember about activity in the language system,
		where this factory holds things that are pretty stable,
		like other factories.
		<p>
		The returned LanguageConnectionContext is intended for use
		only by the connection that requested it.

		@return a language connection context for the context stack.
		@exception StandardException the usual -- for the subclass
	 */

	public LanguageConnectionContext newLanguageConnectionContext(
		ContextManager cm,
		TransactionController tc,
		LanguageFactory lf,
		Database db,
		String userName,
		String drdaID,
		String dbname) throws StandardException {
		
		pushDataDictionaryContext(cm);

		return new GenericLanguageConnectionContext(cm,
													tc,
													lf,
													this,
													db,
													userName,
													getNextLCCInstanceNumber(),
													drdaID,
													dbname);
	}

	public Cacheable newCacheable(CacheManager cm) {
		return new CachedStatement();
	}

	/*
		these methods all look for factories that we booted.
	 */
	 
	 /**
		Get the UUIDFactory to use with this language connection
		REMIND: this is only used by the compiler; should there be
		a compiler module control class to boot compiler-only stuff?
	 */
	public UUIDFactory	getUUIDFactory()
	{
		return uuidFactory;
	}

	/**
		Get the ClassFactory to use with this language connection
	 */
	public ClassFactory	getClassFactory()
	{
		return classFactory;
	}

	/**
		Get the JavaFactory to use with this language connection
		REMIND: this is only used by the compiler; should there be
		a compiler module control class to boot compiler-only stuff?
	 */
	public JavaFactory	getJavaFactory()
	{
		return javaFactory;
	}

	/**
		Get the NodeFactory to use with this language connection
		REMIND: is this only used by the compiler?
	 */
	public NodeFactory	getNodeFactory()
	{
		return nodeFactory;
	}

	/**
		Get the ExecutionFactory to use with this language connection
	 */
	public ExecutionFactory	getExecutionFactory() {
		return ef;
	}

	/**
		Get the AccessFactory to use with this language connection
	 */
	public AccessFactory	getAccessFactory() 
	{
		return af;
	}	

	/**
		Get the PropertyFactory to use with this language connection
	 */
	public PropertyFactory	getPropertyFactory() 
	{
		return pf;
	}	

	/**
		Get the OptimizerFactory to use with this language connection
	 */
	public OptimizerFactory	getOptimizerFactory() {
		return of;
	}
	/**
		Get the TypeCompilerFactory to use with this language connection
	 */
	public TypeCompilerFactory getTypeCompilerFactory() {
		return tcf;
	}

	/**
		Get the DataValueFactory to use with this language connection
	 */
	public DataValueFactory		getDataValueFactory() {
		return dvf;
	}

	protected void pushDataDictionaryContext(ContextManager cm) {
		// we make sure there is a data dictionary context in place.
		dd.pushDataDictionaryContext(cm, false);
	}

	/*
		ModuleControl interface
	 */

	/**
		this implementation will not support caching of statements.
	 */
	public boolean canSupport(Properties startParams) {

		return Monitor.isDesiredType( startParams, EngineType.STANDALONE_DB);
	}

	private	int	statementCacheSize(Properties startParams)
	{
		String wantCacheProperty = null;

		wantCacheProperty =
			PropertyUtil.getPropertyFromSet(startParams, org.apache.derby.iapi.reference.Property.STATEMENT_CACHE_SIZE);

		if (SanityManager.DEBUG)
			SanityManager.DEBUG("StatementCacheInfo", "Cacheing implementation chosen if null or 0<"+wantCacheProperty);

		if (wantCacheProperty != null) {
			try {
			    cacheSize = Integer.parseInt(wantCacheProperty);
			} catch (NumberFormatException nfe) {
				cacheSize = org.apache.derby.iapi.reference.Property.STATEMENT_CACHE_SIZE_DEFAULT; 
			}
		}

		return cacheSize;
	}
	
	/**
	 * Start-up method for this instance of the language connection factory.
	 * Note these are expected to be booted relative to a Database.
	 *
	 * @param startParams	The start-up parameters (ignored in this case)
	 *
	 * @exception StandardException	Thrown on failure to boot
	 */
	public void boot(boolean create, Properties startParams) 
		throws StandardException {

		dvf = (DataValueFactory) Monitor.bootServiceModule(create, this, org.apache.derby.iapi.reference.ClassName.DataValueFactory, startParams);
		javaFactory = (JavaFactory) Monitor.startSystemModule(org.apache.derby.iapi.reference.Module.JavaFactory);
		uuidFactory = Monitor.getMonitor().getUUIDFactory();
		classFactory = (ClassFactory) Monitor.getServiceModule(this, org.apache.derby.iapi.reference.Module.ClassFactory);
		if (classFactory == null)
 			classFactory = (ClassFactory) Monitor.findSystemModule(org.apache.derby.iapi.reference.Module.ClassFactory);

		bootDataDictionary(create, startParams);

		//set the property validation module needed to do propertySetCallBack
		//register and property validation
		setValidation();

		setStore();

		ef = (ExecutionFactory) Monitor.bootServiceModule(create, this, ExecutionFactory.MODULE, startParams);
		of = (OptimizerFactory) Monitor.bootServiceModule(create, this, OptimizerFactory.MODULE, startParams);
		tcf =
		   (TypeCompilerFactory) Monitor.startSystemModule(TypeCompilerFactory.MODULE);
		nodeFactory = (NodeFactory) Monitor.bootServiceModule(create, this, NodeFactory.MODULE, startParams);

		// If the system supports statement caching boot the CacheFactory module.
		int cacheSize = statementCacheSize(startParams);
		if (cacheSize > 0) {
			CacheFactory cacheFactory = (CacheFactory) Monitor.startSystemModule(org.apache.derby.iapi.reference.Module.CacheFactory);
			singleStatementCache = cacheFactory.newCacheManager(this,
												"StatementCache",
												cacheSize/4,
												cacheSize);
		}

	}

	protected void bootDataDictionary(boolean create, Properties startParams) throws StandardException {
		dd = (DataDictionary) Monitor.bootServiceModule(create, this, DataDictionary.MODULE, startParams);
	}

	/**
	 * returns the statement cache that this connection should use; currently
	 * there is a statement cache per connection.
	 */
	

	public CacheManager getStatementCache()
	{
		return singleStatementCache;
	}

	/**
	 * Stop this module.  In this case, nothing needs to be done.
	 */
	public void stop() {
	}

	/*
	** Methods of PropertySetCallback
	*/

	public void init(boolean dbOnly, Dictionary p) {
		// not called yet ...
	}

	/**
	  @see PropertySetCallback#validate
	  @exception StandardException Thrown on error.
	*/
	public boolean validate(String key,
						 Serializable value,
						 Dictionary p)
		throws StandardException {
		if (value == null)
			return true;
		else if (key.equals(Property.DEFAULT_CONNECTION_MODE_PROPERTY))
		{
			String value_s = (String)value;
			if (value_s != null &&
				!StringUtil.SQLEqualsIgnoreCase(value_s, Property.NO_ACCESS) &&
				!StringUtil.SQLEqualsIgnoreCase(value_s, Property.READ_ONLY_ACCESS) &&
				!StringUtil.SQLEqualsIgnoreCase(value_s, Property.FULL_ACCESS) &&
				!StringUtil.SQLEqualsIgnoreCase(value_s, Property.SQL_STANDARD_ACCESS))
				throw StandardException.newException(SQLState.AUTH_INVALID_AUTHORIZATION_PROPERTY, key, value_s);

			return true;
		}
		else if (key.equals(Property.READ_ONLY_ACCESS_USERS_PROPERTY) ||
				 key.equals(Property.FULL_ACCESS_USERS_PROPERTY))
		{
			String value_s = (String)value;

			/** Parse the new userIdList to verify its syntax. */
			String[] newList_a;
			try {newList_a = IdUtil.parseIdList(value_s);}
			catch (StandardException se) {
                throw StandardException.newException(SQLState.AUTH_INVALID_AUTHORIZATION_PROPERTY, se, key,value_s);
			}

			/** Check the new list userIdList for duplicates. */
			String dups = IdUtil.dups(newList_a);
			if (dups != null) throw StandardException.newException(SQLState.AUTH_DUPLICATE_USERS, key,dups);

			/** Check for users with both read and full access permission. */
			String[] otherList_a;
			String otherList;
			if (key.equals(Property.READ_ONLY_ACCESS_USERS_PROPERTY))
				otherList = (String)p.get(Property.FULL_ACCESS_USERS_PROPERTY);
			else
				otherList = (String)p.get(Property.READ_ONLY_ACCESS_USERS_PROPERTY);
			otherList_a = IdUtil.parseIdList(otherList);
			String both = IdUtil.intersect(newList_a,otherList_a);
			if (both != null) throw StandardException.newException(SQLState.AUTH_USER_IN_READ_AND_WRITE_LISTS, both);
			
			return true;
		}

		return false;
	}
	/** @see PropertySetCallback#apply */
	public Serviceable apply(String key,
							 Serializable value,
							 Dictionary p)
	{
			 return null;
	}
	/** @see PropertySetCallback#map */
	public Serializable map(String key, Serializable value, Dictionary p)
	{
		return null;
	}

	protected void setValidation() throws StandardException {
		pf = (PropertyFactory) Monitor.findServiceModule(this,
			org.apache.derby.iapi.reference.Module.PropertyFactory);
		pf.addPropertySetNotification(this);
	}

	protected void setStore() throws StandardException {
		af = (AccessFactory) Monitor.findServiceModule(this,AccessFactory.MODULE);
	}

    public Parser newParser(CompilerContext cc)
    {
        return new org.apache.derby.impl.sql.compile.ParserImpl(cc);
    }

	// Class methods

	/**
	 * Get the instance # for the next LCC.
	 * (Useful for logStatementText=true output.
	 *
	 * @return instance # of next LCC.
	 */
	protected synchronized int getNextLCCInstanceNumber()
	{
		return nextLCCInstanceNumber++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2379.java