DBS (Database Support) Reports Web Application
Monolittyczna aplikacja webowa do generowania raportow dla DBS.

----------
1. Configuring on your local box
	- requires JDK 6 to be installed
	- Java 6 has to be enabled in your OS so the following command "java -version" should return "1.6.xx
	- requires Maven 3.2.5


2. Building application
To build a war file, simply run:

	mvn clean package [-Pdev|tes|acc|pro]

2.1. Run tests

    mvn clean test

2.1.1. To skip tests

    mvn clean install -Dmaven.test.skip=true

3. Running Reports on your local box

    Go to: http://localhost:8080/reports

3.1. Local DB
    Create local Postgres database named: 'reports' with user 'reports'.

3.2. Running on Tomcat (6) server
    - Provide context.xml:

    <Resource
            auth="Container"
            driverClassName="org.postgresql.Driver"
            maxActive="20"
            maxIdle="10"
            maxWait="-1"
            name="reports/jdbc/DataSource"
            password="..."
            type="javax.sql.DataSource"
            url="jdbc:postgresql://localhost:5432/reports"
            username="reports"/>


    -- Available run parameters
    -Ddbs.reports.processing.threads.max=30
    -XX:+CMSClassUnloadingEnabled
    -XX:+CMSPermGenSweepingEnabled
    -XX:MaxPermSize=1024m
    -XX:PermSize=512m
    -Xms256m
    -Xmx512m
    -DbuildNumber=0
    -Dspring.profiles.active=[dev|tes|pro] (FIXME)
    -Dliquibase.shouldRun=[true|false]

3.3. Troubleshooting
    - add lib/postgresql-9.1-901.jdbc4.jar, ojdbc6-11.2.0.jar
    - allow full access for user to server logs, temp folders
    - JAXB + Tomcat
    -- Jesli przy generowaniu formatki leci cos ala: java.lang.NoSuchMethodError: javax.xml.bind.annotation.XmlElementType..
    - Wymagane jest: jaxb-api-2.2.2.jar
    -- Pod IntelliJ jedyne rozwiazanie to umiescic jara w katalogu endorsed w katalogu Tomcata.
    Czyli jesli Tomcat: D:\Program Files\apache-tomcat-6.0.43 to: D:\Program Files\apache-tomcat-6.0.43\endorsed\jaxb-api-2.2.2.jar
    - -Djava.endorsed.dirs="" - jest nadpisywane(?) jconsole.exe pokazuje ze ten katalog jest brany tylko pod uwage.
    - Jesli nie buduje sie metamodel:
        -- wymagane jest by eclipse mial podpieta jdk, a nie jre > installed JRE (?)
        -- org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor

    - Oracle Expres Edition local setup problems
        Apex: http://127.0.0.1/apex/apex_admin (nie dziala :/)

        Odblokowanie HR/zmiana hasla:
        - uruchom SQL Plus
        - alter user hr account unlock;
        - alter user hr identified by NEW_PASSWORD;

        Albo:
        Oracle -> Run SQL command
        - connect hr.. poprosi o zmiane hasla..

        Reset hasla do apex:
        - uruchom SQL Plus
        - @C:\oraclexe\app\oracle\product\11.2.0\server\apex\apxxepwd NEW_PASSWORD;

        http://oraclepitstop.wordpress.com/2008/04/16/installing-configuring-apex-application-express-htmldb-with-10gr2-on-windows-xp/
        http://blog.mclaughlinsoftware.com/2011/09/14/reset-11g-xe-apex-password/

        Zainstralowanie sterownikow do lokalnego repo mavena:
        # http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html
        # mvn install:install-file -Dfile=D:\oracle\app\oracle\product\11.2.0\server\jdbc\lib\ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

        To login as the DBA user:
        C:\>set ORACLE_SID=XE
        C:\>sqlplus / as sysdba
        SQL> SELECT * FROM dba_users;

        Login to demo accounts, say HR:
        C:\>set ORACLE_SID=XE
        C:\>sqlplus HR/HR

        To change the default port from 8080 to something else, execute:
        sqlplus '/ as sysdba'
        SQL> EXEC DBMS_XDB.SETHTTPPORT(80);
        Check if the port was changed:
        SQL> SELECT dbms_xdb.gethttpport, dbms_xdb.getftpport FROM dual;

4. Deployment
4.1. Using TeamCity server deployment.
    - Target server (Tomcat) should have defined deployer user. In tomcat-users.xml:

        <user username="deployer" password="deployer" roles="manager-gui,manager-script" />

    - Local Maven settings.xml should contain:

        <servers>
            <server>
                <id>localhost-tomcat</id>
                <username>deployer</username>
    			<password>deployer</password>
            </server>
        </servers>


