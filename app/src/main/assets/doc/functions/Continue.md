## Continue

``` 
Continue()
``` 
> continues with the next iteration in a `For`, `While`, or `Do` loop.

### Examples
``` 
>> For(i=1, i<=8, i=i+1, If(Mod(i,2) == 0, Continue()); Print(i))
 | 1
 | 3
 | 5
 | 7
```
 


