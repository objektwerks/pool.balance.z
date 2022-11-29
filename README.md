Pool Balance
------------
>Pool cleaning, measurement and chemical balancing app using Swing and Scala 3.

Show Stopper Errors
-------------------
>With Quill inline methods:
```
[error] -- Error: /Users/tripletail/workspace/pool.balance.z/server/src/main/scala/objektwerks/Handler.scala:4:68 
[error]  4 |  def handle(listPools: ListPools): PoolsListed = PoolsListed(store.listPools)
[error]    |                                                              ^^^^^^^^^^^^^^^
[error]    |                                       Could not summon a parser factory
[error]    |----------------------------------------------------------------------------
[error]    |Inline stack trace
[error]    |- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
[error]    |This location contains code that was inlined from Handler.scala:4
[error] 17 |  inline def listPools: List[Pool] = run( query[Pool] )
[error]    |                                          ^^^^^^^^^^^
[error]     ----------------------------------------------------------------------------
[error] -- Error: /Users/tripletail/workspace/pool.balance.z/server/src/main/scala/objektwerks/Handler.scala:7:47 
[error]   7 |    val id = if pool.id == 0 then store.addPool(pool)
[error]     |                                  ^^^^^^^^^^^^^^^^^^^
[error]     |      Cannot Find a 'scala.Long' Encoder of pool.id.asInstanceOf[Long]
[error]     |---------------------------------------------------------------------------
[error]     |Inline stack trace
[error]     |- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
[error]     |This location contains code that was inlined from ElaborateStructure.scala:542
[error]     |- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
[error]     |This location contains code that was inlined from ElaborateStructure.scala:542
[error]  12 |    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )
[error]     |                                  ^^^^^^^^^^
[error]      ---------------------------------------------------------------------------
[error] -- Error: /Users/tripletail/workspace/pool.balance.z/server/src/main/scala/objektwerks/Handler.scala:8:34 
[error]  8 |             else store.updatePool(pool)
[error]    |                  ^^^^^^^^^^^^^^^^^^^^^^
[error]    |                  Cannot Find a 'scala.Long' Encoder of pool.id
[error]    |----------------------------------------------------------------------------
[error]    |Inline stack trace
[error]    |- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
[error]    |This location contains code that was inlined from Handler.scala:10
[error] 10 |
[error]    |                    ^^^^^^^
[error]    |- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
[error]    |This location contains code that was inlined from Handler.scala:10
[error] 15 |    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )
[error]    |                                    ^^^^^^^^^^^^^
[error]     ----------------------------------------------------------------------------
[error] three errors found
[error] (server / Compile / compileIncremental) Compilation failed
[error] Total time: 10 s, completed Nov 29, 2022, 4:24:27 PM
```

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

Model
-----
* App 1 --> 1 Context | Store | Model | View
* Store 1 --> 1 Context
* Model 1 --> 1 Context | Store
* View 1 --> 1 Context | Model

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