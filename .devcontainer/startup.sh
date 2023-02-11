#!/bin/bash
curl -k https://cosmos:8081/_explorer/emulator.pem > ~/emulatorcert.crt
sudo cp ~/emulatorcert.crt /usr/local/share/ca-certificates/
sudo update-ca-certificates
sudo $JAVA_HOME/bin/keytool -noprompt -storepass changeit -keypass changeit -keystore -cacerts -delete -alias cosmos_emulator
sudo $JAVA_HOME/bin/keytool -noprompt -storepass changeit -keypass changeit -keystore -cacerts -importcert -alias cosmos_emulator -file ~/emulatorcert.crt \
&& dapr uninstall \
&& dapr init --slim