FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY Libreria/pom.xml .
COPY Libreria/src ./src
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
ENV TOMCAT_VERSION=9.0.120
ENV TOMCAT_HOME=/opt/tomcat

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/* \
    && curl -fsSL "https://dlcdn.apache.org/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz" -o /tmp/tomcat.tar.gz \
    && mkdir -p ${TOMCAT_HOME} \
    && tar -xzf /tmp/tomcat.tar.gz -C ${TOMCAT_HOME} --strip-components=1 \
    && rm -f /tmp/tomcat.tar.gz \
    && rm -rf ${TOMCAT_HOME}/webapps/*

COPY --from=build /app/target/Libreria-0.0.1-SNAPSHOT.war ${TOMCAT_HOME}/webapps/Libreria.war

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
