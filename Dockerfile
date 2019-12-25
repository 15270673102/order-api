FROM tylnn/sk-centos-jdk:1.0
MAINTAINER 15270673102@163.com

COPY target/*.jar order-api.jar
RUN sh -c "touch /order-api.jar"

EXPOSE 8080
ENTRYPOINT ["java", "-Xms2048m", "-Xmx2048m", "-XX:MaxNewSize=1024m", "-jar", "order-api.jar"]
