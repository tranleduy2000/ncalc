## Last

``` 
Last(expr)
``` 
> returns the last element in `expr`.

### Examples
`Last(expr)` is equivalent to `expr[[-1]]`.
``` 
>> Last({a, b, c})
c
``` 

Nonatomic expression expected.
``` 
>> Last(x)
Last(x)
``` 