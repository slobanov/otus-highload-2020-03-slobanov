read -r -d '' EXPLAIN_QUERY <<- EOM
EXPLAIN SELECT USER_PERSONAL_DETAILS.ID,
               USER_PERSONAL_DETAILS.USER_ID,
               USER_PERSONAL_DETAILS.FIRST_NAME,
               USER_PERSONAL_DETAILS.LAST_NAME,
               USER_PERSONAL_DETAILS.BIRTH_DATE,
               GENDER.ID AS GENDER_ID,
               GENDER.NAME AS GENDER_NAME,
               CITY.ID AS CITY_ID,
               CITY.NAME AS CITY_NAME
          FROM USER_PERSONAL_DETAILS
          JOIN GENDER
            ON USER_PERSONAL_DETAILS.GENDER_ID = GENDER.ID
          JOIN CITY
            ON USER_PERSONAL_DETAILS.CITY_ID = CITY.ID
         WHERE LAST_NAME LIKE 'ha%'
               AND FIRST_NAME LIKE 'ha%'
         ORDER BY ID DESC
         LIMIT 5001;
EOM

../social-network/scripts/docker-compose/docker-mysql-exec.sh "$EXPLAIN_QUERY"
