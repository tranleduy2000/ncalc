## Join
```
Join(l1, l2)
```
> concatenates the lists `l1` and `l2`.

### Examples

`Join` concatenates lists:
```
>> Join({a, b}, {c, d, e})
{a,b,c,d,e}
 
>> Join({{a, b}, {c, d}}, {{1, 2}, {3, 4}})
{{a,b},{c,d},{1,2},{3,4}} 
```

The concatenated expressions may have any head:
```
>> Join(a + b, c + d, e + f)
a+b+c+d+e+f
```

However, it must be the same for all expressions:
```
>> Join(a + b, c * d)
Join(a+b,c*d)
 
>> Join(x, y)
Join(x,y)
 
>> Join(x + y, z)
Join(x+y,z)
 
>> Join(x + y, y * z, a)
Join(x + y, y z, a)
 
>> Join(x, y + z, y * z)
Join(x,y+z,y*z)
```