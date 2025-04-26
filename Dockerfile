FROM openjdk:23-jdk AS BUILD
ADD https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz .
RUN tar xzvf apache-maven-3.9.9-bin.tar.gz -C /opt/ && \
    ln -s /opt/apache-maven-3.9.9 /opt/maven
ENV MAVEN_HOME /opt/maven
ENV PATH $MAVEN_HOME/bin:$PATH
COPY . /build
WORKDIR /build
RUN mvn clean package

FROM openjdk:23-jdk
WORKDIR /app
ADD https://curl.se/download/curl-8.13.0.tar.gz .
RUN tar xzvf curl-8.13.0.tar.gz
COPY --from=BUILD ./build/target ./target
CMD ["java","-jar","target/telegram-bot-0.0.1-SNAPSHOT.jar"]