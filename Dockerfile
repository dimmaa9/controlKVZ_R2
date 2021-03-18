FROM openjdk:11.0.10
COPY . /usr/app/
WORKDIR /usr/app/
EXPOSE 8081
RUN mv target/*jar control-1.jar
ENTRYPOINT ["java", "-jar", "control-1.jar"]