FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main_service.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
ENTRYPOINT ["java","-jar","/main_service.jar"]