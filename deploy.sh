git pull
mvn clean install
docker stop prog_ing_be
docker rm prog_ing_be
docker build -t prog_ing_be .
docker run --restart=always -d --name prog_ing_be -p 8080:8080 prog_ing_be