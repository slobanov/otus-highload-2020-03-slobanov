#!/bin/bash

FILE_NAME="result.csv"
DEFAULT_THREDS=8

function load_test () {
  > $FILE_NAME
  echo "connections,latencyp90[ms],throughput[rps]" > ${FILE_NAME}

  for CONNECTIONS in 1 10 100 1000
  do
    THREADS=$([ $CONNECTIONS -le $DEFAULT_THREDS ] && echo "$CONNECTIONS" || echo "$DEFAULT_THREDS")
    echo -n "$CONNECTIONS," >> $FILE_NAME
    wrk -t$THREADS -c$CONNECTIONS -d120s --latency --timeout 30s -s load_script.lua \
      'http://localhost:8080/social-network/api/user/?limit=5000'
  done
}

./drop-LN_FN_ID_DESC_INDX.sh
load_test
mv $FILE_NAME no_index.csv
./create-LN_FN_ID_DESC_INDX.sh
load_test
mv $FILE_NAME with_index.csv
