#!/bin/sh

#generate number from range 0 and 32767
#avarage number length is 4.8 (5)
#1 Gb = 1073741824 byte ~ 185127900 number

file_out=text1.txt
rm $file_out
c=0
date +"%m/%d/%Y %H:%M:%S"
while [ $c -lt 185127900 ]
do  
  echo -n "$RANDOM;">>$file_out
  c=`expr $c + 1`
done
date +"%m/%d/%Y %H:%M:%S"
