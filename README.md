Database Support Reports Web Application

----------
1. Configuring on your local box
	- requires JDK 7 to be installed, you can get it here (Java SE Development Kit 6 Downloads)
	- Java 6 has to be enabled in your OS so the following command "java -version" should return "1.6.xx
	- requires Maven 3.2.5

2. Troubleshooting
2.1. Tomcat
	-Ddbs.reports.processing.threads.max=30
    -XX:+CMSClassUnloadingEnabled
    -XX:+CMSPermGenSweepingEnabled
    -XX:MaxPermSize=128M

    JAXB + Tomcat
    Jesli przy generowaniu formatki leci cos ala: java.lang.NoSuchMethodError: javax.xml.bind.annotation.XmlElementType..

    Wymagane jest: jaxb-api-2.2.2.jar
    Pod IntelliJ jedyne rozwiazanie to umiescic jara w katalogu endorsed w katalogu Tomcata.
    Czyli jesli Tomcat: D:\Program Files\apache-tomcat-6.0.43 to: D:\Program Files\apache-tomcat-6.0.43\endorsed\jaxb-api-2.2.2.jar
    -Djava.endorsed.dirs="" - jest nadpisywane(?)
    jconsole.exe pokazuje ze ten katalog jest brany tylko pod uwage.

2.2. Persistance
    Jesli nie buduje sie metamodel:
    # wymagane jest by eclipse mial podpieta jdk, a nie jre > installed JRE (?)

    org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor

2.3. Oracle
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
    C:\>sqlplus HR/...


    To change the default port from 8080 to something else, execute:
    sqlplus '/ as sysdba'
    SQL> EXEC DBMS_XDB.SETHTTPPORT(80);
    Check if the port was changed:
    SQL> SELECT dbms_xdb.gethttpport, dbms_xdb.getftpport FROM dual;