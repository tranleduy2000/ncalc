## SurvivalFunction

```
SurvivalFunction(dist, x)
```

> returns the survival function for the distribution `dist` evaluated at `x`. 

```
SurvivalFunction(dist, {x1, x2, ...})
```

> returns the survival function at `{x1, x2, ...}`.

```
SurvivalFunction(dist)
```

> returns the survival function as a pure function. 

### Examples

```
>> SurvivalFunction(NormalDistribution(0, 1), x)
1-Erfc(-x/Sqrt(2))/2
```
