FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/tokenManagement-1.0.0-SNAPSHOT.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar tokenManagement-1.0.0-SNAPSHOT.jar