# run 'docker build -t my-tag .'
# at root project dir to create a image tagged my-tag

# Start with a base image (alpine is a linux distro) with java 8 installed, exposing port 8080
FROM openjdk:8-jdk-alpine
EXPOSE 8080

# copy to root changing name
COPY build/libs/SomeBank-1.jar app.jar

# ENTRYPOINT defines command, default is '/bin/sh -c'
# CMD specifies arguments to ENTRYPOINT
# RUN = CMD committing new docker layer
CMD ["java", "-jar", "app.jar"]

# run 'docker run -d -p 5000:8080 --name container-name my-tag'
# to start a detached container named container-name from my-tag using host post 5000 connected to internal image port 8080

# next time just run 'docker start container-name' or 'docker stop container-name'