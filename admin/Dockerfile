FROM openjdk:8
EXPOSE 8533
VOLUME /tmp
ADD target/admin.jar  /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+8","-jar","/app.jar"]
