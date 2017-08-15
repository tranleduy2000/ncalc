## Tuples
``` 
Tuples(list, n)
``` 
> creates a list of all `n`-tuples of elements in `list`.

``` 
Tuples({list1, list2, ...})
``` 
> returns a list of tuples with elements from the given lists.

### Examples
``` 
>> Tuples({a, b, c}, 2)
{{a,a},{a,b},{a,c},{b,a},{b,b},{b,c},{c,a},{c,b},{c,c}}

>> Tuples[{{a, b}, {1, 2, 3}}]
{{a,1},{a,2},{a,3},{b,1},{b,2},{b,3}}
``` 