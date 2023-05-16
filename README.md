Pool Balance
------------
>Pool cleaning, measurement and chemical balancing distributed app using Swing, ScalaFX, ZIO, Postgresql and Scala 3.

Warning
-------
>As of 2023.5.16, App will not start: **java.lang.RuntimeException: No main class detected**.
> So **ZIOAppDefault** does not appear to work as intended. Adding a **main** method does not help.

>Consequently, the current integration test **will not** pass! Yet it can be **rewritten** to **directly** invoke the **handler**.

ZIO Quill
---------
>Is ZIO Quill ***production ready***? See these open issues: https://github.com/zio/zio-quill/issues

>The current steward of ZIO Quill is a part-time, one-person team, whose priorities are ***not always***
>aligned with those of the ZIO Quill community.

>Consequently ZIO Quill users have had to consider transitioning to other persistence libraries
>as a risk mitigation strategy.

>Alternative ZIO database libraries include:
1. [ZIO Schema](https://zio.dev/version-1.x/resources/ecosystem/officials/#zio-schema)
2. [TranzactIO](https://zio.dev/version-1.x/resources/ecosystem/community#tranzactio)
3. [ZIO SQL](https://github.com/zio/zio-sql)

Todo
----
>The following showstopper bugs have been fixed as of 2023.2.16:
1. github.com/zio/zio-protoquill/issues/217
2. github.com/zio/zio-protoquill/pull/242
>Continue with:
1. run and pass server integration test
2. test and refactor client as required

Install
-------
1. brew install postgresql
2. brew services start postgresql

Build
-----
1. sbt clean compile

Test
----
1. sbt clean test

Server Run
----------
1. sbt ( interactive session )
2. project server
3. run

Client Run
----------
1. sbt ( interactive session )
2. project client
3. run

Package Server
--------------
1. sbt ( interactive session )
2. project client
3. test
4. universal:packageBin

Package Client
--------------
1. sbt ( interactive session )
2. project server
3. test
4. universal:packageBin

Deploy
------
>Consider these options:
1. [jDeploy](https://www.npmjs.com/package/jdeploy)
2. [Conveyor](https://hydraulic.software/index.html)

Features
--------
1. dashboard
2. cleanings
3. measurements
4. chemicals
5. charts
6. database

Use Cases
---------
1. clean pool and components
2. measure pool chemical content
3. add chemicals to pool

Model
-----
* Pool 1 --> * Cleaning | Measurement | Chemical

Measurements
------------
>Measured in ppm ( parts per million ).

| Measurement                       | Range       | Good        | Ideal |
| --------------------------------- | ----------- | ----------- | ----- |
| total chlorine (tc = fc + cc)     | 0 - 10      | 1 - 5       | 3     |
| free chlorine (fc)                | 0 - 10      | 1 - 5       | 3     |
| combinded chlorine (cc = tc - fc) | 0.0 - 0.5   | 0.0 - 0.2   | 0.0   |
| ph                                | 6.2 - 8.4   | 7.2 - 7.6   | 7.4   |
| calcium hardness                  | 0 - 1000    | 250 - 500   | 375   |
| total alkalinity                  | 0 - 240     | 80 - 120    | 100   |
| cyanuric acid                     | 0 - 300     | 30 - 100    | 50    |
| total bromine                     | 0 - 20      | 2 - 10      | 5     |
| salt                              | 0 - 3600    | 2700 - 3400 | 3200  |
| temperature                       | 50 - 100    | 75 - 85     | 82    |

Chemicals
---------
* Liquids measured in **gallons** ( gl ) and **liters** ( l ).
* Granules measured in **pounds** ( lbs ) and **kilograms** ( kg ).
1. LiquidChlorine ( gl/l )
2. Trichlor ( tablet )
3. Dichlor ( lbs/kg )
4. CalciumHypochlorite ( lbs/kg )
5. Stabilizer ( lbs/kg )
6. Algaecide ( gl/l )
7. MuriaticAcid ( gl/l )
8. Salt ( lbs/kg )

Postgresql
----------
1. config:
    1. on osx intel: /usr/local/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
    2. on osx m1: /opt/homebrew/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
2. build.sbt:
    1. IntegrationTest / javaOptions += "-Dquill.binds.log=true"
3. run:
    1. brew services start postgresql
4. logs:
    1. on osx intel: /usr/local/var/log/postgres.log
    2. on m1: /opt/homebrew/var/log/postgres.log

Database
--------
>Example database url: postgresql://localhost:5432/poolbalance?user=mycomputername&password=poolbalance"
1. psql postgres
2. CREATE DATABASE poolbalance OWNER [your computer name];
3. GRANT ALL PRIVILEGES ON DATABASE poolbalance TO [your computer name];
4. \l
5. \q
6. psql poolbalance
7. \i ddl.sql
8. \q

DDL
---
>Alternatively run: psql -d poolbalance -f ddl.sql
1. psql poolbalance
2. \i ddl.sql
3. \q

Drop
----
1. psql postgres
2. drop database poolbalance;
3. \q

Environment
-----------
>The following environment variables must be defined:
* export POOL_BALANCE_HOST="127.0.0.1"
* export POOL_BALANCE_PORT=7272

* export POOL_BALANCE_POSTGRESQL_URL="jdbc:postgresql://localhost:5432/poolbalance"
* export POOL_BALANCE_POSTGRESQL_USER="yourusername"
* export POOL_BALANCE_POSTGRESQL_PASSWORD="poolbalance"
* export POOL_BALANCE_POSTGRESQL_DRIVER="org.postgresql.ds.PGSimpleDataSource"
* export POOL_BALANCE_POSTGRESQL_DB_NAME="poolbalance"
* export POOL_BALANCE_POSTGRESQL_HOST="127.0.0.1"
* export POOL_BALANCE_POSTGRESQL_PORT=5432

* export POOL_BALANCE_EMAIL_HOST="your-email.provider.com"
* export POOL_BALANCE_EMAIL_ADDRESS="your-email@provider.com"
* export POOL_BALANCE_EMAIL_PASSWORD="your-email-password"

Resources
---------
1. [Swing](http://www.java2s.com/Tutorials/Java/Java_Swing/index.htm)
4. [jDeploy](https://www.jdeploy.com/)

License
-------
> Copyright (c) [2022, 2023] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
