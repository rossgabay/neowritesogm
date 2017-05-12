package com.rgabay.neowritesogm;


import com.rgabay.neowritesogm.domain.OgmTestNode;
import com.rgabay.neowritesogm.domain.OgmTestRelated;

import com.rgabay.neowritesogm.domain.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.*;
import org.neo4j.ogm.transaction.Transaction;

@Slf4j
public class App {

    private static final String DOMAIN_PACKAGE = "com.rgabay.neowritesogm.domain";

    public static void main(String[] args) {


        SessionFactory sessionFactory = new SessionFactory(DOMAIN_PACKAGE);
        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        OgmTestNode ogmTestNode = new OgmTestNode();
        ogmTestNode.setName("test_node_1");

        OgmTestRelated ogmTestRelated = new OgmTestRelated();
        ogmTestRelated.setName("related_name_1");

        Set<OgmTestRelated> relateds = new HashSet<>();
        relateds.add(ogmTestRelated);

        ogmTestNode.setRelateds(relateds);

        ogmTestNode.setRoles(Arrays.asList(RoleType.DEVELOPER, RoleType.ARCHITECT));
        tx.commit();
        session.save(ogmTestNode);
        tx.close();

        //try to reuse the session with new transaction
        tx = session.beginTransaction();
        OgmTestNode ogmTestNode2 = new OgmTestNode();
        ogmTestNode2.setName("test_name_2");

        OgmTestRelated ogmTestRelated2 = new OgmTestRelated();
        ogmTestRelated2.setName("related_name_2");

        relateds = new HashSet<>();
        relateds.add(ogmTestRelated2);

        ogmTestNode2.setRelateds(relateds);

        tx.commit();
        session.save(ogmTestNode2);
        tx.close();

        session.clear();

        tx = session.beginTransaction();
        //now lets read some of the data with cypher
        log.info("let's read some nodes back");

        String query1 = "match (n{name:\"test_node_1\"})-[r:RELATED_TO]-(rt) return n,r,rt ";
        System.out.println("Cypher load #1 - looking for OgmTestNode with populated List of Enums. ...");
        Iterable<OgmTestNode> r =  session.query(OgmTestNode.class, query1, Collections.EMPTY_MAP);
        r.forEach(System.out::println);

        String query2 = "match (n{name:\"test_name_2\"})-[r:RELATED_TO]-(rt) return n,r,rt ";
        System.out.println("Cypher load #2 - looking for OgmTestNode with empty List of Enums...");
        Iterable<OgmTestNode> r2 =  session.query(OgmTestNode.class, query2, Collections.EMPTY_MAP);
        r2.forEach(System.out::println);

        System.out.println("loadAll...");
        Collection<OgmTestNode> c = session.loadAll(OgmTestNode.class);
        c.forEach(System.out::println);

        tx.close();
    }
}