package com.rgabay.neowritesogm.util;

import com.beust.jcommander.Parameter;
import lombok.Data;


/**
 * Created by rossgabay on 3/3/17.
 */
@Data
public class JCommanderSetup {

    @Parameter(names = "-n", description = "number of nodes to write")
    private Integer nodesNum;

}
