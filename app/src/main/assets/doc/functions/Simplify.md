## Simplify 

```
Simplify(expr)
```

> simplifies `expr`

```
Simplify(expr, option1, option2, ...)
```

> simplify `expr` with some additional options set

* Assumptions - use assumptions to simplify the expression
* ComplexFunction - use this function to determine the "weight" of an expression.

### Examples

```
>> Simplify(1/2*(2*x+2))
x+1

>> Simplify(2*Sin(x)^2 + 2*Cos(x)^2)
2

>> Simplify(x)
x

>> Simplify(f(x))
f(x)

>> Simplify(a*x^2+b*x^2)
(a+b)*x^2
```

Simplify with an assumption:
```
>> Simplify(Sqrt(x^2), Assumptions -> x>0)
x
```

For `Assumptions` you can define the assumption directly as second argument:
```
>> Simplify(Sqrt(x^2), x>0)
x
```

```
>> Simplify(Abs(x), x<0)
Abs(x)
```

With this "complexity function" the `Abs` expression gets a "heavier weight".
```
>> complexity(x_) := 2*Count(x, _Abs, {0, 10}) + LeafCount(x)

>> Simplify(Abs(x), x<0, ComplexityFunction->complexity)
-x
```