error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10753.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10753.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10753.java
text:
```scala
public E@@ntityGraph<?> getEntityGraph(String s) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.jpa.container;

import static org.jboss.as.jpa.messages.JpaLogger.ROOT_LOGGER;
import static org.jboss.as.jpa.messages.JpaMessages.MESSAGES;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.SynchronizationType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

/**
 * Abstract entity manager used by all container managed entity managers.
 *
 * @author Scott Marlow (forked from jboss-jpa)
 */
public abstract class AbstractEntityManager implements EntityManager {
    private final transient boolean isTraceEnabled = ROOT_LOGGER.isTraceEnabled();

    protected abstract EntityManager getEntityManager();

    /**
     * @return true if an extended persistence context is in use
     *         <p/>
     *         Precondition: getEntityManager() must be called previous to calling isExtendedPersistenceContext
     */
    protected abstract boolean isExtendedPersistenceContext();

    /**
     * @return true if a JTA transaction is active
     *         <p/>
     *         Precondition: getEntityManager() must be called previous to calling isInTx
     */
    protected abstract boolean isInTx();

    public abstract SynchronizationType getSynchronizationType();

    public <T> T unwrap(Class<T> cls) {
        return getEntityManager().unwrap(cls);
    }

    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachTypedQueryNonTxInvocation(entityManager,entityManager.createNamedQuery(name, resultClass));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNamedQuery name '%s', resultClass '%s' took %dms", name, resultClass.getName(), elapsed);
            }
        }
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachTypedQueryNonTxInvocation(entityManager,entityManager.createQuery(criteriaQuery));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createQuery took %dms", elapsed);
            }
        }
    }

    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachTypedQueryNonTxInvocation(entityManager,entityManager.createQuery(qlString, resultClass));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createQuery resultClass '%s' took %dms", resultClass.getName(), elapsed);
            }
        }
    }

    public void detach(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().detach(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("detach entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            final EntityManager underlyingEntityManager = getEntityManager();
            T result = underlyingEntityManager.find(entityClass, primaryKey, properties);
            detachNonTxInvocation(underlyingEntityManager);
            return result;
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("find entityClass '%s' took %dms", entityClass.getName(), elapsed);
            }
        }
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            final EntityManager underlyingEntityManager = getEntityManager();
            T result = underlyingEntityManager.find(entityClass, primaryKey, lockMode);
            detachNonTxInvocation(underlyingEntityManager);
            return result;
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("find entityClass '%s', lockMode '%s' took %dms", entityClass.getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            final EntityManager underlyingEntityManager = getEntityManager();
            T result = underlyingEntityManager.find(entityClass, primaryKey, lockMode, properties);
            detachNonTxInvocation(underlyingEntityManager);
            return result;
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("find entityClass '%s', lockMode '%s' took %dms", entityClass.getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }

    public <T> T find(Class<T> entityClass, Object primaryKey) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            final EntityManager underlyingEntityManager = getEntityManager();
            T result = getEntityManager().find(entityClass, primaryKey);
            detachNonTxInvocation(underlyingEntityManager);
            return result;
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("find entityClass '%s' took %dms", entityClass.getName(), elapsed);
            }
        }
    }


    public CriteriaBuilder getCriteriaBuilder() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getCriteriaBuilder();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getCriteriaBuilder took %dms", elapsed);
            }
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getEntityManagerFactory();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getEntityManagerFactory took %dms", elapsed);
            }
        }
    }

    public LockModeType getLockMode(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        LockModeType result = null;
        try {
            result = getEntityManager().getLockMode(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getLockMode entityClass '%s', lockMode '%s'  took %dms", entity.getClass().getName(), getLockModeAsString(result), elapsed);
            }
        }
        return result;
    }

    public Metamodel getMetamodel() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getMetamodel();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getMetamodel took %dms", elapsed);
            }
        }
    }

    public Map<String, Object> getProperties() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getProperties();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getProperties took %dms", elapsed);
            }
        }
    }

    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().lock(entity, lockMode, properties);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("lock entityClass '%s', lockMode '%s'  took %dms", entity.getClass().getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }


    public void setProperty(String propertyName, Object value) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().setProperty(propertyName, value);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("setProperty took %dms", elapsed);
            }
        }
    }

    public void clear() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().clear();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("clear took %dms", elapsed);
            }
        }
    }

    public void close() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().close();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("close took %dms", elapsed);
            }
        }
    }

    public boolean contains(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().contains(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("contains '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public Query createNamedQuery(String name) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachQueryNonTxInvocation(entityManager, entityManager.createNamedQuery(name));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNamedQuery name '%s' took %dms", name, elapsed);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Query createNativeQuery(String sqlString, Class resultClass) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachQueryNonTxInvocation(entityManager, entityManager.createNativeQuery(sqlString, resultClass));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNativeQuery resultClass '%s' took %dms", resultClass.getName(), elapsed);
            }
        }
    }

    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachQueryNonTxInvocation(entityManager, entityManager.createNativeQuery(sqlString, resultSetMapping));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNativeQuery took %dms", elapsed);
            }
        }
    }

    public Query createNativeQuery(String sqlString) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachQueryNonTxInvocation(entityManager, entityManager.createNativeQuery(sqlString));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNativeQuery took %dms", elapsed);
            }
        }
    }

    public Query createQuery(String ejbqlString) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            // invoke underlying entity manager method and if not running in a tx
            // return a Query wrapper around the result.
            EntityManager entityManager = getEntityManager();
            return detachQueryNonTxInvocation(entityManager, entityManager.createQuery(ejbqlString));
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createQuery took %dms", elapsed);
            }
        }
    }

    public void flush() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().flush();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("flush took %dms", elapsed);
            }
        }
    }

    public Object getDelegate() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getDelegate();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getDelegate took %dms", elapsed);
            }
        }
    }

    public FlushModeType getFlushMode() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getFlushMode();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getFlushMode took %dms", elapsed);
            }
        }
    }

    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            final EntityManager underlyingEntityManager = getEntityManager();
            T result = getEntityManager().getReference(entityClass, primaryKey);
            detachNonTxInvocation(underlyingEntityManager);
            return result;
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getReference entityClass '%s' took %dms", entityClass.getName(), elapsed);
            }
        }
    }

    public EntityTransaction getTransaction() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getTransaction();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getTransaction took %dms", elapsed);
            }
        }
    }

    public boolean isOpen() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        return getEntityManager().isOpen();
    }

    public void joinTransaction() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().joinTransaction();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("joinTransaction took %dms", elapsed);
            }
        }
    }

    public void lock(Object entity, LockModeType lockMode) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().lock(entity, lockMode);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("lock entityClass '%s', lockMode '%s' took %dms", entity.getClass().getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }

    public <T> T merge(T entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            return getEntityManager().merge(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("merge entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public void persist(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().persist(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("persist entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public void refresh(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().refresh(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("refresh entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public void refresh(Object entity, Map<String, Object> properties) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().refresh(entity, properties);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("refresh entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public void refresh(Object entity, LockModeType lockMode) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().refresh(entity, lockMode);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("refresh entityClass '%s', lockMode '%s' took %dms", entity.getClass().getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }

    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().refresh(entity, lockMode, properties);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("refresh entityClass '%s', lockMode '%s' took %dms", entity.getClass().getName(), getLockModeAsString(lockMode), elapsed);
            }
        }
    }

    public void remove(Object entity) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            transactionIsRequired();
            getEntityManager().remove(entity);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("remove entityClass '%s' took %dms", entity.getClass().getName(), elapsed);
            }
        }
    }

    public void setFlushMode(FlushModeType flushMode) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            getEntityManager().setFlushMode(flushMode);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("setFlushMode took %dms", elapsed);
            }
        }
    }

    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createQuery(criteriaUpdate);

        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createQuery(CriteriaUpdate) took %dms", elapsed);
            }
        }
    }

    public Query createQuery(CriteriaDelete criteriaDelete) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createQuery(criteriaDelete);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createQuery(criteriaDelete) took %dms", elapsed);
            }
        }
    }

    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createNamedStoredProcedureQuery(name);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createNamedStoredProcedureQuery %s took %dms", name, elapsed);
            }
        }
    }

    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createStoredProcedureQuery(procedureName);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createStoredProcedureQuery %s took %dms", procedureName, elapsed);
            }
        }
    }

    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createStoredProcedureQuery(procedureName, resultClasses);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createStoredProcedureQuery %s, resultClasses... took %dms", procedureName, elapsed);
            }
        }
    }

    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createStoredProcedureQuery(procedureName, resultSetMappings);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createStoredProcedureQuery %s, resultSetMappings... took %dms", procedureName, elapsed);
            }
        }
    }

    public <T> EntityGraph<T> createEntityGraph(Class<T> tClass) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createEntityGraph(tClass);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createEntityGraph %s took %dms", tClass.getName(), elapsed);
            }
        }

    }

    public EntityGraph<?> createEntityGraph(String s) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().createEntityGraph(s);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("createEntityGraph %s took %dms", s, elapsed);
            }
        }
    }

    public <T> EntityGraph<T> getEntityGraph(String s) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getEntityGraph(s);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getEntityGraph %s took %dms", s, elapsed);
            }
        }
    }

    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> tClass) {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().getEntityGraphs(tClass);
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("getEntityGraphs %s took %dms", tClass.getName(), elapsed);
            }
        }
    }

    public boolean isJoinedToTransaction() {
        long start = 0;
        if (isTraceEnabled)
            start = System.currentTimeMillis();
        try {
            return getEntityManager().isJoinedToTransaction();
        } finally {
            if (isTraceEnabled) {
                long elapsed = System.currentTimeMillis() - start;
                ROOT_LOGGER.tracef("isJoinedToTransaction() took %dms", elapsed);
            }
        }
    }


    // used by TransactionScopedEntityManager to auto detach loaded entities
    // after each non-jta invocation
    protected void detachNonTxInvocation(EntityManager underlyingEntityManager) {
        if (!this.isExtendedPersistenceContext() && !this.isInTx()) {
            underlyingEntityManager.clear();
        }
    }

    // for JPA 2.0 section 3.8.6
    // used by TransactionScopedEntityManager to detach entities loaded by a query in a non-jta invocation.
    protected Query detachQueryNonTxInvocation(EntityManager underlyingEntityManager, Query underLyingQuery) {
        if (!this.isExtendedPersistenceContext() && !this.isInTx()) {
            return new QueryNonTxInvocationDetacher(underlyingEntityManager, underLyingQuery);
        }
        return underLyingQuery;
    }

    // for JPA 2.0 section 3.8.6
    // used by TransactionScopedEntityManager to detach entities loaded by a query in a non-jta invocation.
    protected TypedQuery detachTypedQueryNonTxInvocation(EntityManager underlyingEntityManager, TypedQuery underLyingQuery) {
        if (!this.isExtendedPersistenceContext() && !this.isInTx()) {
            return new TypedQueryNonTxInvocationDetacher(underlyingEntityManager, underLyingQuery);
        }
        return underLyingQuery;
    }


    // JPA 7.9.1 if invoked without a JTA transaction and a transaction scoped persistence context is used,
    // will throw TransactionRequiredException for any calls to entity manager remove/merge/persist/refresh.
    private void transactionIsRequired() {
        if (!this.isExtendedPersistenceContext() && !this.isInTx()) {
            throw MESSAGES.transactionRequired();
        }
    }

    private static String getLockModeAsString(LockModeType lockMode) {
        if (lockMode == null)
            return "(null)";
        switch (lockMode) {
            case OPTIMISTIC:
                return "optimistic";
            case OPTIMISTIC_FORCE_INCREMENT:
                return "optimistic_force_increment";
            case READ:
                return "read";
            case WRITE:
                return "write";
            case PESSIMISTIC_READ:
                return "pessimistic_read";
            case PESSIMISTIC_FORCE_INCREMENT:
                return "pessimistic_force_increment";
            default:
            case NONE:
                return "none";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10753.java