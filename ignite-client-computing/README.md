# ignite-computing
Demo project for Ignite compute grid

## Usage
1. open project root in terminal
2. build artifacts `./gradlew clean fatJar`
3. process any one of available launches described in sections below
4. load data to cache by
    ```
    http://localhost:8080/product/cache/load/%2Ftmp%2Fignite-data%2Fjcpenney_com-ecommerce_sample.csv
    ```
5. run counting by following command, see how execution 
distributed over instances
    ```
    http://localhost:8080/product/cache/count
    ```
6. run counting by following command, see how execution 
   distributed over instances
   ```
   http://localhost:8080/product/cache/countLocally
   ```

### Docker launch
1. launch ignite docker instances
    ```
    docker build . --tag=myignite -f server-dockerfile 
    docker run -d --name my-ignite-1 -p 47500:47500 -p 47100:47100 -e "CONFIG_URI=file:///tmp/server-config.xml" myignite
    docker run -d --name my-ignite-2 -p 47501:47500 -p 47101:47100 -e "CONFIG_URI=file:///tmp/server-config.xml" myignite
    docker run -d --name my-ignite-3 -p 47502:47500 -p 47102:47100 -e "CONFIG_URI=file:///tmp/server-config.xml" myignite
    ```
2. built application `./gradlew dockerBuildImage`
2. start application 
    ```
    docker run -d --name my-ignite-client -p 8080:8080 edu.ignite.computing
    ```

### Local launch
1. install ignite to `$IGNITE_HOME`
3. copy artifacts to ignite
    ```
    cp -f ./build/libs/* $IGNITE_HOME/libs/
    mkdir -p "/tmp/ignite-data/" 
    cp -f ./jcpenney_com-ecommerce_sample.csv /tmp/ignite-data/
    ```
4. start some instances of ignite 
    ```
    $IGNITE_HOME/bin/ignite.sh ./server-config.xml
    ```
3. start application `./gradlew bootRun`


## References
http://apache-ignite-users.70518.x6.nabble.com/Loadcache-behavior-td2571.html
https://apacheignite.readme.io/docs/data-loading#ignitecacheloadcache