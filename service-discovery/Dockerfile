FROM openjdk:17
VOLUME /tmp
EXPOSE 8761
ARG JAR_FILE
COPY ${JAR_FILE} service-discovery.jar
ENTRYPOINT ["java","-jar","/service-discovery.jar"]