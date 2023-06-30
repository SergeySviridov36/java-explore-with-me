FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats_server.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004
ENTRYPOINT ["java","-jar","/stats_server.jar"]