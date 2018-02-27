## Scoping

By default, all symbols are “global” in Symja, i.e. they can be read and written in any part of your program.
However, sometimes “local” variables are needed in order not to disturb the global namespace. 
Symja provides two ways to support this:

* lexical scoping by `Module`, and
* dynamic scoping by `Block`.

```
Module({vars}, expr)
```

> localizes variables by giving them a temporary name of the form `name$number`, where number is the current internal Symja value of "module number". Each time a module is evaluated, "module number" is incremented.

```
Block({vars}, expr)
```

> temporarily stores the definitions of certain variables, evaluates `expr` with reset values and restores the original definitions afterwards.

Both scoping constructs shield inner variables from affecting outer ones:
```
>> t = 3
3

>> Module({t}, t = 2)
2

>> Block({t}, t = 2)
2

>> t
3
```

`Module` creates new variables:
```
>> y = x ^ 3;
>> Module({x = 2}, x * y)
2*x^3
```

`Block` does not:
```
>> Block({x = 2}, x * y)
16
```

Thus, `Block` can be used to temporarily assign a value to a variable:
```
>> expr = x ^ 2 + x;
>> Block({x = 3}, expr)
12

>> x
x
```

It is common to use scoping constructs for function definitions with local variables:
```
>> fac(n_) := Module({k, p}, p = 1; For(k = 1, k <= n, ++k, p *= k); p)
>> fac(10)
3628800

>> 10!
3628800
```
