heroku create otus-highload-slobanov
heroku addons:create cleardb:ignite -a otus-highload-slobanov
heroku config:set SPRING_PROFILES_ACTIVE=heroku -a otus-highload-slobanov
