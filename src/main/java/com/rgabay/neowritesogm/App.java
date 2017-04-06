package com.rgabay.neowritesogm;


import com.beust.jcommander.JCommander;
import com.rgabay.neowritesogm.domain.OgmTestNode;
import com.rgabay.neowritesogm.domain.OgmTestRelated;
import com.rgabay.neowritesogm.util.JCommanderSetup;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.neo4j.ogm.transaction.Transaction;

@Slf4j
public class App {

    private static final String DOMAIN_PACKAGE = "com.rgabay.neowritesogm.domain";
    private static final int DEFAULT_NODES_NUM = 1000;


    public static void main(String[] args) {

        JCommanderSetup jcommanderSetup = new JCommanderSetup();

        new JCommander(jcommanderSetup, args);

        int nodesNum = (jcommanderSetup.getNodesNum() == null) ?  DEFAULT_NODES_NUM : jcommanderSetup.getNodesNum();
        List<OgmTestNode> nodes =  Stream.generate(OgmTestNode::new).limit(nodesNum).collect(Collectors.toList());

        log.info("nodes to write: {} ", nodesNum);

        SessionFactory sessionFactory = new SessionFactory(DOMAIN_PACKAGE);
        sessionFactory.register(new com.rgabay.neowritesogm.listeners.AddUuidPreSaveEventListener());
        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        DateTime dtStart = new DateTime();
        log.info("Start Time : {}",  dtStart.toString());

     //   nodes.parallelStream().forEach(session::save);

        DateTime dtEnd = new DateTime();
        log.info("End Time : {}", dtEnd);
        log.info("Duration : {}", new Period(dtStart, dtEnd));

        //now let's "manually" create/save some OgmTestNode nodes with related_to OgmTestRelated nodes

        OgmTestNode ogmTestNode = new OgmTestNode();
        ogmTestNode.setName("test_node_1");

        OgmTestRelated ogmTestRelated = new OgmTestRelated();
        ogmTestRelated.setName("related_name_1");

        Set<OgmTestRelated> relateds = new HashSet<>();
        relateds.add(ogmTestRelated);

        ogmTestNode.setRelateds(relateds);

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

        // Cypher update
        tx = session.beginTransaction();
        Result r1 = session.query("match (n{name:\"test_node_1\"}) set n.new_prop=\"new data\" return n", Collections.EMPTY_MAP);
        tx.commit();
        tx.close();

        // NOTE: this is important here since it "flushes" the data
        //session.clear();

        //now lets read some of the data with cypher
        log.info("let's read some nodes back");
        //Result r =  session.query("match (n) return n limit 10", Collections.EMPTY_MAP);

        // the result totally depends on whether session.clear() was called or not.
        // if it was - relateds aren't loaded because the query
        // itself doesn't load them, it it wasn't - then they get retrieved from cache
        // ... and since we're doing a manual cypher update above those updates are only picked up if clear() was called
        System.out.println("Cypher load...");
        Result r =  session.query("match (n{name:\"test_node_1\"}) return n ", Collections.EMPTY_MAP);
        r.forEach(System.out::println);

        System.out.println("loadAll...");
       Collection<OgmTestNode> c = session.loadAll(OgmTestNode.class);
       c.forEach(System.out::println);
       //c.stream().map(z -> z.getRelateds()).forEach(System.out::println);
    }
}