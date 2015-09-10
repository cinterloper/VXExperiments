#!/bin/bash
trap cleanup 0 1 2 3 15

function cleanup {
  kill -9 $RUNPID 2>/dev/null
}

export JAVA_TOOL_OPTIONS='-Dnashorn.args=-scripting'
export METHOD=$2

socat - TCP-LISTEN:8080,crlf &
export RUNPID=$!
vertx run $1
