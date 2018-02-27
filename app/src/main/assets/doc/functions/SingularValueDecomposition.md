## SingularValueDecomposition
```
SingularValueDecomposition(matrix)
```
> calculates the singular value decomposition for the `matrix`.

'SingularValueDecomposition' returns `u`, `s`, `w` such that `matrix =u s v`,
`u' u`=1, `v' v`=1, and `s` is diagonal.

See:
* [Wikipedia: Singular value decomposition](https://en.wikipedia.org/wiki/Singular_value_decomposition)

### Examples
```
>> SingularValueDecomposition({{1.5, 2.0}, {2.5, 3.0}}) 
{
{{0.5389535334972082,0.8423354965397538},
 {0.8423354965397537,-0.5389535334972083}},
{{4.635554529660638,0.0},
 {0.0,0.10786196059193007}},
{{0.6286775450376476,-0.7776660879615599},
 {0.7776660879615599,0.6286775450376476}}}
```

Symbolic SVD is not implemented, performing numerically.
```
>> SingularValueDecomposition({{3/2, 2}, {5/2, 3}}) 
{
{{0.5389535334972082,0.8423354965397538},
 {0.8423354965397537,-0.5389535334972083}},
{{4.635554529660638,0.0},
 {0.0,0.10786196059193007}},
{{0.6286775450376476,-0.7776660879615599},
 {0.7776660879615599,0.6286775450376476}}}
```

Argument {1, {2}} at position 1 is not a non-empty rectangular matrix.
```
>> SingularValueDecomposition({1, {2}})
SingularValueDecomposition({1, {2}})
```