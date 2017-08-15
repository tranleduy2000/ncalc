## ReplacePart

```
ReplacePart(expr, i -> new)
```
> replaces part `i` in `expr` with `new`.

```
ReplacePart(expr, {{i, j} -> e1, {k, l} -> e2})'
```

> replaces parts `i` and `j` with `e1`, and parts `k` and `l` with `e2`.

### Examples

```
>> ReplacePart({a, b, c}, 1 -> t)
{t,b,c}

>> ReplacePart({{a, b}, {c, d}}, {2, 1} -> t)
{{a,b},{t,d}}
 
>> ReplacePart({{a, b}, {c, d}}, {{2, 1} -> t, {1, 1} -> t})
{{t,b},{t,d}}
 
>> ReplacePart({a, b, c}, {{1}, {2}} -> t)
{t,t,c}
```

Delayed rules are evaluated once for each replacement:
```
>> n = 1
>> ReplacePart({a, b, c, d}, {{1}, {3}} :> n++)
{1,b,2,d} 
```

Non-existing parts are simply ignored:
```
>> ReplacePart({a, b, c}, 4 -> t)
{a,b,c}
```

You can replace heads by replacing part `0`:
```
>> ReplacePart({a, b, c}, 0 -> Times)
a*b*c
```
 
Negative part numbers count from the end:
```
>> ReplacePart({a, b, c}, -1 -> t)
{a,b,t}
```