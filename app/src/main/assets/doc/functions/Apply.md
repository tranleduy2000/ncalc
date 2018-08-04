## Apply

``` 
f @ expr
```

> returns `f(expr)`

```
Apply(f, expr)

f @@ expr
```

> replaces the head of `expr` with `f`.

```
Apply(f, expr, levelspec)
```

> applies `f` on the parts specified by `levelspec`.


### Examples

```
>> f @@ {1, 2, 3}
f(1, 2, 3)
>> Plus @@ {1, 2, 3}
6
```
 
The head of `expr` need not be `List`:

```
>> f @@ (a + b + c)
f(a, b, c)
```

Apply on level 1:

```
>> Apply(f, {a + b, g(c, d, e * f), 3}, {1})
{f(a, b), f(c, d, e*f), 3}
```

The default level is 0:

```
>> Apply(f, {a, b, c}, {0})
f(a, b, c)
```

Range of levels, including negative level (counting from bottom):

```
>> Apply(f, {{{{{a}}}}}, {2, -3})
{{f(f({a}))}}
```

Convert all operations to lists:

```
>> Apply(List, a + b * c ^ e * f(g), {0, Infinity})
{a,{b,{c,e},{g}}}
```

Level specification x + y is not of the form n, {n}, or {m, n}.

```
>> Apply(f, {a, b, c}, x+y) 
Apply(f, {a, b, c}, x + y)
```

The [A001597](http://oeis.org/A001597) Perfect powers: `m^k` where `m > 0` and `k >= 2`

```
>> $min = 0; $max = 10^4; Union@ Flatten@ Table( n^expo, {expo, Prime@ Range@ PrimePi@ Log2@ $max}, {n, Floor(1 + $min^(1/expo)), $max^(1/expo)})
{1,4,8,9,16,25,27,32,36,49,64,81,100,121,125,128,144,169,196,216,225,243,256,289,"324,343,361,400,441,484,512,529,576,625,676,729,784,841,900,961,1000,1024,1089,1156,1225,1296,1331,1369,1444,1521,1600,1681,1728,1764,1849,1936,2025,2048,2116,2187,2197,2209,2304,2401,2500,2601,2704,2744,2809,2916,3025,3125,3136,3249,3364,3375,3481,3600,3721,3844,3969,4096,4225,4356,4489,4624,4761,4900,4913,5041,5184, "5329,5476,5625,5776,5832,5929,6084,6241,6400,6561,6724,6859,6889,7056,7225,7396,7569,7744,7776,7921,8000,8100,8192,8281,8464,8649,8836,9025,9216,9261,9409,9604,9801,10000}
```