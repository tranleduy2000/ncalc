## Control statements

Like most programming languages, Symja has common control statements for conditions, loops, etc.:

```
If(cond, pos, neg)
```

> returns `pos` if `cond` evaluates to `True`, and neg if it evaluates to `False`.

```
Which(cond1, expr1, cond2, expr2, ...)
```

> yields `expr1` if `cond1` evaluates to `True`, `expr2` if `cond2` evaluates to `True`, etc.

```
Do(expr, {i, max})
```

> evaluates `expr` `max` times, substituting `i` in `expr` with values from `1` to `max`.

```
For(start, test, incr, body)
```

> evaluates `start`, and then iteratively `body` and `incr` as long as `test` evaluates to `True`.

```
While(test, body)
```

> evaluates `body` as long as `test` evaluates to `True`.

```
Nest(f, expr, n)
```

> returns an expression with `f` applied `n` times to `expr`.

```
NestWhile(f, expr, test)
```

> applies a function `f` repeatedly on an expression `expr`, until applying `test` on the result no longer yields `True`.

```
FixedPoint(f, expr)
```

> starting with `expr`, repeatedly applies `f` until the result no longer changes.

```
>> If(2 < 3, a, b)
a

>> x = 3; Which(x < 2, a, x > 4, b, x < 5, c)
c
```

Compound statements can be entered with `;`. 
The result of a compound expression is its last part or `Null` if it ends with a `;`.
```
>> 1; 2; 3
3

>> 1; 2; 3;
```

Inside For, While, and Do loops, Break() exits the loop and Continue() continues to the next iteration.
```
>> For(i = 1, i <= 5, i++, If(i == 4, Break()); Print(i))
1
2
3
```

## Functions Reference
* [Abort](functions/Abort.md)
* [Block](functions/Block.md)
* [Break](functions/Break.md)
* [CompoundExpression](functions/CompoundExpression.md)
* [Continue](functions/Continue.md)
* [Do](functions/Do.md)
* [FixedPoint](functions/FixedPoint.md)
* [FixedPointList](functions/FixedPointList.md)
* [For](functions/For.md)
* [If](functions/If.md)
* [Nest](functions/Nest.md)
* [NestList](functions/NestList.md)
* [NestWhile](functions/NestWhile.md)
* [Return](functions/Return.md)
* [Switch](functions/Switch.md)
* [Which](functions/Which.md)
* [While](functions/While.md)