## Sort
``` 
Sort(list)
``` 

> sorts $list$ (or the leaves of any other expression) according to canonical ordering.

``` 
Sort(list, p) 
``` 

> sorts using `p` to determine the order of two elements.
 
### Examples
```
>> Sort({4, 1.0, a, 3+I})
{1.0,4,3+I,a}
```

 