package com.rgabay.neowritesogm.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by rossgabay on 4/3/17.
 */
@NodeEntity
@Data
public class OgmTestRelated {

    @GraphId
    private Long id;

    private String name;
}
