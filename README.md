# play-seed.g8
A template project for quickly creating [Playframework](https://www.playframework.com/) applications.
The project relies on the [sbt-scaffold-play](https://github.com/dnvriend/sbt-scaffold-play) plugin that
is automatically installed in this project. 

## Usage
You need sbt v0.13.13 or higher:

- Create a new project by typing: `sbt new dnvriend/play-seed.g8`
- launch sbt

## Workflow
I have two terminals running, both running the same sbt project but the first terminal runs the play application
and the second terminal scaffolds features in your play application

- Launch `play application` with `sbt run`
- Launch the `sbt terminal` with `sbt`

## Enabling features
You can enable play features with the __enable__ command for example:

```bash
[play-seed] $ enable swagger
[info] Enable complete
```

This will install swagger in your project.

You can also type `enable all` to get _scalariform, sbtheader, buildinfo, fp, json, loggng, anorm, akka, circuitbreaker and swagger_.

Please see the [sbt-scaffold-play](https://github.com/dnvriend/sbt-scaffold-play) website for available enablers.

## Scaffolding
You can scaffold (quickly create basic working functionality which you then alter to fit your needs) using the __scaffold__
command for example:

```bash
[play-seed] $ scaffold crud
[crud-controller] Enter component name > people
[crud-controller] Enter REST resource name > people
Enter entityName > Person
Person(): Enter field Name > name
Person(): Enter field type > str
Person(): Another field ? > y
Person(name: String): Enter field Name > age
Person(name: String): Enter field type > int
Person(name: String): Another field ? > n
[info] Scaffold complete
```

The scaffold is available for use.

Please see the [sbt-scaffold-play](https://github.com/dnvriend/sbt-scaffold-play) for available scaffolds.