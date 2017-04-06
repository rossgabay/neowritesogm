package com.rgabay.neowritesogm.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
public class OgmTestNode {

    @GraphId
    private Long id;

    @Relationship(type = "RELATED_TO") // check  neo4j label
    Set<OgmTestRelated> relateds;

    private String name;
    private String new_prop;
    private Integer customId;
}