## ListCorrelate

```
ListCorrelate(kernel-list, tensor-list)
```

> create the correlation of the `kernel-list` with `tensor-list`.

### Examples

```
>> ListCorrelate({x, y}, {a, b, c, d, e, f})
{a*x+b*y,b*x+c*y,c*x+d*y,d*x+e*y,e*x+f*y}
```