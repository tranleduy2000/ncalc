## Extract

```
Extract(expr, list)
```
> extracts parts of `expr` specified by `list`.

```
Extract(expr, {list1, list2, ...})'
```
> extracts a list of parts.

### Examples

`Extract(expr, i, j, ...)` is equivalent to `Part(expr, {i, j, ...})`.

```
>> Extract(a + b + c, {2})
b
 
>> Extract({{a, b}, {c, d}}, {{1}, {2, 2}})
{{a,b},d}
```