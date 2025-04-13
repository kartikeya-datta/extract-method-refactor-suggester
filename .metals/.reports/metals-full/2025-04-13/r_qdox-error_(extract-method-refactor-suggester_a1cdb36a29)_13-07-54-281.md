error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5948.java
text:
```scala
H@@OSTNAME_REGEXP = "^[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+\\.[a-zA-Z]{2,4}+$",

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.mail.config;

import java.util.regex.Pattern;

import org.columba.core.config.DefaultItem;
import org.columba.core.xml.XmlElement;

import org.columba.mail.main.MailInterface;

import org.columba.ristretto.parser.ParserException;

public class AccountItem extends DefaultItem {
  
  	private static final String 
  		HOSTNAME_REGEXP = "^[a-zA-Z0-9]+\\.[a-zA-Z0-9\\.]+\\.[a-zA-Z]{2,4}+$",
  		DOTTED_IPV4_REGEXP = 
  		  "^[1-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[1-9]{1,3}+$";
  	
  	private static final Pattern 	
  		hostnamePattern = Pattern.compile(HOSTNAME_REGEXP),
  		dottedIpv4Pattern = Pattern.compile(DOTTED_IPV4_REGEXP);														
  	
  	
  	/*
  	 * Add supported account formats here
  	 * */
  	public static final int POP3_ACCOUNT = 0,
  													IMAP_ACCOUNT = 1,
  													UNKNOWN_TYPE = -1;
  	
    private AccountItem defaultAccount;
    //private boolean pop3;
    private Identity identity;
    private PopItem pop;
    private ImapItem imap;
    private SmtpItem smtp;
    private PGPItem pgp;
    private SpecialFoldersItem folder;
    private SpamItem spam;
    
		private int accountType = UNKNOWN_TYPE;
		
    public AccountItem(XmlElement e) {
        super(e);

        if (e.getElement("/popserver") != null)
        {
	        accountType = POP3_ACCOUNT;
        }
        else if (e.getElement("/imapserver") != null)
        {
        	accountType = IMAP_ACCOUNT;  
        }
        else
          accountType = UNKNOWN_TYPE;
        
    }

    /*
     * validates a hostname, i.e.:
     * mail.myhost.com
     * mail.us.myhost.com
     * 127.0.0.1
     * */
		public static boolean validHostname(String hostname)
		{
		  return (dottedIpv4Pattern.matcher(hostname).matches() ||
		      		hostnamePattern.matcher(hostname).matches());
		}
    
    public final String getAccountTypeDescription()
    {
    	switch(accountType)
    	{
    		case POP3_ACCOUNT:
    		  return "POP3";
    		case IMAP_ACCOUNT:
    		  return "IMAP";
    		default:
    		  return "UNKNOWN";
    	}
    }
    
    public final int getAccountType()
    {
    	return accountType;  
    }
    
    public boolean isPopAccount()
    {
      return (accountType == POP3_ACCOUNT);
    }

    public SpecialFoldersItem getSpecialFoldersItem() {
        if (folder == null) {
            folder = new SpecialFoldersItem(getRoot().getElement("specialfolders"));
        }

        if (folder.getBoolean("use_default_account")) {
            // return default-account ImapItem instead 
            SpecialFoldersItem item = MailInterface.config.getAccountList()
                                                          .getDefaultAccount()
                                                          .getSpecialFoldersItem();

            return item;
        }

        return folder;
    }

    private AccountItem getDefaultAccount() {
        if (defaultAccount == null) {
            defaultAccount = MailInterface.config.getAccountList()
                                                 .getDefaultAccount();
        }

        return defaultAccount;
    }

    public PopItem getPopItem() {
        if (pop == null) {
            pop = new PopItem(getRoot().getElement("popserver"));
        }

        if (pop.getBoolean("use_default_account")) {
            // return default-account ImapItem instead 
            PopItem item = MailInterface.config.getAccountList()
                                               .getDefaultAccount().getPopItem();

            return item;
        }

        return pop;
    }

    public SmtpItem getSmtpItem() {
        if (smtp == null) {
            smtp = new SmtpItem(getRoot().getElement("smtpserver"));
        }

        if (smtp.getBoolean("use_default_account")) {
            // return default-account ImapItem instead 
            return getDefaultAccount().getSmtpItem();
        }

        return smtp;
    }
    
    public SpamItem getSpamItem() {
        if ( spam == null) {
            XmlElement parent = getRoot().getElement("spam");
            // create if not available
            if ( parent == null) parent = getRoot().addSubElement("spam");
            
            spam = new SpamItem(parent);
        }
        
        if (spam.getBoolean("use_default_account")) {
            // return default-account SpamItem instead 
            return getDefaultAccount().getSpamItem();
        }
        
        return spam;
    }

    public PGPItem getPGPItem() {
        if (pgp == null) {
            pgp = new PGPItem(getRoot().getElement("pgp"));
        }

        if (pgp.getBoolean("use_default_account")) {
            // return default-account ImapItem instead 
            PGPItem item = MailInterface.config.getAccountList()
                                               .getDefaultAccount().getPGPItem();

            return item;
        }

        return pgp;
    }

    public ImapItem getImapItem() {
        if (imap == null) {
            imap = new ImapItem(getRoot().getElement("imapserver"));
        }

        if (imap.getBoolean("use_default_account")) {
            // return default-account ImapItem instead 
            ImapItem item = MailInterface.config.getAccountList()
                                                .getDefaultAccount()
                                                .getImapItem();

            return item;
        }

        return imap;
    }

    /**
     * Returns the identity used with this account.
     */
    public Identity getIdentity() {
        if (identity == null) {
            XmlElement e = getRoot().getElement("identity");
            if (Boolean.valueOf(e.getAttribute("use_default_account", "false"))
                    .booleanValue()) {
                // return default-account identityItem instead
                return MailInterface.config.getAccountList().getDefaultAccount()
                        .getIdentity();
            } else {
                try {
                    identity = new Identity(e);
                } catch (ParserException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return identity;
    }

    public void setName(String str) {
        set("name", str);
    }

    public String getName() {
        return get("name");
    }

    public void setUid(int i) {
        set("uid", i);
    }

    public int getUid() {
        return getInteger("uid");
    }

    public boolean isDefault() {
        if (MailInterface.config.getAccountList().getDefaultAccountUid() == getUid()) {
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // same object
        }

        if ((obj == null) || !(obj instanceof AccountItem)) {
            return false;
        }

        /*
         * The fields on this object is in fact represented in the xml
         * structure found as getRoot(). Therefore super.equals()
         * should do the job
         */
        return super.equals(obj);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        /*
         * The fields on this object is in fact represented in the xml
         * structure found as getRoot(). Therefore super.hashCode()
         * should do the job.
         */
        return super.hashCode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5948.java