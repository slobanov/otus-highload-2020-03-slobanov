heroku container:login
docker tag otushighload202003slobanov/social-network:latest registry.heroku.com/otus-highload-slobanov/web
docker push registry.heroku.com/otus-highload-slobanov/web
heroku container:release web -a otus-highload-slobanov
