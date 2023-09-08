FROM openjdk:17-alpine
VOLUME /tmp
COPY target/spacelab_admin.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]