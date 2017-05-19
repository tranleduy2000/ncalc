## Select

```
Select({e1, e2, ...}, f)
```
> returns a list of the elements `ei` for which `f(ei)` returns `True`.

### Examples

Find numbers greater than zero:
```
>> Select({-3, 0, 1, 3, a}, #>0&)
{1,3}
```

`Select` works on an expression with any head:
```
>> Select(f(a, 2, 3), NumberQ)
f(2,3)
```

Nonatomic expression expected.
```
>> Select(a, True) 
Select(a,True)
```