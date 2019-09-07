# ignite-computing
Demo project for Ignite compute grid


/opt/ignite/apache-ignite/bin/ignite.sh /Users/asokolov/Desktop/edu/ignite/ignite-computing/src/main/resources/config.xml

1. for docker: 

docker run --name bootignite -e "CONFIG_URI=file:///tmp/config.xml" -v "/Users/asokolov/Desktop/docker/ignite-storage":/tmp edu.ignite.computinf:0.0.1-SNAPSHOT

gradlew dockerBuildImage
docker run --name myignite -e "CONFIG_URI=file:///tmp/config.xml" -v "/Users/asokolov/Desktop/docker/ignite-storage":/tmp apacheignite:ignite




there are some possible bugs with cassandra persistence storage:
http://apache-ignite-users.70518.x6.nabble.com/Getting-NullPointerException-during-commit-into-cassandra-after-reconnecting-to-ignite-server-td22005.html

also peer class loading works only for computing:
https://www.gridgain.com/resources/blog/apacher-ignitetm-tip-peer-class-loading-deployment-magic


