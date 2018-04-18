## HoldPattern

```
HoldPattern(expr)
```

> `HoldPattern` doesn't evaluate `expr` for pattern-matching.


### Examples

One might be very surprised that the following line evaluates to `True`!

```
>> MatchQ(And(x, y, z), Times(p__))
True
```

When the line above is evaluated  `Times(p__)` evaluates to `(p__)` before the kernel checks to see if the pattern matches. `MatchQ` then determines if `And(x,y,z)` matches the pattern `(p__)` and it does because `And(x,y,z)` is itself a sequence of one.

Now the next cell also evaluates to `True` because both `( And(p__) )` and `( Times(p__) )` evaluate to `( p__ )`.

```
>> Times(p__)===And(p__)
True
```

Prevent patterns in the examples above from evaluating by wrapping them with `HoldPattern` as in the following lines.

```
>> MatchQ(And(x, y, z), HoldPattern(Times(p__)))
False

>> HoldPattern(Times(p__))===HoldPattern(And(p__))
False
```

In the next lines `HoldPattern` is used to ensure the head `(And)` is changed to `(List)`.
The two examples that follow have the same effect, but the use of `HoldPattern` isn't needed.

```
>> And(x, y, z)/.HoldPattern(And(a__)) ->List(a)
{x,y,z}

>> And(x, y, z)/.And->List
{x,y,z}

>> And(x, y, z)/.And(a_,b___)->List(a,b)
{x,y,z}
```





