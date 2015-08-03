#!/bin/sh
#Script for lunch multiple peer on same node
SERVER_PORT=1235
SERVER_ADDR=160.78.27.134
NUM_PEER=4
PORT_SPACE=10
PEER=0
PORT=2345

echo "Create a directory for every peer on cluster..."
echo "Copy JAR on peer directory..."
echo "Remove old log and txt"
while [ $PEER != $NUM_PEER ]
do
  mkdir ${HOSTNAME}Peer${PEER}
  cp clusterPeer.jar ${HOSTNAME}Peer${PEER}
  rm ${HOSTNAME}Peer${PEER}/${HOSTNAME}_Log_Peer${PEER}.log
  rm ${HOSTNAME}Peer${PEER}/*.txt
  
  let PEER=$PEER+1
done

echo "Lunching peer..."
PEER=0
while [ $PEER != $NUM_PEER ]
do
	cd ${HOSTNAME}Peer${PEER}
	java -jar clusterPeer.jar $PORT $SERVER_ADDR $SERVER_PORT > ${HOSTNAME}_Log_Peer${PEER}.log & 
	cd .. 
	
	let PEER=$PEER+1
	let PORT=$PORT+$PORT_SPACE
done