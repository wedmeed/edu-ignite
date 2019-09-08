# ignite-computing
Demo project for Ignite compute grid

1. start some instances of application
2. for any of them check working port:
``` 2019-09-07 16:36:23.409  INFO 24891 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port(s): 61904```
3. put port into following URLs and invoke them:
```http://localhost:61904/product/cache/load```
```http://localhost:61904/product/cache/count```
```http://localhost:61904/product/cache/countLocally```
4. check logs


if you wish to have net of application with pure ignite instances then:
1. create a jar with application classes (for IDEA: "artifacts" in "Project Structure")
2. put artifacts to $IGNITE_HOME/libs
3. check correctness of library versions in $IGNITE_HOME/libs
4. run ignite instance with $IGNITE_HOME/bin/ignite.sh $CONFIG_PATH/config.xml
5. run application and follow steps from the previous list 


## There are some limitations which do not allow to make docker solution in easy way:
- discovery is working through IP-addresses, not through hostnames
 (not sure). It create troubles for docker.for.mac.localhost 
 (or similar) hostname usage 
- peer class loading works only for computing:
 https://www.gridgain.com/resources/blog/apacher-ignitetm-tip-peer-class-loading-deployment-magic. 
 It lead to the necessity of manual library delivery
- for ignite version 2.7.5 some lib versions in docker image do not match dependencies in maven
 (e.g. com.datastax.cassandra:cassandra-driver-core:3.6.0)    
 
### Possible workaround
gradlew dockerBuildImage
docker run --name bootignite -e "CONFIG_URI=file:///tmp/config.xml" -v "$DOCKER_STORAGES/ignite-storage":/tmp edu.ignite.computinf:0.0.1-SNAPSHOT
docker run --name myignite -e "CONFIG_URI=file:///tmp/config.xml" -v "$DOCKER_STORAGES/ignite-storage":/tmp apacheignite:ignite



