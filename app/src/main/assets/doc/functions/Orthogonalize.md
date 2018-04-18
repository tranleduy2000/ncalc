## Orthogonalize

```
Orthogonalize(matrix)
```

> returns a basis for the orthogonalized set of vectors defined by `matrix`.

See:
* [Wikipedia - Gram–Schmidt process (linear algebra](https://en.wikipedia.org/wiki/Gram%E2%80%93Schmidt_process)

### Examples

```
>> Orthogonalize({{3,1},{2,2}})
{{3/Sqrt(10),1/Sqrt(10)},{-Sqrt(5/2)/5,3/5*Sqrt(5/2)}}
```
