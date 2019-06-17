## ReplaceList

```
ReplaceList(expr, lhs -> rhs)
```

or

```
ReplaceList(expr, lhs :> rhs)
```
> replaces the left-hand-side pattern expression `lhs` in `expr` with the right-hand-side `rhs`.
 
### Examples

```
>> ReplaceList(a+b+c,(x_+y_) :> {{x},{y}})
{{{a},{b+c}},{{b},{a+c}},{{c},{a+b}},{{a+b},{c}},{{a+c},{b}},{{b+c},{a}}} 
```


### Related terms 
[Replace](Replace.md), [ReplaceAll](ReplaceAll.md), [ReplacePart](ReplacePart.md), [ReplaceRepeated](ReplaceRepeated.md)
