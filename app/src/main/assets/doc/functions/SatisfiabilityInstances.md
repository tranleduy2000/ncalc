## SatisfiabilityInstances


```
SatisfiabilityInstances(boolean-expr, list-of-variables)
```

> test whether the `boolean-expr` is satisfiable by a combination of boolean `False` and `True` values for the `list-of-variables` and return exactly one instance of `True, False` combinations if possible.

```
SatisfiabilityInstances(boolean-expr, list-of-variables, combinations)
```

> test whether the `boolean-expr` is satisfiable by a combination of boolean `False` and `True` values for the `list-of-variables` and return up to `combinations` instances of `True, False` combinations if possible.


See: 
* [Wikipedia - Boolean satisfiability problem](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem)

### Examples

```
>> SatisfiabilityInstances((a || b) && (! a || ! b), {a, b}, All)
{{False,True},{True,False}}
```