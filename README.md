# TRMS

Tuition Reimbursement Management System RESTful API. 
Employees can submit a reimbursemnt request and that request will be passed along and approved by the appropriate positions.


## Technologies
 * Java 8
 * Javalin
 * AWS Keyspaces
 * Datastax
 * AWS S3
 * Spring

## Getting Started
  1. `git clone https://github.com/DuskDaleSpider/TRMS.git`
  3. Create a truststore for keyspaces [Docs](https://docs.aws.amazon.com/keyspaces/latest/devguide/using_java_driver.html#using_java_driver.BeforeYouBegin
  4. Create the keyspace and tables in AWS Keyspaces using the cql found in src/main/resources/trms_table_creation.cql
  5. run `mvn package` within the repo directory
  6. Set environment variables

    CASS_USER - Username for aws keyspaces-specific credentials
    CASS_PASS - Password for aws keyspaces-specific credentials
    JWT_SECRET - Secret used for signing JWT
    TRUSTSTORE_PASS - Password used for the created truststore
  7. copy `trms.jar` and your truststore to the same dir
    * Example: 
```
          $ ls -al
          total 35140
          drwxr-xr-x 1 Dakota 197121        0 May 21 21:53 ./
          drwxr-xr-x 1 Dakota 197121        0 May 21 21:52 ../
          -rw-r--r-- 1 Dakota 197121     1109 May 21 21:52 cassandra-truststore.jks
          -rw-r--r-- 1 Dakota 197121 35973790 May 21 21:53 trms.jar
```
  7. run `java -jar trms.jar`
