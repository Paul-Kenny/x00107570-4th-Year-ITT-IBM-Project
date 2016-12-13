#!/bin/bash

#Create tar.gz
#tar -cvzf Tars/$1Tar.tar.gz $1

#Change directory
#cd Tars

#List all jars in tar
#tar ztvf $1.tar.gz | grep ".jar"

#Will list and extract all jars from a tar.gz file
#arc=$1.tar.gz; file='*.jar'; 

#tar ztvf $arc | grep -E "$file" && tar zxvf $arc "$file"

#Create arc variable
arc=$1.tar;

#Create file variable
file='*.jar';

mkdir tartemp

#Create a tarball in a tartemp directory
tar -cvf tartemp/$arc $1

#cd to new directory
cd tartemp

#Will list and extract all jars from a tar file
tar tvf $arc | grep -E "$file" && tar xvf $arc "$file" | tee tarscan.log


