## First

```
First(expr)
```
> returns the first element in `expr`.

### Examples

`First(expr)` is equivalent to `expr[[1]]`.
```
>> First({a, b, c})
a
 
>> First(a + b + c)
a
```

Nonatomic expression expected.
```
>> First(x)
First(x)
```