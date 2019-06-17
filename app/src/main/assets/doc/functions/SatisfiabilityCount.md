## SatisfiabilityCount

```
SatisfiabilityCount(boolean-expr)
```

> test whether the `boolean-expr` is satisfiable by a combination of boolean `False` and `True` values for the  variables of the boolean expression and return the number of possible combinations.

```
SatisfiabilityCount(boolean-expr, list-of-variables)
```

> test whether the `boolean-expr` is satisfiable by a combination of boolean `False` and `True` values for the `list-of-variables` and return the number of possible combinations.


See: 
* [Wikipedia - Boolean satisfiability problem](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem)

### Examples

```
>> SatisfiabilityCount((a || b) && (! a || ! b), {a, b})
2
```