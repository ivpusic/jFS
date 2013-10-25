jFS
===

File system simulator with basic UNIX commands

####How to run?

First you need set env variable FS_TYPE, with FAT or EXT4 value.

Example:

```
export FS_TYPE=EXT4
```

Then simply run ``jFS.jar`` from dist directory:

```
java -jar jFS.jar /path/to/jFS/root
```
