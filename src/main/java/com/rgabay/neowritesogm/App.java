package com.rgabay.neowritesogm;


import com.beust.jcommander.JCommander;
import com.rgabay.neowritesogm.domain.OgmTestNode;
import com.rgabay.neowritesogm.util.JCommanderSetup;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.Period;

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
        Session session = sessionFactory.openSession();

        DateTime dtStart = new DateTime();
        log.info("Start Time : {}",  dtStart.toString());

        nodes.parallelStream().forEach(session::save);

        DateTime dtEnd = new DateTime();
        log.info("End Time : {}", dtEnd);
        log.info("Duration : {}", new Period(dtStart, dtEnd));

    }
}