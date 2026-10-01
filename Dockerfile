# Jetty 위에 WAR 얹어서 실행 (Maven 빌드는 CI에서 미리 끝내고 target/을 받아옴)
FROM jetty:9.4-jdk17
COPY target/petclinic.war /var/lib/jetty/webapps/root.war

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/ || exit 1