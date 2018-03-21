## ListConvolve

```
ListConvolve(kernel-list, tensor-list)
```

> create the convolution of the `kernel-list` with `tensor-list`.

### Examples

```
>> ListConvolve({x, y}, {a, b, c, d, e, f})
{b*x+a*y,c*x+b*y,d*x+c*y,e*x+d*y,f*x+e*y}
```