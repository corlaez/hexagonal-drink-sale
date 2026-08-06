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
> or a test. And you should be able to run it with the internet and the DB down

- The creator behind it: Alistair Cockburn

---
# Roadmap
1. **Origins:** the community and the pain that produced the idea (1986-2005)
2. **A short guide:** scope, lingo, and a diagram
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
  - Mho Salim's Weather Warning System, XP practitioner Kevin Rutherford

---
![bg fit](../img/1994.png)

---
# How the 2005 article came to be
- Early 00s C2 Wiki pages: **Hexagonal Architecture** first, then **Ports and Adapters**
  - Pages were edited in place. Years of conversation survive, the revisions do not
- Alistair formalizes the ideas and writes the 2005 article, crediting:
  - **Wirfs-Brock's _Object Design_ (2002):** her Interfacer == Alistair's Transformer
  - **GoF _Design Patterns_:** the adapter pattern, used directly and helped name it
  - **Kevin Rutherford:** wrote articles that motivated Alistair to write his official doc

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Part II : A short guide

---
# Scope
## In scope
- The application runs without a UI or a database
  - Run it under automated tests, with no GUI
  - Run it when the database (or the network) is unavailable

## Out of scope
- File names, folder structure, layer counts
- Details about how to structure your domain

---
# The asterisk: Alistair and the use cases
- A lot of Alistair's work through the years talk about use cases
- The 2005 article has a section on use cases and the application boundary
  - They should not have any technology dependency
- However, as time went on I find Alistair mentions use cases less and less
- He seems much more interested on guidance for naming ports (interfaces) lately:
  - `ForPlacingOrders` (primary), `ForStoringUsers` (secondary).
  - Essentially: name the purpose, never the technology

---
# What about Primary Ports
- In languages like ruby you don't need an interface
- In languages such as Java, Alistair's advice is to write an interface
    - However, many practitioners will skip primary interface
        - Mock libraries allows faking without an interface declared
        - 

---
# What about ports
- A port is a purposeful conversation, not a method. Expect several methods in one
- Every use case is reachable through a primary port, but not one port each
  - A port per use case is an extreme he rejects. So is one port per side
- Many technologies share one port. GUI, CLI and tests drive the same one
- Group by external actor, not by class. All the DB chatter is usually one port
  - His own Blue Zone still splits rates (file) from tickets (database)
- The article says two to four ports. Blue Zone, his 2023 example, has six

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
  - His 2023 Ruby example declares none. The Java twin declares both sides
- Reasons to declare one anyway: it lets you write a fake application
- Reasons to skip it: mock libraries fake concrete types cleanly
- Some reject faking the application at all
  - For them primary adapter tests also tests the application

---
# His framing kept moving, away from use cases
- **2015:** driving and driven adapters, plus the `For_doing_something` naming
- **2022:** the pattern restated as a special case of Component + Strategy
- **2023:** the app is a component with provided and required interfaces
  - The configurator (composition root) gets its own section: injection vs lookup
  - Costs and benefits listed like a trade study, not a requirements method
- **2024:** the book, with Juan Manuel Garrido de Paz

> His 2023 talk never says "use case"

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

---
# Some extra notes
- 2005 article is a bit dated by now
    - I would advise checking his 2023 talk or getting his 2025 book
- I personally think that if you are not doing Jakub's approach you can omit it
  - You can still run the application through multiple primary adapters w/o them
- Jakub Nabrdalik goes a step futher 
  - Defines primary port explicitly with an interface
  - Create clients (programatic actors) for each adapter. They implement the primary port interface
  - Test the interface & execute with the applications and primary adapters

---
# Some useful links
- C2 pages: [HexagonalArchitecture](https://wiki.c2.com/?HexagonalArchitecture) & [PortsAndAdaptersArchitecture](https://wiki.c2.com/?PortsAndAdaptersArchitecture)
- Rutherford's blog: [snapshot of silkandspinach.net/blob/archives](https://web.archive.org/web/20060209233610/http://silkandspinach.net/blog/archives.html)
- 2005 article: [snapshot with comments](https://web.archive.org/web/20140329201018/http://alistair.cockburn.us/Hexagonal+architecture) & [current website (no comments)](https://alistair.cockburn.us/hexagonal-architecture)
- 2017 Talk by Alistair: [Inside the Hexagon](https://www.youtube.com/watch?v=th4AgBcrEHA)
- 2020 [Interview with Alistair Cockburn](https://jmgarridopaz.github.io/content/interviewalistair.html) by Garrido de Paz 
- 2023 Talk by Alistair: [Hexagonal Architecture from its Inventor](https://www.youtube.com/watch?v=Gsgisj1Ns40)
- 2024-2025 book: [_Hexagonal Architecture Explained_](https://www.amazon.com/Hexagonal-Architecture-Explained-architecture-simplifies/dp/B0F5QSH28F/ref=pd_lpo_d_sccl_1/134-2074605-9554937?psc=1), Cockburn & Garrido de Paz
    - 2024 preview version, 2025 updated first edition
    - In memory of Juan Manuel Garrido de Paz

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Thanks
Github: @corlaez
![bg auto](../img/armando.png)