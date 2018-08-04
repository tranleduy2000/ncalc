## FromDigits

```
FromDigits(list)
```

> creates an expression from the list of digits for radix `10`.

```
FromDigits(list, radix)
```

> creates an expression from the list of digits for the given `radix`.

```
FromDigits(string)
```

> creates an expression from the characters in the `string` for radix `10`.

```
FromDigits(string, radix)
```

> creates an expression from the characters in the `string` for radix `10`.

### Examples

```
>> FromDigits("789abc")
790122

>> FromDigits("789abc", 16)
790122

>> FromDigits({1,1,1,1,0,1,1}, 2)
123
```

The [A023391 a(n+1) = a(n) converted to base 9 from base 8 (written in base 10)](https://oeis.org/A023391) integer sequence

```
>> NestList(FromDigits(IntegerDigits(#, 8), 9) &, 8, 50) 
{8,9,10,11,12,13,14,15,16,18,20,22,24,27,30,33,37,41,46,51,57,64,81,100,121,145,"181,221,275,345,433,541,761,1036,1471,2014,2787,3927,5533,8537,13555,21441,34102,60891,103386,185033,329032,651411,1286139,2551404,5654254}
```
