# clj-rpc

a leiningen plugin for [clj-rpc](https://github.com/zhuangxm/clj-rpc)
to generate client stub code.

## Usage 

in project.clj add :dev-dependencies [[lein-clj-rpc "0.1.0-SNAPSHOT"]]

command: lein gen-stub namespace [serveraddress serverport]
namespace like a.b.c will generate a file c.clj in src/a/b 

## License

Copyright (C) 2011 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

