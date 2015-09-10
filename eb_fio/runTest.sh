#!/bin/bash

vertx run startup.js &
VPID=$! 
echo vx running as $VPID
bash pollfh.sh $VPID 
