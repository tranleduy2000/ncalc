## Partition

```
Partition(list, n)
```

> partitions `list` into sublists of length `n`.

``` 
Partition(list, n, d)
```

> partitions `list` into sublists of length `n` which overlap `d` indices.

### Examples

``` 
>> Partition({a, b, c, d, e, f}, 2)
{{a,b},{c,d},{e,f}}
 
>> Partition({a, b, c, d, e, f}, 3, 1)
{{a,b,c},{b,c,d},{c,d,e},{d,e,f}} 
 
>> Partition({a, b, c, d, e}, 2)
{{a,b},{c,d}}
``` 