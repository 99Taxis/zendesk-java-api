
Publishing to 99Taxis' Maven Public
-----------------------------------

    git commit -a
    git push
    git tag vVERSION
    git push --tags
    mvn source:jar install -DupdateReleaseInfo=true -DcreateChecksum=true

    cd ../maven-public/
    cd releases/com/taxis99/zendesk-java-api/
    cp -a ~/.m2/repository/com/taxis99/zendesk-java-api/VERSION ./
    rm VERSION/_maven.repositories
    git add ./VERSION
    git commit
    git push