../social-network/scripts/docker-compose/docker-mysql-exec.sh \
  "CREATE index LN_FN_ID_DESC_INDX on USER_PERSONAL_DETAILS(LAST_NAME, FIRST_NAME, ID DESC);"
