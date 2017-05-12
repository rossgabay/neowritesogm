package com.rgabay.neowritesogm.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Set;

@NodeEntity
@Data
public class OgmTestNode {

    @GraphId
    private Long id;

    @Relationship(type = "RELATED_TO")
    Set<OgmTestRelated> relateds;

    @Property
    List<RoleType> roles;

    private String name;
    private String new_prop;
    private Integer customId;
}