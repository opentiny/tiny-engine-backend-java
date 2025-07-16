FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app 
ADD . .
RUN wget --no-check-certificate https://mirrors.huaweicloud.com/apache/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz

RUN tar -zxvf apache-maven-3.9.8-bin.tar.gz

RUN rm apache-maven-3.9.8-bin.tar.gz
ENV M2_HOME=/app/apache-maven-3.9.8
ENV PATH=$M2_HOME/bin:$PATH
COPY settings.xml /app/apache-maven-3.9.8/conf/settings.xml
RUN mvn clean package -Dmaven.test.skip=true
FROM eclipse-temurin:17-jdk-jammy 
WORKDIR /app
COPY --from=build /app/app/target/tiny-engine-app-*.jar /app/tiny-engine-app.jar
COPY --from=build /app/base/target/tiny-engine-base-*.jar /app/tiny-engine-base.jar

ENTRYPOINT ["java", "-jar", "tiny-engine-app.jar", "--spring.profiles.active=alpha"]
EXPOSE 9090

