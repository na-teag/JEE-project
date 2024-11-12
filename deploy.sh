#! /bin/bash
rm -r target
mvn install tomcat7:deploy
sudo rm -r /var/lib/tomcat9/webapps/ent.war /var/lib/tomcat9/webapps/ent
sudo cp -r target/projet_jee-1.0-SNAPSHOT /var/lib/tomcat9/webapps/ent
sudo cp target/projet_jee-1.0-SNAPSHOT.war /var/lib/tomcat9/webapps/ent.war