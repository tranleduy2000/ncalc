## Transpose

```
Transpose(m)
```

> transposes rows and columns in the matrix `m`.

```
Transpose(tensor, permutation-list)
```

> transposes rows and columns in the `tensor` according to `permutation-list`.

See:
* [Wikipedia - Transpose](https://en.wikipedia.org/wiki/Transpose)

### Examples

``` 
>> Transpose({{1, 2, 3}, {4, 5, 6}})
{{1, 4}, {2, 5}, {3, 6}}

>> MatrixForm(%)
1   4
2   5
3   6
 
>> Transpose(x)
Transpose(x)

>> Transpose({{1, 2, 3}, {4, 5, 6}}, {2,1})
{{1,4},{2,5},{3,6}}

>> Transpose({{1, 2, 3}, {4, 5, 6}}, {1,2})
{{1,2,3},{4,5,6}}
``` 