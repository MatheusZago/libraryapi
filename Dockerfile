#build
FROM maven:3.8.8-amazoncorretto-21-al2023
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

#run
FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build ./build/target/*.jar ./libraryapi.jar

#Exposing the ports the app will use
EXPOSE 8080
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USERNAME=''
ENV GOOGLE_CLIENT_ID='client_id'
ENV GOOGLE_CLIENT_SECRET='client_secret'

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

#To initialize the app
ENTRYPOINT java -jar libraryapi.jar