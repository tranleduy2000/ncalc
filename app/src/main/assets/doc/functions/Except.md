## Except

``` 
Except(c)
``` 
> represents a pattern object that matches any expression except those matching `c`.

``` 
Except(c, p)
``` 
> represents a pattern object that matches `p` but not `c`.
 
### Examples

```
>> Cases({x, a, b, x, c}, Except(x))
{a,b,c}

>> Cases({a, 0, b, 1, c, 2, 3}, Except(1, _Integer))
{0,2,3}
```