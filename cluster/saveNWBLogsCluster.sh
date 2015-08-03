#!/bin/sh
#Script for copy NWB logs

echo "Copy NWB log files to backup folder"

#Create folder for NWB logs
DATE=$(date +%H:%M:%S)
mkdir NWB_LOGS

PEER=0
NUM_PEER=10

#Copy NWB File to Backup folder
while [ $PEER != $NUM_PEER ]
do
	cp ${HOSTNAME}Peer${PEER}/ChordNWB.txt NWB_LOGS/${HOSTNAME}_NWB_BACK_PEER${PEER}.txt 
	
	let PEER=$PEER+1
done

cd NWB_LOGS
ls *NWB_BACK*.txt > configNWBFile.txt
cd ..