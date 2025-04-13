error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/577.java
text:
```scala
i@@nfo.getProperty(Attribute.DRDA_SECMEC)) != null)

/*

   Derby - Class org.apache.derby.impl.jdbc.authentication.BasicAuthenticationServiceImpl

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

package org.apache.derby.impl.jdbc.authentication;

import org.apache.derby.iapi.reference.MessageId;
import org.apache.derby.iapi.reference.Attribute;
import org.apache.derby.authentication.UserAuthenticator;
import org.apache.derby.iapi.services.property.PropertyUtil;
import org.apache.derby.iapi.services.daemon.Serviceable;
import org.apache.derby.iapi.services.monitor.ModuleFactory;
import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.services.i18n.MessageService;
import org.apache.derby.iapi.store.access.TransactionController;
import org.apache.derby.iapi.jdbc.AuthenticationService;
import org.apache.derby.iapi.util.StringUtil;

import java.util.Properties;
// security imports - for SHA-1 digest
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.Serializable;
import java.util.Dictionary;

/**
 * This authentication service is the basic Derby user authentication
 * level support.
 *
 * It is activated upon setting derby.authentication.provider database
 * or system property to 'BUILTIN'.
 * <p>
 * It instantiates & calls the basic User authentication scheme at runtime.
 * <p>
 * In 2.0, users can now be defined as database properties.
 * If derby.database.propertiesOnly is set to true, then in this
 * case, only users defined as database properties for the current database
 * will be considered.
 *
 */
public final class BasicAuthenticationServiceImpl
	extends AuthenticationServiceBase implements UserAuthenticator {

	//
	// ModuleControl implementation (overriden)
	//

	/**
	 *  Check if we should activate this authentication service.
	 */
	public boolean canSupport(Properties properties) {

		if (!requireAuthentication(properties))
			return false;

		//
		// We check 2 System/Database properties:
		//
		//
		// - if derby.authentication.provider is set to 'BUILTIN'.
		//
		// and in that case we are the authentication service that should
		// be run.
		//

		String authenticationProvider = PropertyUtil.getPropertyFromSet(
					properties,
					org.apache.derby.iapi.reference.Property.AUTHENTICATION_PROVIDER_PARAMETER);

		if ( (authenticationProvider != null) &&
			 (authenticationProvider.length() != 0) &&
			 (!(StringUtil.SQLEqualsIgnoreCase(authenticationProvider,
				  org.apache.derby.iapi.reference.Property.AUTHENTICATION_PROVIDER_BUILTIN))))
			return false;
		else
			return true;	// Yep, we're on!
	}

	/**
	 * @see org.apache.derby.iapi.services.monitor.ModuleControl#boot
	 * @exception StandardException upon failure to load/boot the expected
	 * authentication service.
	 */
	public void boot(boolean create, Properties properties)
	  throws StandardException {

		// We need authentication
		// setAuthentication(true);

		// we call the super in case there is anything to get initialized.
		super.boot(create, properties);

		// Initialize the MessageDigest class engine here
		// (we don't need to do that ideally, but there is some
		// overhead the first time it is instantiated.
		// SHA-1 is expected in jdk 1.1x and jdk1.2
		// This is a standard name: check,
		// http://java.sun.com/products/jdk/1.{1,2}
		//					/docs/guide/security/CryptoSpec.html#AppA 
		try {
			MessageDigest digestAlgorithm = MessageDigest.getInstance("SHA-1");
			digestAlgorithm.reset();

		} catch (NoSuchAlgorithmException nsae) {
			throw Monitor.exceptionStartingModule(nsae);
		}

		// Set ourselves as being ready and loading the proper
		// authentication scheme for this service
		//
		this.setAuthenticationService(this);
	}

	/*
	** UserAuthenticator methods.
	*/

	/**
	 * Authenticate the passed-in user's credentials.
	 *
	 * @param userName		The user's name used to connect to JBMS system
	 * @param userPassword	The user's password used to connect to JBMS system
	 * @param databaseName	The database which the user wants to connect to.
	 * @param info			Additional jdbc connection info.
	 */
	public boolean	authenticateUser(String userName,
								 String userPassword,
								 String databaseName,
								 Properties info
									)
	{
        // Client security mechanism if any specified
        // Note: Right now it is only used to handle clients authenticating
        // via DRDA SECMEC_USRSSBPWD mechanism
        String clientSecurityMechanism = null;
        // Client security mechanism (if any) short representation
        // Default value is none.
        int secMec = 0;

		// let's check if the user has been defined as a valid user of the
		// JBMS system.
		// We expect to find and match a System property corresponding to the
		// credentials passed-in.
		//
		if (userName == null)
			// We don't tolerate 'guest' user for now.
			return false;

		String definedUserPassword = null, passedUserPassword = null;

        // If a security mechanism is specified as part of the connection
        // properties, it indicates that we've to account as far as how the
        // password is presented to us - in the case of SECMEC_USRSSBPWD
        // (only expected one at the moment), the password is a substitute
        // one which has already been hashed differently than what we store
        // at the database level (for instance) - this will influence how we
        // assess the substitute password to be legitimate for Derby's
        // BUILTIN authentication scheme/provider.
        if ((clientSecurityMechanism =
                info.getProperty(Attribute.CLIENT_SECURITY_MECHANISM)) != null)
        {
            secMec = Integer.parseInt(clientSecurityMechanism);
        }

		//
		// Check if user has been defined at the database or/and
		// system level. The user (administrator) can configure it the
		// way he/she wants (as well as forcing users properties to
		// be retrieved at the datbase level only).
		//
        String userNameProperty =
          org.apache.derby.iapi.reference.Property.USER_PROPERTY_PREFIX.concat(
                        userName);

		// check if user defined at the database level
		definedUserPassword = getDatabaseProperty(userNameProperty);

        if (definedUserPassword != null)
        {
            if (secMec != SECMEC_USRSSBPWD)
            {
                // encrypt passed-in password
                passedUserPassword = encryptPassword(userPassword);
            }
            else
            {
                // Dealing with a client SECMEC - password checking is
                // slightly different and we need to generate a
                // password substitute to compare with the substitute
                // generated one from the client.
                definedUserPassword = substitutePassword(userName,
                                                         definedUserPassword,
                                                         info, true);
                // As SecMec is SECMEC_USRSSBPWD, expected passed-in password
                // to be HexString'ified already
                passedUserPassword = userPassword;
            }
        }
        else
        {
            // check if user defined at the system level
            definedUserPassword = getSystemProperty(userNameProperty);
            passedUserPassword = userPassword;

            if ((definedUserPassword != null) &&
                (secMec == SECMEC_USRSSBPWD))
            {
                // Dealing with a client SECMEC - see above comments
                definedUserPassword = substitutePassword(userName,
                                                         definedUserPassword,
                                                         info, false);
            }
        }

		if (definedUserPassword == null)
			// no such user found
			return false;

		// check if the passwords match
		if (!definedUserPassword.equals(passedUserPassword))
			return false;

		// NOTE: We do not look at the passed-in database name value as
		// we rely on the authorization service that was put in
		// in 2.0 . (if a database name was passed-in)

		// We do have a valid user
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/577.java