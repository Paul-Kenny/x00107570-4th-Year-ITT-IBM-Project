#!/bin/bash

#Run container in background
docker run -td $1

#Export and search tar externally
docker export <containerID> | tar tf - | grep '.jar'












