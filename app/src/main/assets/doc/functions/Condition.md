## Condition

```
Condition(pattern, expr)
```
or
```
pattern /; expr
```
> places an additional constraint on `pattern` that only allows it to match if `expr` evaluates to `True`.
   
### Examples

The controlling expression of a `Condition` can use variables from the pattern:
```
> f(3) /. f(x_) /; x>0 -> t
t
	 
>> f(-3) /. f(x_) /; x>0 -> t
f(-3)
```

`Condition` can be used in an assignment:
```
>> f(x_) := p(x) /; x>0
>> f(3)
p(3)

>> f(-3)
f(-3)
```
