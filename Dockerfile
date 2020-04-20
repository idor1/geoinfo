FROM openjdk:8-jdk-alpine

COPY /target/*.jar app.jar

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

CMD /wait && npm start
ENTRYPOINT ["java","-jar","/app.jar"]