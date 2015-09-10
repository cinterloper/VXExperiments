#!/bin/bash

vertx run database.groovy &
VPID=$! 
echo vx running as $VPID
bash pollfh.sh $VPID 
