# Movie Api
## Technologies: 
- Java, 
- Springboot:
  - Rest Controller
  - JPA Repository with Hibernate
  - Bean Validation
  - Swagger

## Driver JDBC

Add dependency according to RDBMS chosen.

### Maven dependency

- Maria DB:
```
  <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
      <scope>runtime</scope>
  </dependency>
```
- MySQL:
```
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
  </dependency>
 ``` 
- PostgreSQL:
```
  <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
  </dependency>
 ``` 
- Microsoft SQL Server:
```
  <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>mssql-jdbc</artifactId>
      <scope>runtime</scope>
  </dependency>
```
- Oracle Database  
```
  <dependency>
     <groupId>com.oracle.database.jdbc</groupId>
     <artifactId>ojdbc8</artifactId>
     <scope>runtime</scope>
  </dependency>
```
### Gradle Dependencies
- MySQL:
  ```
  runtimeOnly 'com.mysql:mysql-connector-j'
  ```
- Oracle Database:
  ```
  runtimeOnly 'com.oracle.database.jdbc:ojdbc8'
  ```
- Microsoft SQL Server:
  ```
  runtimeOnly 'com.microsoft.sqlserver:mssql-jdbc'
  ```
- PostgreSQL:
  ```
  runtimeOnly 'org.postgresql:postgresql'
  ```
- Maria DB: 
  ```
  runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
  ```

## Application Configuration
Example of config file applications.properties to deploy with api.
Adapt url JDBC and credentials.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=movie
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```
