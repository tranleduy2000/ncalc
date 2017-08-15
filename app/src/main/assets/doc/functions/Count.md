## Count
```
Count(list, pattern)
```
> returns the number of times `pattern` appears in `list`.

```
Count(list, pattern, ls)
```
> counts the elements matching at levelspec `ls`.

### Examples
```
>> Count({3, 7, 10, 7, 5, 3, 7, 10}, 3)
2
 
>> Count({{a, a}, {a, a, a}, a}, a, {2})
5
```