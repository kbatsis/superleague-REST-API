FROM amazoncorretto:17

EXPOSE 8080

WORKDIR /app

COPY . .

CMD [ "./gradlew", "bootRun" ]