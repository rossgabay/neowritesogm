#neowritesogm

##very basic harness to write specified number of nodes to neo4j using the ogm bolt driver
- the endpoint is statically configured in the ```ogm.properties``` file to point at localhost
- the only parameter the harness accepts is ```-n number_of_nodes``` (default is 1000)

##to run:
 - clone the repo
 - mvn clean install
 - ```java -jar ./target/neowritesogm-1.0-SNAPSHOT.jar -n 10000```
