## SatisfiableQ

```
SatisfiableQ(boolean-expr, list-of-variables)
```

> test whether the `boolean-expr` is satisfiable by a combination of boolean `False` and `True` values for the `list-of-variables`.

See: 
* [Wikipedia - Boolean satisfiability problem](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem)

### Examples

```
>> SatisfiableQ((a || b) && (! a || ! b), {a, b})
True
```