Pool Balance
------------
>Pool cleaning, measurement and chemical balancing app using Swing, ZIO and Scala 3.

Install
-------
1. brew install postgresql && brew services start postgresql

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

Measurements
------------
>Measured in ppm ( parts per million ).
1. total chlorine (tc = fc + cc): range = 0 - 10, good = 1 - 5, ideal = 3
2. free chlorine (fc): range = 0 - 10, good = 1 - 5, ideal = 3
3. combined chlorine (cc = tc - fc): range = 0 - 0.5, good = 0 - 0.2, ideal = 0
4. ph: range = 6.2 - 8.4, good = 7.2 - 7.6, ideal = 7.4
5. calcium hardness (ch): range = 0 - 1000, good = 250 - 500, ideal = 375
6. total alkalinity (ta): range = 0 - 240, good = 80 - 120, ideal = 100
7. cyanuric acid (cya): range = 0 - 300, good = 30 - 100, ideal = 50
8. total bromine (tb): range = 0 - 20, good = 2 - 10, ideal = 5
9. salt: range = 0 - 3600, good = 2,700-3,400, ideal = 3200
10. temperature: range = 50 - 100, good = 75 - 85, ideal = 82

Chemicals
---------
* Liquids measured in: gallons ( gl ) and liters ( l ).
* Granules measured in: pounds ( lbs ) and kilograms ( kg ).
1. LiquidChlorine ( gl/l )
2. Trichlor ( tablet )
3. Dichlor ( lbs/kg )
4. CalciumHypochlorite ( lbs/kg )
5. Stabilizer ( lbs/kg )
6. Algaecide ( gl/l )
7. MuriaticAcid ( gl/l )
8. Salt ( lbs/kg )

Entity
------
* Pool 1 --> * Cleaning | Measurement | Chemical

UI
--
1. Top: dashboard(total chlorine, free chlorine, ph, calcium hardness, total alkalinity, cyanuric acid, total bromine)
2. Left: pane(pools)
3. Center: tabbedpane(cleanings, measurements, chemicals)

Charts
------
* cleanings - line chart ( x = cleaned, y = ? )
* measurements - line chart ( x = measured, y = measurement )
* chemicals - bar chart ( x = added, y = amount/typeof )

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

Resources
---------
1. [Swing](http://www.java2s.com/Tutorials/Java/Java_Swing/index.htm)
4. [jDeploy](https://www.jdeploy.com/)

License
-------
> Copyright (c) [2022] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.