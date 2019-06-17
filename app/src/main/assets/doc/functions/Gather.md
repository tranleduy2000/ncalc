## Gather

```
Gather(list, test) 
```

> gathers leaves of `list` into sub lists of items that are the same according to `test`.


```
Gather(list) 
```

> gathers leaves of `list` into sub lists of items that are the same.


### Examples

The order of the items inside the sub lists is the same as in the original list.

``` 
>> Gather({1, 7, 3, 7, 2, 3, 9})
{{1},{7,7},{3,3},{2},{9}}

>> Gather({1/3, 2/6, 1/9})
{{1/3,1/3},{1/9}}
```