# ignite-computing
Demo project for Ignite compute grid


1. open project root in terminal
2. build artifacts and copy them to `$IGNITE_HOME/bin`
2. start some instances of ignite 
    ```
    $IGNITE_HOME/bin/ignite.sh ./server-config.xml
    ```
3. start application
    ```
    gradlew bootRun
    ```
3. load data to cache by
    ```
    http://localhost:8080/product/cache/load
    ```
4. run counting by following command, see how execution 
distributed over instances
    ```
    http://localhost:8080/product/cache/count
    ```
5. run counting by following command, see how execution 
   distributed over instances
   ```
   http://localhost:8080/product/cache/countLocally
   ```

### References
http://apache-ignite-users.70518.x6.nabble.com/Loadcache-behavior-td2571.html
https://apacheignite.readme.io/docs/data-loading#ignitecacheloadcache