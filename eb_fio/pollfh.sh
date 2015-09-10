#!/bin/bash
while true; do
 echo **********OPEN FILES FOR PROCESS: $1:  $(lsof -p$1 | wc -l)
 sleep 1
done
