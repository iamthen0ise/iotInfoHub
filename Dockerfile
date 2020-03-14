ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk as BUILD

RUN mkdir /app

COPY . /src
WORKDIR /src
RUN ./gradlew --no-daemon --parallel shadowJar

FROM openjdk:${VERSION}-jre
COPY --from=BUILD /src/build/libs/shadow-stable.jar /bin/runner/run.jar

RUN apt-get update -qqy && \
    apt-get -qqy install \
    postgresql-client

COPY ./deploy /app/deploy/

WORKDIR /bin/runner
CMD ["java",  "-jar", "run.jar"]
