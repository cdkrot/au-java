#!/bin/bash

set -e

for term in term3 term4
do
    echo -e "\033[92m[[Entering $term]]\033[00m"
    for task in $(ls "$term")
    do
        pushd . >/dev/null
        cd "$term/$task"
        if [ -x "build.sh" ]
        then
            echo -e "\033[92mBuilding $task [build.sh]\033[00m"
            ./build.sh
        elif [ -f "build.gradle" ]
        then
            echo -e "\033[92mBuilding $task [build.gradle]\033[00m"
            gradle build
        fi

        popd  >/dev/null
    done
done
