# Hibernate mini-course


Introduction
 - http://docs.jboss.org/hibernate/orm/5.1/
 - Java Persistence with Hibernate 2nd edition
 - https://vladmihalcea.com/
 - entity, ORM, JPA vs Hibernate

Entity Manager, Persistence Context and Entity lifecycle
- first level cache, the same object instance with find
- refresh vs merge
- performance considerations, flushing and clearing
- (Lifecycle.java example) Persistence Context type TRANSACTIONAL vs EXTENDED and application managed entity manager
- how this fits into Candlepin - when we start, end transaction and when we call close method?
- synchronization FLUSH vs AUTO vs (ALWAYS, MANUAL - Hibernate only)- view hibernate as a synchronization framework

http://docs.jboss.org/hibernate/orm/5.1/userguide/html_single/Hibernate_User_Guide.html#pc

Business Equivalence in entities. Implementing equals() hashCode()
 - getReference() or other proxies

JTA vs RESOURCE_LOCAL transaction type
 - JTA bitronix or Application server. JTA TX manager is JNDI bound object that binds a transaction to a local thread
 - Why JTA? When running JMS plus Database, ideally in one transaction. See CandlepinPersistFilter and EventFilter, we do 'best effort transactionality'
 - RESOURCE_LOCAL JDBC tx

Google Guice JPA support
 - guice-persist
 - JpaPersistModule
 - UnitOfWork and JpaPersistService (impl)
 - JpaLocalTxnInterceptor
 - live debug of buildr rspec:consumer_resource_spec:'does not allow a consumer to view entitlements from a different consumer'

Optimistic and Pessimistic locking
 - optimistic locking 
 - pessimistic locking (SELECT FOR UPDATE)
 - SELECT FOR SHARE and deadlock situations
 - force increment locking
 - setting the lock mode during find and query
 - repeatable read vs read committed isolation. Select for update bypasses repeatable read

Relationship mapping 
 - unidirectional, bidirectional
 - join table, join column
 - owning side, inverse side 
 - runtime consistency
 - cascading and unscheduling of deletes

JPQL and JPA Criteria
 - JOINs and JOIN FETCH
 - correlated subselects
 - long path expressions
 - select NEW 

N+1 problem solutions - Batching and other load strategies
 - eager fetch Lazy Fetch TYPE vs FETCH JOIN
 - fetch type vs fetch strategy
 - fetch JOIN, can be specified even for ManyToOne
 - set vs list and multiple bags
 - batch
 - subselect, select, join
 - LAZY is recommended strategy
 
(branch batching) JDBC batching example 

(branch caching) Caching - Second Level Cache
 - configuration
 - statistics
 - entity cache
 - query cache 
 - collection cache

