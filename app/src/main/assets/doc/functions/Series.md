## Series

```
Series(expr, {x, x0, n})
```

> create a power series of `expr` up to order `(x- x0)^n` at the point `x = x0`

### Examples

```
>> Series(f(x),{x,a,3})
f(a)+f'(a)*(-a+x)+1/2*f''(a)*(-a+x)^2+1/6*Derivative(3)[f][a]*(-a+x)^3+O(-a+x)^4
```
