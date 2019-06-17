## ReplaceRepeated

```
ReplaceRepeated(expr, lhs -> rhs)

expr //. lhs -> rhs
```

or

```
ReplaceRepeated(expr, lhs :> rhs)

expr //. lhs :> rhs
```

> repeatedly applies the rule `lhs -> rhs` to `expr` until  the result no longer changes. 
 
### Examples

```
>> a+b+c //. c->d
a+b+d
```

Simplification of logarithms:

```
>> logrules = {Log(x_ * y_) :> Log(x) + Log(y), Log(x_^y_) :> y * Log(x)};

>> Log(a * (b * c) ^ d ^ e * f) //. logrules
Log(a)+d^e*(Log(b)+Log(c))+Log(f) 
```

`ReplaceAll` just performs a single replacement:

```
>> Log(a * (b * c) ^ d ^ e * f) /. logrules
Log(a)+Log((b*c)^d^e*f) 
```

### Related terms 
[Replace](Replace.md), [ReplaceAll](ReplaceAll.md), [ReplaceList](ReplaceList.md), [ReplacePart](ReplacePart.md)
