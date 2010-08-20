#!/bin/bash

if [ ! -d bin ]
then
 cat<<EOF>&2
Error requires compile.
EOF
 exit 1
fi

cp src/*.xtm bin
cd bin
1>/dev/null unzip ../../../trunk/hapax-2.3.3.jar
rm -rf META-INF
jar cmf ../src/Manifest.mf ../HapaxTest.jar * 
cd ..
rm -rf bin
