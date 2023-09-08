FROM openjdk:17-alpine
EXPOSE 1488
VOLUME /tmp
COPY target/spacelab_admin.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]