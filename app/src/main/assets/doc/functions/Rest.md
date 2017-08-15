## Rest

```
Rest(expr)
```
> returns `expr` with the first element removed.

`Rest(expr)` is equivalent to `expr[[2;;]]`.

### Examples
```
>> Rest({a, b, c})
{b,c}
 
>> Rest(a + b + c)
b+c
```

Nonatomic expression expected.
```
>> Rest(x)
Rest(x)
```