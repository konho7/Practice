# 1단계: Maven으로 WAR 빌드
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .

# DB_PROFILE=H2 (기본, RDS 없이 로컬 테스트용) / MySQL (실제 RDS 연동용)
ARG DB_PROFILE=H2
RUN ./mvnw -DskipTests -P ${DB_PROFILE} package

# 2단계: Jetty 위에 WAR 얹어서 실행
FROM jetty:9.4-jdk17
COPY --from=build /app/target/petclinic.war /var/lib/jetty/webapps/root.war

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/ || exit 1
