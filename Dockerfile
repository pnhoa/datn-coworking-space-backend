FROM eclipse-temurin:8-alpine
WORKDIR /app
COPY /target/coworking-space.jar /app/coworking-space.jar
ENTRYPOINT ["java","-jar"]
CMD ["coworking-space.jar"]