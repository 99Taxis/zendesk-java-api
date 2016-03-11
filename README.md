zendesk-java-api
================

[![Build Status](https://travis-ci.org/99taxis/zendesk-java-api.svg?branch=master)](https://travis-ci.org/99taxis/zendesk-java-api) [![Codacy Badge](https://api.codacy.com/project/badge/grade/6cddab4d424143e78b31f34935f16f45)](https://www.codacy.com/app/99taxis/zendesk-java-api) [![Coverage Status](https://coveralls.io/repos/github/99taxis/zendesk-java-api/badge.svg?branch=master)](https://coveralls.io/github/99taxis/zendesk-java-api?branch=master "Coveralls")

Zendesk Java API

[![License](http://img.shields.io/:license-MIT-blue.svg)](https://github.com/99taxis/zendesk-java-api/blob/master/LICENSE "MIT Licence")  [![GitHub tag](https://img.shields.io/github/tag/99taxis/zendesk-java-api.svg)](https://github.com/99taxis/zendesk-java-api/tags)

Publishing to 99Taxis' Maven Public
-----------------------------------

    git commit -a
    git push
    git tag vVERSION
    git push --tags
    mvn source:jar install -DupdateReleaseInfo=true -DcreateChecksum=true

    cd ../maven-public/
    cd releases/com/taxis99/zendesk-java-api/
    cp -a /home/miguel/.m2/repository/com/taxis99/zendesk-java-api/VERSION ./
    rm VERSION/_maven.repositories
    git add ./VERSION
    git commit
    git push


License
-------

`zendesk-java-api` is an open source software released under the MIT License.

See the [LICENSE](https://github.com/99taxis/zendesk-java-api/blob/master/LICENSE) file for details.
