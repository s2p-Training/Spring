# Introduction To Database Migration

**Database migration** is the process of moving data, schemas, and objects from a source database to a target database.

In this section we will be moving all the data from H2 Database To MySQL Database. 

1. Update The POM.xml, Comment The H2 Database Dependencies
```xml
<dependencies>
<!--		<dependency>-->
<!--			<groupId>org.springframework.boot</groupId>-->
<!--			<artifactId>spring-boot-h2console</artifactId>-->
<!--		</dependency>-->

<!--		<dependency>-->
<!--			<groupId>com.h2database</groupId>-->
<!--			<artifactId>h2</artifactId>-->
<!--			<scope>runtime</scope>-->
<!--		</dependency> -->

</dependencies>
```

2. Update The pom.xml, Add New MySQL Driver Dependency & Sync Maven Changes
```xml

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>

```

3. Go To MySQL Workbench Or Ant Database Client And Create The Database With The Name **JobPortalDB**
```sql
CREATE DATABASE JobPortalDB;
```

4. For Performing Operations On Database, Select Or Switch To The **JobPortalDB** Database
```sql
USE JobPortalDB; 
```

5. From Project Directory, Go To resources/sql/schema.sql file, copy the whole content and create companies table inside database.
6. From Project Directory, Go To resources/sql/data.sql file, copy the whole content and insert the data inside companies table.

7. Update The application.properties file and MySQL Database Configuration Settings
```properties
spring.datasource.url=jdbc:mysql://${DATABASE_HOST:localhost}:${DATABASE_PORT:3306}/${DATABASE_NAME:JobPortalDB}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:root}
spring.jpa.show-sql=${SHOW_SQL:true}
spring.jpa.properties.hibernate.format_sql=${HIBERNATE_FORMAT_SQL:true}
spring.jpa.properties.hibernate.highlight_sql=true
```
8. Re-run The Application And This Time Repository Object Will Be Using MySQL Database For Fetching The Database. 