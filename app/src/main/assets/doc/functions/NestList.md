## NestList

```
NestList(f, expr, n)
```
> starting with `expr`, iteratively applies `f` `n` times and returns a list of all intermediate results.

### Examples
 
``` 
>> NestList(f, x, 3)
{x,f(x),f(f(x)),f(f(f(x)))}
 
>> NestList(2 # &, 1, 8)
{1,2,4,8,16,32,64,128,256} 
```
  


