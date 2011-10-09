# clj-rpc

a leiningen plugin for [clj-rpc](https://github.com/zhuangxm/clj-rpc)
to generate client stub code.

## Usage 

in project.clj add

````clojure
:dev-dependencies [[lein-clj-rpc "0.1.0-SNAPSHOT"]]
````

command:

````bash
lein gen-stub namespace [serveraddress serverport]
````
serveraddress and serverport can be optional
the default values are 127.0.0.1 and 8080

example:

```bash
lein gen-stub com.basecity.api 127.0.0.1 8080
```
this example will generate a file name api.clj under the
src/com/basecity/ directory


## License

Copyright (C) 2011 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

