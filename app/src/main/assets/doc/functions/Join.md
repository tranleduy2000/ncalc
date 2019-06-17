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
 
The [A001597](http://oeis.org/A001597) Perfect powers: `m^k` where `m > 0` and `k >= 2`

```
>> Join({1}, Select(Range(1770), GCD@@FactorInteger(#)[[All, 2]]>1&))
{1,4,8,9,16,25,27,32,36,49,64,81,100,121,125,128,144,169,196,216,225,243,256,289,324,343,361,400,441,484,512,529,576,625,676,729,784,841,900,961,1000,1024,1089,1156,1225,1296,1331,1369,1444,1521,1600,1681,1728,1764}
```
