# hibernate

http://docs.jboss.org/hibernate/orm/5.1/

Introduction
 - entity, ORM, JPA vs Hibernate
 - Java Persistence with Hibernate 2nd edition

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
 - JTA bitronix or Application server 
 - why JTA, JMS plus Database, see CandlepinPersistFilter and EventFilter
 - RESOURCE_LOCAL JDBC tx

Google Guice JPA support
 - guice-persist
 - UnitOfWork unitOfWork
 - JpaPersistModule
 - JpaLocalTxnInterceptor
 - TODO live debug
 - JpaPersistService
 - TODO manual start nad end of transaction, persistence context

Optimistic and Pessimistic locking
 - optimistic locking 
 - pessimistic locking (SELECT FOR UPDATE)
 - SELECT FOR SHARE and deadlock situations
 - force increment locking
 - repeatable read vs read committed isolation
 - setting the lock mode during find and query
 -     Collections.singletonMap( "javax.persistence.lock.timeout", 200 )

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
 - fetch JOIN
 - set vs list and multiple bags
 - batch
 - subselect, select, join
 - LAZY is recommended strategy
 
Caching - Second Level Cache
 - entity cache
 - query cache 
 - collection cache

JDBC batching example 
