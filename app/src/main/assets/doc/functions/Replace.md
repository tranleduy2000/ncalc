## Replace

```
Replace(expr, lhs -> rhs)
```

> replaces the left-hand-side pattern expression `lhs` in `expr` with the right-hand-side `rhs`.

```
Replace(expr, {lhs1 -> rhs1, lhs2 -> rhs2, ... } )
```
 
> replaces the left-hand-side patterns expression `lhsX` in `expr` with the right-hand-side `rhsX`.
 
## Examples

```
>> Replace(x, {{e->q, x -> a}, {x -> b}})
{a,b}
```


### Related terms 
[ReplaceAll](ReplaceAll.md), [ReplaceList](ReplaceList.md), [ReplacePart](ReplacePart.md), [ReplaceRepeated](ReplaceRepeated.md)
