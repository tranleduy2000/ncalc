## Collect

```
Collect(expr, variable)
```

> collect subexpressions in `expr` which belong to the same `variable`.

```
Collect(expr, variable, head)
```

> collect subexpressions in `expr` which belong to the same `variable` and apply `head` on these subexpressions.

Collect additive terms of an expression.

This function collects additive terms of an expression with respect to a list of expression up to powers with rational exponents. By the term symbol here are meant arbitrary expressions, which can contain powers, products, sums etc. In other words symbol is a pattern which will be searched for in the expression's terms.

### Examples

```
>> Collect(a*x^2 + b*x^2 + a*x - b*x + c, x)
c+(a-b)*x+(a+b)*x^2

>> Collect(a*Exp(2*x) + b*Exp(2*x), Exp(2*x))
(a+b)*E^(2*x)

>> Collect(x^2 + y*x^2 + x*y + y + a*y, {x, y})
(1+a)*y+x*y+x^2*(1+y)
```