0. Install gpg:  https://gnupg.org/download/index.html

A - Generate Encrypted Properties
1. create unencrypted properties file (entity-client.properties)
2. encript file:
gpg --symmetric --cipher-algo AES256 entity-client.properties
3. when prompted provide encyption password
4. if successfull entity-client.properties.gpg file is created
4.1 delete entity-client.properties
4.2 save used encryption password in github secret (e.g. CLIENT_PROPS_PASS)

B - Use Encrypted Properties
1. Create & configure decrypt script (decrypt_config.sh)
1.1 on linux, ensure executions rights: chmod +x decrypt_config.sh
2. Ensure that the application is able to load properties from external file and that the decripted properties file matches the location from which the app is loading properties 
3. Update Github Workflow to run decrypt script (NOTE: secrets.CLIENT_PROPS_PASS is the name of the secret in Github secrets see Settings-> Secrets and Variables -> Actions -> Secrets)

 ....
 steps:
      - uses: actions/checkout@v5
	  ....
      - name: Decrypt configuration properties
        run: ./decrypt_config.sh
        env:
          SECRET_PROPS_PASSPHRASE: ${{ secrets.CLIENT_PROPS_PASS }}
	   .....  