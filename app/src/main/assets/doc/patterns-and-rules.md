## Patterns and rules

Some examples:
```
>> a + b + c /. a + b -> t
c+t
 
>> a + 2 + b + c + x * y /. n_Integer + s__Symbol + rest_ -> {n, s, rest}
{2,a,b+c+x*y}
 
>> f(a, b, c, d) /. f(first_, rest___) -> {first, {rest}}
{a,{b,c,d}}
```

Tests and Conditions:
```
>> f(4) /. f(x_?(# > 0&)) -> x ^ 2
16
 
>> f(4) /. f(x_) /; x > 0 -> x ^ 2
16
```

Leaves in the beginning of a pattern rather match fewer leaves:
```
>> f(a, b, c, d) /. f(start__, end__) -> {{start}, {end}}
{{a},{b,c,d}}
```
 
Optional arguments using `Optional`:
```
>> f(a) /. f(x_, y_:3) -> {x, y}
{a,3}
```

The attributes `Flat`, `Orderless`, and `OneIdentity` affect pattern matching.

## Functions Reference 

* [Alternatives](functions/Alternatives.md)
* [Condition](functions/Condition.md)
* [Except](functions/Except.md)
* [MatchQ](functions/MatchQ.md)
* [Optional](functions/Optional.md)
* [PatternTest](functions/PatternTest.md)
* [Rule](functions/Rule.md)
* [RuleDelayed](functions/RuleDelayed.md)