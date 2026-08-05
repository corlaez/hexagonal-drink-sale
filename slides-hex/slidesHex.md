---
marp: true
theme: default
paginate: true
footer: '![w:50px](../img/armando.png)'
---
![bg w:300 right:45%](../img/me.jfif)
<!-- _class: invert -->
<!-- _footer: '![invert w:50px](../img/armando.png)' -->
<!-- _paginate: false -->
# Hexagonal Architecture: Origins and Context
A guide by Armando Cordova

@corlaez

---
# The idea in one sentence

> An application should be equally usable by a person, another program,
> or a test. And you should be able to run it in isolaiton without talking to 
> external systems (such as databases or the internet)

- The creator behind it: Alistair Cockburn

---
# Roadmap
1. **Origins:** the community and the pain that produced the idea (1986-2005)
2. **The guide:** scope, lingo, and the diagram
3. **Parallels:** the same shape in FP and in Wirfs-Brock's Object Design
4. **In practice:** what it buys you, and where people go wrong

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Part I : Origins

---
# The community that shaped it
- **1986:** First **OOPSLA** conference. **_The_ object-oriented conference** for years
- **1990:** _'Designing Object-Oriented Software'_ by Rebecca Wirfs-Brock
- **1995:** Ward Cunningham creates C2, the first Wiki
  - The online world's place to discuss software patterns and architectures
- **1997:** Wirfs-Brock mentors and reviews Alistair's _'Surviving Object-Oriented Projects'_
- **2001:** _'Writing Effective Use Cases'_ by Alistair. Remember this one
- **2002:** _'Object Design: Roles, Responsibilities, and Collaborations'_ by Wirfs-Brock

> Hexagonal was not made in one day. It was refined with a decade of conversation.

---
# The pain it answered
- **90s and 00s:** 3-tier architecture is dominant
  - Business logic coupled to the database and to the framework
  - You cannot run the app, nor a test, without the whole stack up
- **2002:** Automated acceptance testing enters the picture
  - **Fit** by Ward Cunningham: tests as HTML tables
  - **Fitnesse** by Micah Martin: a wiki and web server for Fit with ASCII tables

> If tests are to be first-class, the app has to be runnable **without** its devices.

---
# The architecture before the article
- **Smalltalk background:** instant feedback, fast experimentation, event-driven
  - With discipline, primary and secondary adapters are simple to add-or-swap
- **Network Illustrator** and **Faulty Disk Storage:**
  - "For adding events" (GUI, file, network), "For showing picture" (HTML, file)
  - Add a loopback (fake) to replace the faulty disk persistence
- **1994:** Alistair presents his architectural ideas at OOPSLA '94
  - The **_Design Patterns_** book debuted that same year
- **1998:** Shared with friends and peers
  - Moh Salim's Weather Warning System, XP practitioner Kevin Rutherford

---
![bg fit](../img/1994.png)

---
# How the 2005 article came to be
- c2 wiki pages: **Hexagonal Architecture** first, then **Ports and Adapters**
  - Pages were edited in place. Years of conversation survive, the revisions do not
- Alistair formalizes the ideas and writes the 2005 article, crediting:
  - **Wirfs-Brock's _Object Design_ (2002):** her Interfacer == Alistair's Transformer
  - **GoF _Design Patterns_:** the adapter pattern, and the source of the new name
  - **Kevin Rutherford:** opinion articles that motivated Alistair to write his own

<!-- Speaker note: 680 sounds and colors -->

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Part II : The guide

---
# Scope
## In scope
- The application runs without a UI or a database
  - Run it under automated tests, with no GUI
  - Run it when the database (or the network) is unavailable

## Out of scope
- File names, folder structure, layer counts
- Details about how to structure your domain

> Your app can run with and without UI and network with a config change. Done

---
# The asterisk: Alistair thinks in use cases
- The man wrote _'Writing Effective Use Cases'_. The bias is not subtle
- The 2005 article has a section on use cases and the application boundary
  - Write them at the inner hexagon without technology coupling
- **Primary** and **secondary** are borrowed from primary and secondary **actors**
  - Which side you draw a port on follows the use case context diagram
- Much later and after being pressed he has shared his advice for naming ports:
`ForPlacingOrders` (primary), `ForStoringUsers` (secondary)
  - But you don't need to follow that naming, it's optional

---
# What about ports
- 1 use case == 1 primary port (explicit or implicit)
- A single secondary port? Don't mix multiple technologies into a single port
- Are ports exclusive to a use case? No, port explosion
- The port is a purposeful conversation, not a method (A bunch of methods)
- Original article talks about few ports (2, 3, 4)
    - He counts all DB communication as a single port, not individual repositories
    - It is about external actors to the system, not necessarily classes or objects

> Use cases specify what the app does. Ports group who it talks to.

---
# Why the hexagon?
![bg w:400 right:44%](../img/alistair.png)
- There is nothing inherently "6-sided"
- Alistair chose the figure for illustration
- Many facets $\rightarrow$ many ports
- **"Ports & Adapters"** is his preferred name for the architecture
- The hexagon name endured anyway

---
# Lingo: primary (driver) vs. secondary (driven)
- **Driving / primary side:** triggers that invoke the app
  - **Adapter:** translates external input (HTTP, CLI, Kafka) into core commands
  - **Port:** the API the core exposes *(could be implicit)*
- **Driven / secondary side:** invoked by the app
  - **Port:** interface for a needed capability (save, notify, charge)
  - **Adapter:** concrete implementation (Postgres, Stripe, in-memory fake)
- **Application ("inside the hexagon"):** use cases, domain model

> The primary side calls the app. The app calls the secondary side.
> The app is the foundation, the adapters depend on the App

---
![bg fit](../img/diagram.png)

---
# Why are primary ports optional?
- A primary port has exactly one real implementation: the application itself
- The port is the public contract. An explicit interface is a separate choice
- Reasons to declare one anyway: it lets you write a fake application
- Reasons to skip it: mock libraries fake concrete types cleanly
- Some reject faking the application at all
  - For them primary adapter tests also tests the application

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Part III : The parallels

---
# FP & 'Functional core, imperative shell' parallels
- **Primary adapters** $\rightarrow$ translate triggers into input commands
- **Primary ports** $\rightarrow$ public API function types ($f: Input \to Output$)
- **Application core** $\rightarrow$ functional core (pure, no side effects)
- **Secondary ports** --> algebraic effects / capability contracts
- **Secondary adapters** --> side effects & effect interpreters (I/O runtime)

> The secondary side is where the two differ.
> The FP core returns effects, to be executed later and outside the application.

---
# Wirfs-Brock's 'Object Design' parallels
- **Primary adapters** $\rightarrow$ Interfacer
- **Primary ports** $\rightarrow$ public API (optional)
- **Application core** $\rightarrow$ perhaps the Collaborator (Alistair leaves this undefined)
- **Secondary ports & adapters** $\rightarrow$ Interfacer

> The vocabulary is new. The decomposition is not.

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Part IV : In practice

---
# What it buys you, where people go wrong
**What you get**
- A test suite that runs with no database, no network, no browser
- Swapping Postgres for an in-memory fake is a wiring change, not a rewrite

**Where people go wrong**
- Leaking framework types or side effects into the application core
- Thinking it dictates folder structure or naming
- Assuming it mandates DDD, CQRS, or primary ports as explicit interfaces
- Generic services instead of use case classes. Alistair leans this way, I go further // WHAT TO DO WITH THIS?

---
# Some useful links
- Earliest C2 page: [wiki.c2.com/?HexagonalArchitecture](https://wiki.c2.com/?HexagonalArchitecture)
- Newer C2 page: [wiki.c2.com/?PortsAndAdaptersArchitecture](https://wiki.c2.com/?PortsAndAdaptersArchitecture)
- Rutherford's blog, [2006 archive snapshot](https://web.archive.org/web/20060209233610/http://silkandspinach.net/blog/archives.html)
- 2005 article, [snapshot with comments](https://web.archive.org/web/20140329201018/http://alistair.cockburn.us/Hexagonal+architecture)
- 2005 article, no comments: [alistair.cockburn.us/hexagonal-architecture](https://alistair.cockburn.us/hexagonal-architecture)
- 2024 book: _Hexagonal Architecture Explained_, Cockburn & Garrido de Paz

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Thanks
Github: @corlaez
![bg auto](../img/armando.png)