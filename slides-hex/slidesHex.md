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

---
# Preamble
- **1986:** First **OOPSLA** annual conference. **_The_ object-oriented conference** for years
- **1990:** _'Designing Object-Oriented Software'_ by Rebbeca Wirf-Brock
- **1995:** Ward Cunningham creates the first Wiki
    - The online world's place to discuss software patterns and architectures
- **1997:** Rebecca mentors and reviews _'Surviving Object-Oriented Projects'_ by Alistair
- **90s 00s:** 3-tier architecture was dominant. DB and frameworks highly coupled
- **2002:** 
  - Fit, revolutionary acceptance test tool by Ward Cunningham. HTML tables
    - Fitnesse by Micah Martin. A wiki web server for Fit. ASCII tables
  - _'Object Design: Roles, Responsibilities, and Collaborations'_ by Rebecca

---
# The architecture before the article
- **Smalltalk background:** Instant feedback loop, fast experimentation, event-driven
  - With discipline, primary and secondary actors can be simple add or swap
- **Network Illustrator** and **Faulty Disk Storage**:
  - "For adding Events" (gui, file, network), "For Showing picture" (html, file)
  -  Add a loopback (fake) to replace the faulty disk persistence
- **1994:** Alistair presents his architectural ideas in OOPSLA '94
  - **OOPSLA:** _The_ Object-Oriented conference of the 90s
  (**_Design Patterns_** book debuted that same year)
- **1998:** Hexagonal Architecture shared with friends and peers
  - Mho Salim's Weather Warning System and XP practicioner Kevin Rutherford

--- 
# How the 2005 article came to be
- c2 wiki pages: Hexagonal Architecture first, then Ports and Adapters
- Alistair formalizes his ideas and writes the 2005 article crediting:
  - Rebecca's 2002 Object Design
    - The interfacer is practically the same as Alistair's transformer
  - GoF Design Patterns
    - The adapter pattern: A pattern used and inspiration for the new name
  - Kevin Rutherford
    - Wrote opinion articles the motivated Alistair to write his own

[//]: # (680 sounds and colors)

---
![bg fit](../img/1994.png)

---
![bg w:400 right:50%](../img/alistair.png)
<!-- _class: invert -->
# History lesson is over. Hexagonal guide

---
# In Scope
- Application can run without UI or a database
  - Allow app to run with automated tests (no GUI) 
  - Make it possible to work when the database (or network) is not availabe
# Out of Scope
- Prescribe how to organize the business logic
- Mandate specific file names or folder structure
---
# Why the Hexagonal?
![bg w:400 right:33%](../img/alistair.png)
- There is nothing inherently "6-sided"
- Alistair chose this figure for illustration purposes
- Many facets $\rightarrow$ many ports
- Alistair's preferred name is "Ports & Adapters"
- However, the Hexagon name endured

---
# Lingo: Primary (Driver) vs. Secondary (Driven)
- **Driving / Primary Side** (Triggers that invoke the App's methods)
  - **Adapter:** Translates external input (HTTP, CLI, Kafka) into core commands
  - **Port:** The API interface exposed by the core *(optional)*
- **Driven / Secondary Side** (Triggered by the App)
  - **Port:** Interface defining required external capabilities (e.g., `UserRepository`)
  - **Adapter:** Concrete implementations (Postgres, Stripe, In-Memory Fakes)
- **Application ('Inside the Hexagon'):** Use Cases, domain model

---
# Why optional Primary ports?
- Primary ports have only one real implementation: the application
- The Port is the public application contract. An explicit interface is optional
- Interfaces allow defining application fakes
- Mock libraries allow defining fakes cleanly even without interface
- Some reject faking the application and test primary side with real application

---
![](../img/diagram.png)

---
# FP & 'Functional core, Imperative Shell' Parallels
- **Primary Adapters** $\rightarrow$ Translates Triggers into Input Commands
- **Primary Ports** $\rightarrow$ Public API Function Types ($f: Input \to Output$)
- **Application Core** $\rightarrow$ Functional Core (Pure functions, no side-effects)
- **Secondary Ports** $\rightarrow$ Algebraic Effects / Capability Contracts
- **Secondary Adapters** $\rightarrow$ Side Effects & Effect Interpreters (I/O Runtime)

---
# 'Object Design' Parallels
- **Primary Adapters** $\rightarrow$ Interfacer
- **Primary Ports** $\rightarrow$ Public API (optional)
- **Application Core** $\rightarrow$ Perhaps the Collaborator but Alistar really leaves this blank
- **Secondary Ports & Adapters** $\rightarrow$ Interfacer

---
# Some Useful Links:
- Earliest C2 page ([wiki.c2.com/?HexagonalArchitecture](https://wiki.c2.com/?HexagonalArchitecture))
- Newer C2 page ([wiki.c2.com/?PortsAndAdaptersArchitecture](https://wiki.c2.com/?PortsAndAdaptersArchitecture))
- Formal article snapshot (with comments) ([2005 Internet Archive Snapshot](https://web.archive.org/web/20140329201018/http://alistair.cockburn.us/Hexagonal+architecture))
- Active link to Formal article: [alistair.cockburn.us/hexagonal-architecture](https://alistair.cockburn.us/hexagonal-architecture)

---
<!-- _class: invert -->
<!-- _footer: '' -->
# Thanks
Github: @corlaez
![bg auto](../img/armando.png)
