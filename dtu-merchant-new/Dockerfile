FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/dtu-merchant-new-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar dtu-merchant-new-1.0.0.jar
