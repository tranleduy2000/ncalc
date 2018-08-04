## BooleanVariables

```
BooleanVariables(logical-expr)
```

> gives a list of the boolean variables that appear in the `logical-expr`.

### Examples

```
>> BooleanVariables(Xor(p,q,r))
{p,q,r}
```