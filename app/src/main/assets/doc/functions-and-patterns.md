## Functions and patterns

Functions can be defined in the following way:
```
>> f(x_) := x ^ 2
```

This tells Symja to replace every occurrence of `f` with one (arbitrary) parameter `x` with `x ^ 2`.
```
>> f(3)
9

>> f(a)
a^2
```

The definition of f does not specify anything for two parameters, so any such call will stay unevaluated:
```
>> f(1, 2)
f(1,2)
```

In fact, functions in Symja are just one aspect of patterns: 
`f(x_)` is a pattern that matches expressions like `f(3)` and `f(a)`. 
The following patterns are available:

```
_
```
> matches one expression.

```
x_  
```
> matches one expression and stores it in `x`.

```
__ 
```
> matches a sequence of one or more expressions.

```
___
```
> matches a sequence of zero or more expressions.

```
_h
```
> matches one expression with head `h`.

```
x_h 
```
> matches one expression with head `h` and stores it in `x`.

```
p | q
```

or

```
Alternatives(p, q)
```
> matches either pattern `p` or `q`.
	
```
p ? t
```

or

```
PatternTest(p, t)
```
> matches `p` if the test `t(p)` yields `True`.

```
p /; c
```

or 

```
Condition(p, c)
```
> matches `p` if condition `c` holds.

As before, patterns can be used to define functions:
```
>> g(s___) := Plus(s) ^ 2

>> g(1, 2, 3)
36
```

`MatchQ(e, p)` tests whether `e` matches `p`:
```
>> MatchQ(a + b, x_ + y_)
True

>> MatchQ(6, _Integer)
True
```

`ReplaceAll` (operator `/.`) replaces all occurrences of a pattern in an expression using a `Rule` given by `->`:
```
>> {2, "a", 3, 2.5, "b", c} /. x_Integer -> x ^ 2
{4,"a",9,2.5,"b",c}
```

You can also specify a list of rules:
```
>> {2, "a", 3, 2.5, "b", c} /. {x_Integer -> x ^ 2.0, y_String -> 10}
{4.0,10,9.0,2.5,10,c}
```

`ReplaceRepeated` (operator `//.`) applies a set of rules repeatedly, until the expression doesn't change anymore:
```
>> {2, "a", 3, 2.5, "b", c} //. {x_Integer -> x ^ 2.0, y_String -> 10}
{4.0,100.0,9.0,2.5,100.0,c}
```

There is a “delayed” version of `Rule` which can be specified by `:>` (similar to the relation of `:=` to `=`):
```
>> a :> 1 + 2
a:>1+2

>> a -> 1 + 2
a->3
```

This is useful when the right side of a rule should not be evaluated immediately (before matching):
```
>> {1, 2} /. x_Integer -> N(x)
{1,2}
```

Here, `N` is applied to x before the actual matching, simply yielding x. With a delayed rule this can be avoided:
```
>> {1, 2} /. x_Integer :> N(x)
{1.0,2.0}
```

In addition to defining functions as rules for certain patterns, there are pure functions that can be defined using the `&` postfix operator, 
where everything before it is treated as the function body and `#` can be used as argument placeholder:
```
>> h = # ^ 2 &;

>> h(3)
9
```

Multiple arguments can simply be indexed:
```
>> s = #1 + #2 &;

>> s(4, 6)
10
```

It is also possible to name arguments using `Function`:
```
>> p = Function({x, y}, x * y);

>> p(4, 6)
24
```

Pure functions are very handy when functions are used only locally, e.g., when combined with operators like `Map`:
```
>> # ^ 2 & /@ Range(5)
{1,4,9,16,25}
```

Sort according to the second part of a list:
```
>> Sort({{x, 10}, {y, 2}, {z, 5}}, #1[[2]] < #2[[2]] &)
{{y,2},{z,5},{x,10}}
```

Functions can be applied using postfix notation, in addition to using `()`:
```
>> 3 // h
9
```
