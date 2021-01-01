GDL Workbench - IDE for GNU Data Language
=========================================

An IDE for GDL (GNU Data Language). GDL is a free IDL (Interactive Data Language)
compatible incremental compiler (capable of running programs written in IDL/GDL).
IDL is a registered trademark of ITT Visual Information Solutions 
(see: http://www.ittvis.com).


HOMEPAGE
--------
https://gnudatalanguage.github.io/


FEATURES
--------

- Basic IDE functionality (Projects Explorer, Console, Wizards, etc.) thanks to Eclipse RCP
- GDL integrated console / command line support
- Compile / Run script from menu and toolbar
- Syntax coloring


COMPILE
-------

With the JDK (Java Development Kit, http://openjdk.java.net) version equal or higher than 11
installed, type the following at the top source directory:

```shell
$ ./mvnw clean verify
```

Then the compiled binaries will be generated at `product/target/products/gdlde.product/`.


LICENSE
-------

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

(It should be included in this package in the file COPYING)
