## IntegerDigits

```
IntegerDigits(n, base)
```

> returns a list of integer digits for `n` under `base`.

```
IntegerDigits(n, base, padLeft)
```

>  pads the result list on the left with maximum `padLeft` zeros.

### Examples

```
>> IntegerDigits(123)
{1,2,3}

>> IntegerDigits(-123)
{1,2,3}

>> IntegerDigits(123, 2)
{1,1,1,1,0,1,1}

>> IntegerDigits(123, 2, 10)
{0,0,0,1,1,1,1,0,1,1}

>> IntegerDigits({123,456,789}, 2, 10)
{{0,0,0,1,1,1,1,0,1,1},{0,1,1,1,0,0,1,0,0,0},{1,1,0,0,0,1,0,1,0,1}}
```

The [A018900 Sum of two distinct powers of 2](https://oeis.org/A018900) integer sequence

```
>> Select(Range(1000), (Count(IntegerDigits(#, 2), 1)==2)&)
{3,5,6,9,10,12,17,18,20,24,33,34,36,40,48,65,66,68,72,80,96,129,130,132,136,144,160,192,257,258,260,264,272,288,320,384,513,514,516,520,528,544,576,640,768}
```