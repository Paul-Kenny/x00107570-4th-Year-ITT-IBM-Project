#!/bin/bash

#Run container in background
docker run -td $1

#Copy "find.sh" to container
docker cp find.sh $1:/find.sh

#Bash into container
docker exec -i -t <containerID> /bin/bash

#Run "find.sh" in container
sh find.sh 

#Copy "find.sh" to container
docker cp <containerID>:/find.log find.log

#Stop the docker container
docker stop <containerID>









