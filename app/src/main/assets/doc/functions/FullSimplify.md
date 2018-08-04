## FullSimplify 

```
FullSimplify(expr)
```

> works like `Simplify` but additionally tries some `FunctionExpand` rule transformations to simplify `expr`.

```
FullSimplify(expr, option1, option2, ...)
```

> full simplifies `expr` with some additional options set

* Assumptions - use assumptions to simplify the expression
* ComplexFunction - use this function to determine the "weight" of an expression.

### Examples

```
>> FullSimplify(Cos(n*ArcCos(x)) == ChebyshevT(n, x))
True
```

### Related terms 
[Simplify](Simplify.md) 