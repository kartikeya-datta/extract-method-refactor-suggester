error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10363.java
text:
```scala
a@@ssertTrue("could not find the reference from " + bName

package org.apache.openjpa.persistence.spring;

import java.util.*;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.*;
import org.apache.openjpa.persistence.models.library.*;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class TestLibService extends SingleEMFTestCase 
        implements TransactionalEntityManagerFactory {

    private static EnumSet<AutoDetachType> txScope = 
            EnumSet.allOf(AutoDetachType.class);
    
    private LibService service;
    
    public EntityManager getTransactionalEntityManager() {
        // return a transactionally scoped entity manager
        OpenJPAEntityManager em = emf.createEntityManager();
        em.setAutoDetach(txScope);
        return em;
    }
    
    public void setUp() {
        // declare the library model classes
        super.setUp(DROP_TABLES, Book.class, Borrower.class, Subject.class,
            Volunteer.class);
        
        // put golden data in database
        LibTestingService libTestingService = new LibTestingService();
        libTestingService.setEntityManager(emf.createEntityManager());
        libTestingService.repopulateDB();
        libTestingService.close();
        
        // create the LibService
        service = new LibServiceImpl();
        service.setTransactionalEntityManagerFactory(this);
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        service = null;
    }
        
    /**
     * Using known data, test the LibraryService.findBookByTitle() method and 
     * verify the information returned.
     */
    public void testFindBookByTitle() {
        String title = "Gone Sailing";
        String qTitle = "\"" + title + "\"";
        String bName = "Dick";

        try {
            // find the book
            Book book = service.findBookByTitle(title);
            assertNotNull("could not find the book " + qTitle, book);
            assertEquals("the book found was not the book " + qTitle, title,
                    book.getTitle());

            // get the book's borrower
            Borrower borrower = book.getBorrower();
            assertNotNull("could not find the borrower " + bName, borrower);
            assertEquals("the borrower found was not " + bName, bName, 
                    borrower.getName());

            // get the borrower's volunteer status
            Volunteer volunteer = borrower.getVolunteer();
            assertNotNull("could not find " + bName + "'s volunteer status",
                    volunteer);
            assertNotNull("could not find the reference from " + bName
                    + "'s volunteer status back to " + bName, 
                    volunteer.getBorrower() == borrower);

            // get the book's subjects
            List<Subject> subjects = book.getSubjects();
            assertNotNull("no subjects for the book " + qTitle, subjects);
            assertEquals(
                    "unexpected number of subjects for the book " + qTitle, 2,
                    subjects.size());
        } catch (Exception e) {
            fail("Unable to findBookByTitle");
        }
    }

    /**
     * Using known data, test the LibraryService.findBorrowerByName method and
     * verify the information returned.
     */
    public void testFindBorrowerByName() {
        String bName = "Harry";
        try {
            Borrower harry = service.findBorrowerByName(bName);
            assertNotNull("Could not find " + bName, harry);
            assertEquals("the borrower found is not " + bName, bName, 
                    harry.getName());
        } catch (Exception e) {
            fail("Unable to find borrower by name");
        }
    }
    
    /**
     * Using known data, test the LibraryService.borrowBook() operation.
     * <ul>
     * <li>Can we find Tom, and has he borrowed one book?</li>
     * <li>Can we find the book entitled "Gone Visiting"?</li>
     * <li>After Tom borrows the new book, has he borrowed two books?</li>
     * </ul>
     */
    public void testBorrowBook() {
        String bName = "Tom";
        String title = "Gone Visiting";

        try {
            // find the borrower Tom
            Borrower borrower = service.findBorrowerByName(bName);
            assertNotNull("Could not find " + bName, borrower);
            List<Book> books = borrower.getBooks();
            assertEquals(bName + " has borrowed an unexpected number of books",
                    1, (books == null ? 0 : borrower.getBooks().size()));

            // find the book "Gone Visiting"
            Book book = service.findBookByTitle(title);
            assertNotNull("Could not find the book " + title, book);

            // have Tom borrow the book
            service.borrowBook(borrower, book);
            List<Book> borrowedBooks = borrower.getBooks();
            assertEquals("Unexpected number of books borrowed", 2,
                    borrowedBooks.size());

            // Verify that the update is in the database
            borrower = service.findBorrowerByName(bName);
            assertNotNull("Could not find " + bName, borrower);
            List<Book> booksBorrowed2 = borrower.getBooks();
            assertEquals(bName + " has borrowed an unexpected number of books",
                    2, booksBorrowed2.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to borrow a book");
        }
    }
    
    /**
     * Test the LibraryService.returnBook() operation, etc.
     */
    public void testReturnBook() {
        String bName = "Harry";

        try {
            // find the borrower Harry
            Borrower borrower = service.findBorrowerByName(bName);
            assertNotNull("Could not find " + bName, borrower);
            List<Book> books = borrower.getBooks();
            assertEquals(bName + " has borrowed an unexpected number of books",
                    1, (books == null ? 0 : borrower.getBooks().size()));

            // find the one book Harry has borrowed
            Book book = borrower.getBooks().get(0);
            service.returnBook(book);

            // Verify that the update is in the database
            borrower = service.findBorrowerByName(bName);
            assertNotNull("Could not find " + bName, borrower);
            assertEquals(bName + " has borrowed an unexpected number of books",
                    0, borrower.getBooks().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to return a book");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10363.java