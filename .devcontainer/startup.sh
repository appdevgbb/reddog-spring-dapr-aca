#!/bin/bash
dapr uninstall \
&& dapr init --slim
git submodule update --init --recursive
cd ./src/ui && yarn install