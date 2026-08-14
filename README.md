# Drink Sale application (Hexagonal Architecture example)

A small application written following Alistair Cockburn's Hexagonal architecture principles.

Besides the source code, you will find slides about Hexagonal Architecture history and concepts in the `docs` folder.
HTML slides published at https://corlaez.github.io/hexagonal-drink-sale/

To execute the system you can execute application's `run` gradle task.

* passing "console" as first arg triggers console mode
* passing "server" as first arg triggers http server mode (JSON API + HTML REST)
* passing no params will trigger either console or server randomly

IntelliJ should have access to preconfigured run configurations.

## Usage tips

### Server Mode's JSON API

The file `json-api.http` can be used directly if running in IntelliJ Ultimate edition.

If a license isn't available, that same file has the equivalent curl commands that can be used.

### Server Mode's HTML REST

Simply navigate to the index `/`. The route will be printed in console once the server turns on. 

### Console Mode

control+d is used to exit. If you are in the main function selection, it will exit the application.
If you are in the sell drink flow then control+d will only take you out of that flow.

control+c and Esc are configured to do the same. But IntelliJ's console hijacks these inputs.
If you want to try those you would have to create a jar using `shadowJar` and then execute it
in an external terminal such as bash.

## Application (includes Ports)

### Application Use Cases
* `drink/SellDrink`: getAllDrinkStock, validateReadyForSale, sell methods exposed
* `drink/ReportOnSales`: getAll method exposed

### Primary Ports
In this application primary ports are implicit (my choice, I am departing a bit from Alistair's advice here).
If we were to have them we would have 2 (one for each application use case).
Application classes are directly injected (via constructor) on primary adapters.

### Secondary Ports:
* `drink/secondaryports/ForProvidingClock`: Helps to remove now variability
* `drink/secondaryports/ForStoringDrinkStock`: Repository removing state variability
* `drink/secondaryports/ForStoringSale`: Repository removing state variability
* `drink/secondaryports/ForObtainingUUIDGenerators`: Helps to remove random variability

## Adapters

### Primary Adapters:
* `primaryadapters/api`: JSON HTTP API primary Adapter 
* `primaryadapters/console`: Console primary Adapter
* `primaryadapters/rest`: REST/HTML primary adapter (Hypermedia driven application using `htmx`)

### Secondary Adapters:
* `secondaryadapters/storing`: Adapter for repositories (in-memory and file based)
* `secondaryadapters/time`:  Adapter for clock provider (system and fixed clock provider)
* `secondaryadapters/random`: Contains a UUID generator (random and predictable UUID generator supplier adapter)

## Configurators

Configurators weren't originally described by Alistair but they were always necessary. In recent years Alistar
has named them 'configurators' and talks about them to provide more guidance on how hexagonal architecture
implementation.

We have 2 configurators for the entire application, other apps may use more, i.e. one per use case or one per module but this
application is small enough to have one configurator per application execution mode.

The execution modes and configurators are:

* `initConsoleApp`: Console mode configurator. Assembles the console primary adapters
* `initHttpServer`: Http Server mode configurator. Assembles JSON API and HTML REST primary adapters
