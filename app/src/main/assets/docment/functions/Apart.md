## Apart

```
Apart(expr)
```

> rewrites `expr` as a sum of individual fractions. 

```
Apart(expr, var)
``` 

> treats `var` as main variable. 

### Examples

``` 
>> Apart((x-1)/(x^2+x))
2/(x+1)-1/x
 
>> Apart(1 / (x^2 + 5x + 6))
1/(2+x)+1/(-3-x) 
```

When several variables are involved, the results can be different depending on the main variable:
```
>> Apart(1 / (x^2 - y^2), x)
-1 / (2 y (x + y)) + 1 / (2 y (x - y))
>> Apart(1 / (x^2 - y^2), y)
1 / (2 x (x + y)) + 1 / (2 x (x - y))
```

'Apart' is 'Listable':
```
>> Apart({1 / (x^2 + 5x + 6)})
{1/(2+x)+1/(-3-x)}
```

But it does not touch other expressions:
```
>> Sin(1 / (x ^ 2 - y ^ 2)) // Apart
Sin(1/(x^2-y^2))
```  