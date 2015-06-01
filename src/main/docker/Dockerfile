# This tells Docker to use a Java 7 base image
FROM java:7

# Mounts a /tmp directory which Spring Boot uses for Tomcat working folders.
VOLUME /tmp

# Adds the jar file to the container with the given name "todo-svc.jar"
# Note the first parameter should match the build product from your gradle script.
ADD todo-microservice-0.1.0.jar todo-svc.jar

# Not really critical for a jar file, but docker creates all files with an
# unmodified time.  For static content files, they'd want a modification date.
RUN bash -c 'touch /todo-svc.jar'

# Defines the command and commandline properties to send when this image starts up.
# The "urandom" setting helps Tomcat to start faster.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/todo-svc.jar"]