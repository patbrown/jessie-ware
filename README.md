<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="https://i.imgur.com/6wj0hh6.jpg" alt="Project logo"></a>
</p>

<h3 align="center">Jessie Ware</h3>

<div align="center">

  [![Status](https://img.shields.io/badge/status-active-success.svg)]() 
  [![GitHub Issues](https://img.shields.io/github/issues/patbrown/jessie-ware.svg)](https://github.com/patbrown/jessie-ware/issues)
  [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/patbrown/jessie-ware.svg)](https://github.com/patbrown/jessie-ware/pulls)
  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center"> JES: Just Enough Schema to leverage.
    <br> 
</p>

## 📝 Table of Contents
- [What is it?](#what)
- [Why does it exist?](#why)
- [How does it work?](#how)
- [How do I use it?](#usage)
- [Are there examples?](#examples)
- [Is it finished?](#todo)
- [How can I help?](#contribute)
- [Did you show gratitude?](#gratitude)

[![Clojars Project](https://img.shields.io/clojars/v/tools.drilling/jessie-ware.svg)](https://clojars.org/tools.drilling/jessie-ware)

## What is it? <a name = "what"></a>
Jessie Ware A.K.A `jes` provides a means to define attributes and datatypes using plain Clojure maps. It is extended via shortcodes (registered in a plain Clojure map) stored in memory as a Pathom smart map and leveraged via Pathom resolvers. This combination makes it very easy to extend attributes and datatypes into different contexts. It is JUST ENOUGH STRUCTURE to leverage - JES.
## Why does it exist? <a name = "why"></a>
This whole thing kicked off several years ago with a Conj talk by Antonio Andrade about building a data platform on top of Datomic. I was enamored with Datomic, but growing jaded with how often I felt lost in a sea of attributes. Datomic and other EAVT stores are far too granular. I've got good attribute definitions, but in order to really leverage these attributes for fun and profit I need them collected in a reasonable way.

After switching to current favorite XTDB this approach provided loads of power that's missing from a document store. I want to generate, validate, collect, infer, store, all that. I don't want to just fly by the seat of my pants and end up with a brittle system that I'm afraid to touch. Minimum sized attributes and datatypes do that for me. 

Pathom resolvers are hands down the best way to grow capabilities on top of known sources. Combining J.E.S. with a smart map and some write-as-needed resolvers really gave me some power these already powerful tools lack. 
## How does it work? <a name = "how"></a>
1. Hook up your shortcodes.
2. Build attributes using maps that contain 2 required keys (jes/spec, and jes/sc).
3. Build datatypes from attributes using maps that contains 1 required key (jes/req).
4. Scaffold specs and valuable information about your data via `jes-group` fn.
5. Put your jes-group into a Pathom registry
6. Write some resolvers for fun or profit.
7. Use your reg and resolvers via `sm->` fn to do powerful things.

## How do I use it? <a name = "usage"></a>
## Are there examples? <a name = "examples"></a>
## Is it finished? <a name = "todo"></a>
## How can I help? <a name = "contribute"></a>
## Did you show gratitude? <a name = "contribute"></a>
Antonio Andrade
