error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3134.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3134.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3134.java
text:
```scala
S@@tring distingushedName = groupRefValues.next().replace("\\", "\\\\").replace("/", "\\/");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.domain.management.security;

import static org.jboss.as.domain.management.DomainManagementLogger.SECURITY_LOGGER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.jboss.as.domain.management.security.BaseLdapGroupSearchResource.GroupName;

/**
 * Factory for supplying LDAP searches for groups.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class LdapGroupSearcherFactory {

    private static final int searchTimeLimit = 10000;

    static LdapSearcher<LdapEntry[], LdapEntry> createForGroupToPrincipal(final String baseDn, final String groupDnAttribute,
            final String groupNameAttribute, final String principalAttribute, final boolean recursive,
            final GroupName searchBy) {
        return new GroupToPrincipalSearcher(baseDn, groupDnAttribute, groupNameAttribute, principalAttribute, recursive, searchBy);
    }

    static LdapSearcher<LdapEntry[], LdapEntry> createForPrincipalToGroup(final String groupAttribute, final String groupNameAttribute) {
        return new PrincipalToGroupSearcher(groupAttribute, groupNameAttribute);
    }

    private static SearchControls createSearchControl(final boolean recursive, final String[] attributes) {
        if (SECURITY_LOGGER.isTraceEnabled()) {
            SECURITY_LOGGER.tracef("createSearchControl recursive=%b,  attributes=%s", recursive, Arrays.toString(attributes));
        }
        // 2 - Search to identify the DN of the user connecting
        SearchControls searchControls = new SearchControls();
        if (recursive) {
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        } else {
            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        }
        searchControls.setReturningAttributes(attributes);
        searchControls.setTimeLimit(searchTimeLimit);
        return searchControls;
    }

    private static String[] createArray(final String... elements) {
        ArrayList<String> elementList = new ArrayList<String>(elements.length);
        for (String current : elements) {
            if (current != null) {
                elementList.add(current);
            }
        }

        return elementList.toArray(new String[elementList.size()]);
    }

    private static class GroupToPrincipalSearcher implements LdapSearcher<LdapEntry[], LdapEntry> {

        private final String baseDn;
        private final String groupDnAttribute;
        private final String groupNameAttribute;
        private final String[] attributeArray;
        private final String filterString;
        private final boolean recursive;
        private final GroupName searchBy;

        private GroupToPrincipalSearcher(final String baseDn, final String groupDnAttribute, final String groupNameAttribute,
                final String principalAttribute, final boolean recursive, final GroupName searchBy) {
            this.baseDn = baseDn;
            this.groupDnAttribute = groupDnAttribute;
            this.groupNameAttribute = groupNameAttribute;
            this.attributeArray = createArray(groupDnAttribute, groupNameAttribute);
            this.filterString = String.format("(%s={0})", principalAttribute);
            this.recursive = recursive;
            this.searchBy = searchBy;

            if (SECURITY_LOGGER.isTraceEnabled()) {
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher baseDn=%s", baseDn);
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher groupDnAttribute=%s", groupDnAttribute);
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher groupNameAttribute=%s", groupNameAttribute);
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher attributeArray=%s", Arrays.toString(attributeArray));
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher filterString=%s", filterString);
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher recursive=%b", recursive);
                SECURITY_LOGGER.tracef("GroupToPrincipalSearcher searchBy=%s", searchBy);
            }
        }

        @Override
        public LdapEntry[] search(DirContext dirContext, LdapEntry entry) throws IOException, NamingException {
            SearchControls searchControls = createSearchControl(recursive, attributeArray); // TODO - Can we create this in
                                                                                            // advance?
            Set<LdapEntry> foundEntries = new HashSet<LdapEntry>();
            Object[] searchParameter = getSearchParameter(entry);
            boolean trace = SECURITY_LOGGER.isTraceEnabled();
            if (trace) {
                SECURITY_LOGGER.tracef("Performing search baseDn=%s, filterString=%s, searchParameter=%s", baseDn,
                        filterString, Arrays.toString(searchParameter));
            }
            NamingEnumeration<SearchResult> searchResults = dirContext.search(baseDn, filterString, searchParameter, searchControls);
            if (trace && searchResults.hasMore() == false) {
                SECURITY_LOGGER.trace("No search results found.");
            }
            while (searchResults.hasMore()) {
                SearchResult current = searchResults.next();
                Attributes attributes = current.getAttributes();
                if (attributes != null) {
                    LdapEntry newEntry = convertToLdapEntry(current, attributes);
                    SECURITY_LOGGER.tracef("Adding %s", newEntry);
                    foundEntries.add(newEntry);
                } else {
                    SECURITY_LOGGER.tracef("No attributes found for %s", current);
                }
            }

            return foundEntries.toArray(new LdapEntry[foundEntries.size()]);
        }

        private LdapEntry convertToLdapEntry(SearchResult searchResult, Attributes attributes) throws NamingException {
            String simpleName = null;
            String distinguishedName = null;

            if (groupNameAttribute != null) {
                SECURITY_LOGGER.tracef("Getting groupNameAttribute=%s", groupNameAttribute);
                Attribute groupNameAttr = attributes.get(groupNameAttribute);
                if (groupNameAttr != null) {
                    simpleName = (String) groupNameAttr.get();
                }
            }

            if (groupDnAttribute != null) {
                if ("dn".equals(groupDnAttribute)) {
                    SECURITY_LOGGER.trace("Obtaining dn using getNameInNamespace()");
                    distinguishedName = searchResult.getNameInNamespace();
                } else {
                    SECURITY_LOGGER.tracef("Getting groupDnAttribute=%s", groupDnAttribute);
                    Attribute groupDnAttr = attributes.get(groupDnAttribute);
                    if (groupDnAttr != null) {
                        distinguishedName = (String) groupDnAttr.get();
                    }
                }
            }

            return new LdapEntry(simpleName, distinguishedName);
        }

        private Object[] getSearchParameter(final LdapEntry entry) {
            switch (searchBy) {
                case SIMPLE:
                    return new String[] { entry.getSimpleName() };
                default:
                    return new String[] { entry.getDistinguishedName() };
            }
        }

    }

    private static class PrincipalToGroupSearcher implements LdapSearcher<LdapEntry[], LdapEntry> {

        private final String groupAttribute; // The attribute on the principal that references the group it is a member of.
        private final String groupNameAttribute; // The attribute on the group that is it's simple name.

        private PrincipalToGroupSearcher(final String groupAttribute, final String groupNameAttribute) {
            this.groupAttribute = groupAttribute;
            this.groupNameAttribute = groupNameAttribute;

            if (SECURITY_LOGGER.isTraceEnabled()) {
                SECURITY_LOGGER.tracef("PrincipalToGroupSearcher groupAttribute=%s", groupAttribute);
                SECURITY_LOGGER.tracef("PrincipalToGroupSearcher groupNameAttribute=%s", groupNameAttribute);
            }
        }

        @Override
        public LdapEntry[] search(DirContext dirContext, LdapEntry entry) throws IOException, NamingException {
            Set<LdapEntry> foundEntries = new HashSet<LdapEntry>();
            // Load the list of group.
            Attributes groups = dirContext.getAttributes(entry.getDistinguishedName(), new String[] { groupAttribute });
            Attribute groupRef = groups.get(groupAttribute);
            if (groupRef != null && groupRef.size() > 0) {
                NamingEnumeration<String> groupRefValues = (NamingEnumeration<String>) groupRef.getAll();
                while (groupRefValues.hasMore()) {
                    String distingushedName = groupRefValues.next();
                    SECURITY_LOGGER.tracef("Group found with distinguishedName=%s", distingushedName);
                    String simpleName = null;
                    if (groupNameAttribute != null) {
                        // Load the Name
                        Attributes groupNameAttrs = dirContext.getAttributes(distingushedName, new String[] { groupNameAttribute });
                        Attribute groupNameAttr = groupNameAttrs.get(groupNameAttribute);
                        simpleName = (String) groupNameAttr.get();
                        SECURITY_LOGGER.tracef("simpleName %s loaded for group with distinguishedName=%s", simpleName,
                                distingushedName);
                    } else {
                        SECURITY_LOGGER.trace("No groupNameAttribute to load simpleName");
                    }
                    foundEntries.add(new LdapEntry(simpleName, distingushedName));
                }
            } else {
                SECURITY_LOGGER.tracef("No groups found for %s", entry);
            }

            return foundEntries.toArray(new LdapEntry[foundEntries.size()]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3134.java