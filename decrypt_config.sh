#!/bin/sh

# Decrypt the file
#NOTE: execution permissions must be ensured before commiting to github
mkdir -p /opt/app/config
# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$SECRET_PROPS_PASSPHRASE" \
--output /opt/app/config/entity-client.properties entity-client.properties.gpg