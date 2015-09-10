#!/bin/bash
VPID=$1

function cleanup() {
  kill -9 $VPID
  kill -9 $$
}
trap cleanup 0 1 2 3 15

while true; do
 echo -e "\033[31m **********OPEN FILES FOR PROCESS: $VPID:  $(lsof -p$VPID | wc -l) \033[0m"; 
 sleep 1
done
