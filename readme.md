## Busline 1.0

### Get Api key
Get your api key for trafiklab API "SL Hållplatser och Linjer 2" from https://www.trafiklab.se

### How to start locally with gradle
```
$ export TRAFIKLAB_API_KEY=<APIKEY>
$ ./gradlew bootRun
```
Browse to `http://localhost:8080/`

### How to build and start locally with Docker
```
$ ./gradlew jibDockerBuild
$
$ docker run -e TRAFIKLAB_API_KEY=<APIKEY> --name busline -it -d busline
$ docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' busline
```
Browse to `http://<busline ip>:8080/`


