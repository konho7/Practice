# 1단계: Maven으로 WAR 빌드
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
ARG DB_PROFILE=H2
RUN ./mvnw -P ${DB_PROFILE} dependency:go-offline -B

COPY . .
RUN ./mvnw -DskipTests -P ${DB_PROFILE} package -B

# 2단계: Jetty 위에 WAR 얹어서 실행
FROM jetty:9.4-jdk17
COPY --from=build /app/target/petclinic.war /var/lib/jetty/webapps/root.war

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/ || exit 1