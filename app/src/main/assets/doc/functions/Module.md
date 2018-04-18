## Module

```
Module({list_of_local_variables}, expr )
```

> evaluates `expr` for the `list_of_local_variables` by renaming local variables.

### Examples

Print `11` to the console and return `10`:

```
>> xm=10;Module({xm=xm}, xm=xm+1;Print(xm));xm
10
```