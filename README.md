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
  2. Create a truststore for keyspaces [Docs](https://docs.aws.amazon.com/keyspaces/latest/devguide/using_java_driver.html#using_java_driver.BeforeYouBegin)
  2. run `mvn package` within the repo directory
  3. Set environment variables
    * CASS_USER - Username for aws keyspaces-specific credentials
    * CASS_PASS - Password for aws keyspaces-specific credentials
    * JWT_SECRET - Secret used for signing JWT
    * TRUSTSTORE_PASS - Password used for the created truststore
  4. run `java -jar target/TRMS-0.0.1-SNAPSHOT.jar`
